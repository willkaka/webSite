package com.hyw.webSite.utils.excel;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.crab2died.utils.DateUtils;
import com.github.crab2died.utils.RegularUtils;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.dbservice.NQueryWrapper;
import com.hyw.webSite.exception.BizException;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class ExcelService {

    @Autowired
    private DataService dataService;

    /**
     * 读取excel记录,并转为对象输出
     * @param excelFile excel文件
     * @param templateName 模板名称
     * @param clazz 对象类
     * @param excelRecordMax Excel文件最大允许的记录数
     * @return List<T>
     */
    public <T> List<T> readExcel(File excelFile,String templateName,Class<T> clazz, int excelRecordMax){
        List<JSONObject> recordJsonObjectList = readExcel(excelFile,templateName,excelRecordMax);
        List<T> list = new ArrayList<>();
        for(JSONObject object:recordJsonObjectList){
            list.add(object.toJavaObject(clazz));
        }
        return list;
    }

    /**
     * 读取excel记录
     * @param excelFile excel文件
     * @param templateName 模板名称
     * @param excelRecordMax Excel文件最大允许的记录数
     * @return List<JSONObject>
     */
    public List<JSONObject> readExcel(File excelFile, String templateName, int excelRecordMax) {
        //只支持xlsx文件
        Assert.isTrue(excelFile.getName().toUpperCase().endsWith("XLSX") || excelFile.getName().toUpperCase().endsWith("XLS"),
                String.format("当前文件(%s),暂时只支持xls和xlsx文件！",excelFile.getName()));
        //检查文件
        Assert.isTrue(excelFile.isFile(),
                String.format("本地文件路径(%s)不是文件,不处理！",excelFile.getName()));
        //打开文件
        Workbook workbook = null;
        try {
            if(excelFile.getName().toUpperCase().endsWith("XLSX")) {
                workbook = StreamingReader.builder()
                        .rowCacheSize(100)  //缓存到内存中的行数，默认是10
                        .bufferSize(4096)  //读取资源时，缓存到内存的字节大小，默认是1024
                        .open(Files.newInputStream(excelFile.toPath()));  //打开资源，必须，可以是InputStream或者是File，注意：只能打开XLSX格式的文件
            }else if(excelFile.getName().toUpperCase().endsWith("XLS")){
                workbook = new HSSFWorkbook(Files.newInputStream(excelFile.toPath()));
            }
        }catch (Exception e){
            log.error("读取本地文件({})异常！",excelFile.getPath(),e);
            throw new BizException("","读取本地文件("+excelFile.getPath()+")异常！");
        }
        Assert.isTrue(workbook!=null,String.format("Excel文件(%s)打开异常！",excelFile.getName()));

        //只取首个sheet
        Sheet sheet = workbook.getSheetAt(0);

        //检查文件总记录数
        if(excelRecordMax != 0){
           Assert.isTrue(sheet.getLastRowNum() <= excelRecordMax,
                   String.format("Excel文件(%s)总记录数为%s，大于允许的最大值(%s)。请拆成多个文件重新导入！",
                           excelFile.getName(),sheet.getLastRowNum(),excelRecordMax));
        }

        return checkHeader(sheet,excelFile.getName(),templateName);
    }

    /**
     * 检查表头文本
     * @param sheet sheet
     * @param excelFileName excel文件名
     * @param templateName 模板名称
     * @return List<JSONObject>
     */
    private List<JSONObject> checkHeader(Sheet sheet, String excelFileName, String templateName) {
        //检查表头
        @SuppressWarnings("unchecked")
        List<TemplateDefine> headerList = dataService.list(new NQueryWrapper<TemplateDefine>()
                .eq(TemplateDefine::getTemplateName, templateName)
                .eq(TemplateDefine::getDefType, "Header")
                .orderByAsc(TemplateDefine::getPosRow, TemplateDefine::getPosCol));
        Assert.isTrue(CollectionUtils.isNotEmpty(headerList), String.format("未配置模板(%s)的表头信息!", templateName));
        //因Row row = sheet.getRow(header.getPosRow());会报异常。
        List<Integer> headerRows = new ArrayList<>();
        headerList.forEach(h->{if(!headerRows.contains(h.getPosRow()))headerRows.add(h.getPosRow());});

        if(excelFileName.toUpperCase().endsWith("XLSX")) {
            int minRow = Integer.MAX_VALUE,maxRow = 0, minCol = Integer.MAX_VALUE, maxCol = 0;
            for(TemplateDefine templateDefine:headerList){
                minRow = (minRow > templateDefine.getPosRow())?templateDefine.getPosRow():minRow;
                minCol = (minCol > templateDefine.getPosCol())?templateDefine.getPosCol():minCol;
                maxRow = (maxRow > templateDefine.getPosRow())?templateDefine.getPosRow():maxRow;
                maxCol = (maxCol < templateDefine.getPosCol())?templateDefine.getPosCol():maxCol;
            }
            for (Row row : sheet) {
                int rowNum = row.getRowNum();
                if (rowNum < minRow) continue;
                if (rowNum > maxRow) break;
                for (Cell cell : row) {
                    int colNum = cell.getColumnIndex();
                    if(colNum < minCol) continue;
                    if(colNum > maxCol) break;
                    TemplateDefine templateDefine = getByRowAndColNum(headerList, rowNum, colNum);
                    if (null == templateDefine) continue;
                    String headerStr = cell.getStringCellValue();
                    Assert.isTrue(headerStr.equalsIgnoreCase(templateDefine.getFieldName()),
                            String.format("表头第%s行第%s列名称应为(%s),当前值为(%s)", templateDefine.getPosRow()+1, templateDefine.getPosCol()+1,
                                    templateDefine.getFieldName(), headerStr));
                }
            }
        }else if(excelFileName.toUpperCase().endsWith("XLS")){
            for (TemplateDefine header : headerList) {
                Row row = sheet.getRow(header.getPosRow());
                Cell cell = row.getCell(header.getPosCol());
                String headerStr = cell.getStringCellValue();
                Assert.isTrue(headerStr.equalsIgnoreCase(header.getFieldName()),
                        String.format("表头第%s行第%s列名称应为(%s),当前值为(%s)", header.getPosRow()+1, header.getPosCol()+1,
                                header.getFieldName(), headerStr));
            }
        }

        return readRecords(sheet,excelFileName,templateName);
    }

    /**
     * 读取sheet记录
     * @param sheet sheet
     * @param excelFileName excel文件名
     * @param templateName 模板名称
     * @return List<JSONObject>
     */
    private List<JSONObject> readRecords(Sheet sheet, String excelFileName, String templateName){
        //取字段字义
        @SuppressWarnings( "unchecked" )
        List<TemplateDefine> fieldList = dataService.list(new NQueryWrapper<TemplateDefine>()
                .eq(TemplateDefine::getTemplateName,templateName)
                .eq(TemplateDefine::getDefType, "Detail")
                .orderByAsc(TemplateDefine::getPosRow,TemplateDefine::getPosCol));
        Assert.isTrue(CollectionUtils.isNotEmpty(fieldList),String.format("未配置模板(%s)的字段信息!",templateName));

        //遍历所有的行
        List<JSONObject> result = new ArrayList<>();
        int begRow = fieldList.get(0).getPosRow();
        int readRcdCnt = 0;
        for (Row row : sheet) {
            if (row.getRowNum() < begRow) continue;
            readRcdCnt ++;
            try {
                result.add(cvtCellToJson(row, fieldList));
            }catch (Exception e){
                log.error("读Excel表记录异常!",e);
                throw new BizException("","读Excel表记录异常!");
            }
        }
        log.info("共读取excel({})记录{}条。",excelFileName,readRcdCnt);
        return result;
    }

    /**
     * 读取行记录，并转为JSONObject输出
     * @param row 行
     * @param fieldList 列字段定义
     * @return JSONObject
     */
    private JSONObject cvtCellToJson(Row row,List<TemplateDefine> fieldList){
        //遍历所有的列
        int minColNum = getMinColNum(fieldList);
        JSONObject jsonObj = new JSONObject();
        for (Cell cell : row) {
            if (cell == null) continue;
            int colNum = cell.getColumnIndex();
            if(colNum < minColNum) continue;
            TemplateDefine templateDefine = getByColNum(fieldList, colNum);
            if (null == templateDefine) continue;

            String val = getCellValue(cell);
            if (null == val) {
                jsonObj.put(templateDefine.getFieldName(), null);
            } else if (val.equalsIgnoreCase("null")) {
                jsonObj.put(templateDefine.getFieldName(), null);
            } else if ("date".equalsIgnoreCase(templateDefine.getFieldType())) {
                try {
                    jsonObj.put(templateDefine.getFieldName(), ExcelDateFormatUtil.str2Date(val));
                } catch (ParseException e) {
                    log.error("模板日期转换异常，日期字符串(" + val + ").",e);
                    throw new BizException("","模板日期转换异常，日期字符串(" + val + ").");
                }
            } else {
                jsonObj.put(templateDefine.getFieldName(), val.trim());
            }
        }
        return jsonObj;
    }

    private TemplateDefine getByColNum(List<TemplateDefine> fieldList, int colNum){
        for(TemplateDefine templateDefine:fieldList){
            if(templateDefine.getPosCol() == colNum){
                return templateDefine;
            }
        }
        return null;
    }

    private TemplateDefine getByRowAndColNum(List<TemplateDefine> fieldList, int rowNum, int colNum){
        for(TemplateDefine templateDefine:fieldList){
            if(templateDefine.getPosRow() == rowNum && templateDefine.getPosCol() == colNum){
                return templateDefine;
            }
        }
        return null;
    }
    private int getMinColNum(List<TemplateDefine> fieldList){
        int minCol = 99;
        for(TemplateDefine templateDefine:fieldList){
            if(minCol > templateDefine.getPosCol()){
                minCol = templateDefine.getPosCol();
            }
        }
        return minCol;
    }

    /**
     * 获取单元格内容
     * @param c 单元格
     * @return 单元格内容
     */
    public String getCellValue(Cell c) {
        String o;
        switch (c.getCellTypeEnum()) {
            case BLANK:
                o = "";
                break;
            case BOOLEAN:
                o = String.valueOf(c.getBooleanCellValue());
                break;
            case FORMULA:
                o = calculationFormula(c);
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(c)) {
                    o = DateUtils.date2Str(c.getDateCellValue());
                } else {
                    o = String.valueOf(c.getNumericCellValue());
                    o = matchDoneBigDecimal(o);
                    o = RegularUtils.converNumByReg(o);
                }
                break;
            case STRING:
                o = c.getStringCellValue();
                break;
            default:
                o = null;
                break;
        }
        return o;
    }

    /**
     * 计算公式结果
     * @param cell 单元格类型为公式的单元格
     * @return 返回单元格计算后的值 格式化成String
     */
    public String calculationFormula(Cell cell) {
        CellValue cellValue = cell.getSheet().getWorkbook().getCreationHelper()
                .createFormulaEvaluator().evaluate(cell);
        if (cellValue.getCellTypeEnum().equals(CellType.STRING)){
            return  cellValue.getStringValue();
        }else{
            return cellValue.formatAsString();
        }
    }

    /**
     * 科学计数法数据转换
     * @param bigDecimal 科学计数法
     * @return 数据字符串
     */
    private static String matchDoneBigDecimal(String bigDecimal) {
        // 对科学计数法进行处理
        boolean flg = Pattern.matches("^-?\\d+(\\.\\d+)?(E-?\\d+)?$", bigDecimal);
        if (flg) {
            BigDecimal bd = new BigDecimal(bigDecimal);
            bigDecimal = bd.toPlainString();
        }
        return bigDecimal;
    }
}
