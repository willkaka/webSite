package com.hyw.webSite.utils.excel;

import com.alibaba.fastjson.JSONObject;
import com.github.crab2died.utils.DateUtils;
import com.github.crab2died.utils.Utils;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Slf4j
@Service
public class ExcelTemplateUtil {

    @Autowired
    private DataService dataService;
    @Autowired
    private ThreadPoolTaskExecutor asyncExecutor;

    private final int THREAD_CNT= 8;

    /**
     * 解析excel后返回文件记录
     *
     * @param templateNo 模板号
     * @param clazz      数据解析对象
     * @return List<T>
     */
    public <T> List<T> getObjectList(String templateNo, File file, Class<T> clazz) {
        List<T> list;
        if (StringUtils.isBlank(templateNo)) {
            throw new BizException("读取EXCEL记录时，模板编号不允许为空！");
        }

        //读取导入文件记录
        List<JSONObject> result = getExcelRecords(templateNo, file);
        //将本地文件流按模板定义转为JSON
        try {
            list = JSONObject.parseArray(result.toString(), clazz);
        } catch (Exception e) {
            log.error("数据转对象异常！", e);
            throw new BizException("数据转对象异常！");
        }
        return list;
    }

    /**
     * 读取导入文件记录
     *
     * @param templateNo 模板号
     * @param file       文件
     * @return List<JSONObject>
     */
    public List<JSONObject> getExcelRecords(String templateNo, File file) {
        List<Map<String, Object>> mapList = dataService.mapList(new NQueryWrapper<TemplateDefine>()
                .eq(TemplateDefine::getTemplateName, templateNo)
                .selectFields(TemplateDefine::getSheetNo)
                .groupBy(TemplateDefine::getSheetNo)
        );

        //打开excel
        Workbook wb = null;
        try {
            if (file.getName().toUpperCase().endsWith("XLS")) {
                wb = new HSSFWorkbook(new FileInputStream(file));
            } else if (file.getName().toUpperCase().endsWith("XLSX")) {
                wb = new XSSFWorkbook(new FileInputStream(file));
            } else {
                throw new BizException("上传文件文件(" + file.getName() + ")暂不支持，目前仅支持后缀为.xls/.xlsx的文件！");
            }
        } catch (Exception e) {
            log.error("读取本地文件({})异常！", file.getPath(), e);
            throw new BizException("读取本地文件(" + file.getPath() + ")异常！");
        } finally {
            try {
                if (wb != null) wb.close();
            } catch (Exception ignored) {
            }
        }

        List<JSONObject> result = new ArrayList<>();
        for(Map<String,Object> map:mapList){
            int sheetNo = (Integer) map.getOrDefault("sheet_no","");
            result.addAll(readRecordFromSheet(wb,templateNo,sheetNo));
        }

        return result;
    }

    private List<JSONObject> readRecordFromSheet(Workbook wb,String templateNo,int sheetNo){
        List<JSONObject> result = new ArrayList<>();
        //取模板定义，表头信息
        List<TemplateDefine> headerList = dataService.list(new NQueryWrapper<TemplateDefine>()
                .eq(TemplateDefine::getTemplateName, templateNo)
                .eq(TemplateDefine::getDefType, "Header")
                .eq(TemplateDefine::getSheetNo, sheetNo)
                .orderByAsc(TemplateDefine::getPosRow, TemplateDefine::getPosCol));
        //记录字段定义
        List<TemplateDefine> fieldList = dataService.list(new NQueryWrapper<TemplateDefine>()
                .eq(TemplateDefine::getTemplateName, templateNo)
                .eq(TemplateDefine::getDefType, "Detail")
                .eq(TemplateDefine::getSheetNo, sheetNo)
                .orderByAsc(TemplateDefine::getPosRow, TemplateDefine::getPosCol));

        //取excel sheet.
        Sheet sheet = wb.getSheetAt(sheetNo);
        //检查模板表头
        if (CollectionUtils.isNotEmpty(headerList)) {
            checkTemplateHeader(sheet, headerList);
        }
        //模板字段定义
        if (CollectionUtils.isEmpty(fieldList)) {
            throw new BizException("模板(" + templateNo + ")未定义模板字段！");
        }

        // 循环遍历表sheet.getLastRowNum()是获取一个表最后一条记录的记录号，
        double maxNum = sheet.getLastRowNum();
        List<Future<List<JSONObject>>> futureList = new ArrayList<>();
        double pageSize = Math.ceil(maxNum / THREAD_CNT);
        for(double i=0; i<=maxNum; i=i+pageSize){
            double finalI = i;
            futureList.add(asyncExecutor.submit(() -> readRecords(sheet, (int) finalI+1, (int) (finalI +pageSize),fieldList)));
        }

        //取结果
        for (Future<List<JSONObject>> future : futureList) {
            try {
                result.addAll(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 分页读取sheet记录
     * @param sheet sheet
     * @param begRow 开始行
     * @param endRow 结束行
     * @param fieldList 字段定义
     * @return List<JSONObject>
     */
    private List<JSONObject> readRecords(Sheet sheet,int begRow,int endRow,List<TemplateDefine> fieldList){
        log.info("读取记录，行区间{}-{}.",begRow,endRow);
        List<JSONObject> result = new ArrayList<>();
        for (int rowNum = begRow; rowNum <= endRow; rowNum++) {
            JSONObject jsonObj = new JSONObject();
            for (TemplateDefine templateDefine : fieldList) {
                if (rowNum < templateDefine.getPosRow()) {
                    continue;
                }
                Row row = sheet.getRow(rowNum);
                if (null == row) continue;
                Cell cell = row.getCell(templateDefine.getPosCol());
                if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
                    break;
                }
                String val = Utils.getCellValue(cell);
                if("string".equalsIgnoreCase(templateDefine.getFieldType())) {
                    jsonObj.put(templateDefine.getFieldName(), val);
                }else if("number".equalsIgnoreCase(templateDefine.getFieldType())) {
                    jsonObj.put(templateDefine.getFieldName(), null == val || StringUtils.isBlank(val)?"0":val);
                }else if ("date".equals(templateDefine.getFieldType())) {
                    try {
                        jsonObj.put(templateDefine.getFieldName(), DateUtils.str2Date(val));
                    } catch (ParseException e) {
                        log.error("第{}条记录，模板日期转换异常，日期字符串({}).",rowNum,val);
                        throw new BizException("第"+rowNum+"条记录，模板日期转换异常，日期字符串(" + val + ").");
                    }
                } else {
                    jsonObj.put(templateDefine.getFieldName(), val.trim());
                }
            }
            if (jsonObj.size() > 0) result.add(jsonObj);
        }
        return result;
    }

    /**
     * 模板表头检查
     *
     * @param sheet      excel sheet
     * @param headerList 表头定义
     */
    public void checkTemplateHeader(Sheet sheet, List<TemplateDefine> headerList) {
        for (TemplateDefine templateDefine : headerList) {
            Row row = sheet.getRow(templateDefine.getPosRow());
            Cell cell = row.getCell(templateDefine.getPosCol());
            if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
                break;
            }
            String val = Utils.getCellValue(cell);
            if (!templateDefine.getFieldName().equals(val)) {
                throw new BizException(templateDefine.getTemplateName() + "-" + templateDefine.getTemplateDesc()
                                + ",导入文件的第" + templateDefine.getPosRow() + "行第" + templateDefine.getPosCol()
                                + "列表头应为:" + templateDefine.getFieldName() + ",而当前该位置值为:" + val);
            }
        }
    }
}
