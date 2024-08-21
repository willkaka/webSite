package com.hyw.webSite.utils.excel;

import com.alibaba.fastjson.JSONObject;
import com.github.crab2died.utils.DateUtils;
import com.github.crab2died.utils.RegularUtils;
import com.hyw.webSite.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ExcelToJson {


    public static void main(String[] args){
        File file = new File("D:\\temp\\excel2json.xlsx");
        List<JSONObject> jsonObjectList = ExcelToJson.getExcelRecords(file);

        System.out.println(jsonObjectList);
    }
    /**
     * 读取导入文件记录
     *
     * @param file       文件
     * @return List<JSONObject>
     */
    public static List<JSONObject> getExcelRecords(File file) {
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
            try { if (wb != null) wb.close(); } catch (Exception ignored) { }
        }

        //遍历sheet
        List<JSONObject> result = new ArrayList<>();
        int sheetNo = 0;
        result.addAll(readRecordFromSheet(wb,sheetNo));

        return result;
    }


    /**
     * 从指定sheet中读取记录(JSONObject)
     * @param wb 表格
     * @param sheetNo sheet号
     * @return List<JSONObject>
     */
    private static <T> List<JSONObject> readRecordFromSheet(Workbook wb,int sheetNo){
        List<JSONObject> result = new ArrayList<>();
        //取excel sheet.
        Sheet sheet = wb.getSheetAt(sheetNo);

        // 循环遍历表sheet.getLastRowNum()是获取一个表最后一条记录的记录号，
        int maxNum = sheet.getLastRowNum();
        //取结果
        result.addAll(readRecords(sheet, 0, maxNum));
        return result;
    }


    /**
     * 分页读取sheet记录
     * @param sheet sheet
     * @param begRow 开始行
     * @param endRow 结束行
     * @return List<JSONObject>
     */
    private static List<JSONObject> readRecords(Sheet sheet,int begRow,int endRow){
        Map<Integer,String> headerMap = new HashMap<>();
        List<JSONObject> result = new ArrayList<>();
        for (int rowNum = begRow; rowNum <= endRow; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if(rowNum == 0){
                int colMax = row.getLastCellNum();
                for(int col=0;col <colMax;col++){
                    Cell cell = row.getCell(col);
                    headerMap.put(col,cell.getStringCellValue());
                }
                continue;
            }

            JSONObject jsonObj = new JSONObject();
            headerMap.forEach((colNum,fieldName)->{
                Cell cell = row.getCell(colNum);
//                String value = getCellValue(cell).toString();
                jsonObj.put(fieldName,getCellValue(cell));
            });
            if (jsonObj.size() > 0) result.add(jsonObj);
        }
        return result;
    }


    public static Object getCellValue(Cell cell){
        Object valueObject = null;
        if(cell.getCellType() == CellType.FORMULA.getCode()) {
//            valueObject = getCellValue(cell);
            if(cell.getCachedFormulaResultType() == CellType.FORMULA.getCode())
                valueObject = cell.getNumericCellValue();

            if(cell.getCachedFormulaResultType() == CellType.STRING.getCode())
                valueObject = cell.getRichStringCellValue();

        }else if(cell.getCellType() == CellType.NUMERIC.getCode()) {
            if (DateUtil.isCellDateFormatted(cell)) {
                valueObject = DateUtils.date2Str(cell.getDateCellValue());
            } else {
                valueObject = String.valueOf(cell.getNumericCellValue());
                valueObject = matchDoneBigDecimal(valueObject.toString());
                valueObject = RegularUtils.converNumByReg(valueObject.toString());
            }
        }else if(cell.getCellType() == CellType.STRING.getCode()){
            valueObject = cell.getStringCellValue();
        }else if(cell.getCellType() == CellType.BOOLEAN.getCode()) {
            valueObject = cell.getBooleanCellValue();
        }else if(cell.getCellType() == CellType.BLANK.getCode()) {
            valueObject = "";
        }else if(cell.getCellType() == CellType._NONE.getCode()) {
            valueObject = null;
        }else if(cell.getCellType() == CellType.ERROR.getCode()) {
            valueObject = "error";
        }else{}
        return valueObject;
    }

    /**
     * 科学计数法数据转换
     *
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
