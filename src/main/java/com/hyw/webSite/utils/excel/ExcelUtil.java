package com.hyw.webSite.utils.excel;

import com.hyw.webSite.exception.IfThrow;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelUtil {


    public static void writeDataToExcel(File file, String sheetName,
                                 Map<String,String> headFieldName, List<Map<String,Object>> data) {
        Workbook workbook = getWorkbook(file);
        IfThrow.isTrue(workbook == null,"");

        generateSheet(workbook,data,headFieldName,sheetName);

        try{
            workbook.write(Files.newOutputStream(file.toPath()));
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                workbook.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static SXSSFWorkbook getWorkbook(File file){
        FileInputStream inputStream = null;
        SXSSFWorkbook workbook = null;
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        workbook = new SXSSFWorkbook(500);
        return workbook;
    }

    public static void writeRecordIntoExcel(File file, String sheetName,List<Map<String,Object>> dataList, Map<String,String> headFieldName) {
        if(file.exists()){
            WriteRecordToExcel(file,dataList,headFieldName,sheetName);
        }else{
            createNewExcelAndWrite(file,dataList,headFieldName,sheetName);
        }
    }


    public static void createNewExcelAndWrite(File file, List<Map<String,Object>> data, Map<String,String> headFieldName,
                                              String sheetName) {
        XSSFWorkbook workbook = new XSSFWorkbook();

        //遍历list,将数据写入Excel中
        generateSheet(workbook, data, headFieldName,sheetName);

        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            workbook.write(out);
            out.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void WriteRecordToExcel(File file, List<Map<String,Object>> data, Map<String,String> headFieldName,
                                              String sheetName) {
        FileInputStream in;
        XSSFWorkbook workbook = null;

        try {
            in = new FileInputStream(file);
            workbook = new XSSFWorkbook(in);
        }catch (Exception e){
            log.error("",e);
        }

        //遍历list,将数据写入Excel中
        generateSheet(workbook, data, headFieldName,sheetName);

        //workbook
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.flush();
            workbook.write(out);
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 生成sheet数据
     * @param workbook workbook
     * @param data 待导出数据
     * @param headFieldName 表头字段定义
     * @param sheetName 指定导出Excel的sheet名称
     */
    public static void generateSheet(Workbook workbook, List<Map<String,Object>> data, Map<String,String> headFieldName,
                              String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        boolean writeHead = true;
        if(sheet == null) {
            if (null != sheetName && !"".equals(sheetName)) {
                sheet = workbook.createSheet(sheetName);
            } else {
                sheet = workbook.createSheet();
            }
        }else{
            writeHead = false;
        }
        int lastRowNum = sheet.getLastRowNum();
//        int lastRowNum = sheet.getLastRowNum();
//        System.out.println("lastRowNum:"+sheet.getLastRowNum()+" lastFlushRowNum:"+((SXSSFSheet) sheet).getLastFlushedRowNum());

        Map<Integer,Integer> headColLengthSumMap = new HashMap<>();
        if (writeHead && headFieldName!=null && headFieldName.size()>0 && lastRowNum<=0) {
            Row row = sheet.createRow(lastRowNum>0?lastRowNum+1:0);
            // 写标题
            CellStyle style = workbook.createCellStyle();
//            style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());//title单元格背景色
            style.setFillForegroundColor((short) 44);//title单元格背景色 https://www.docin.com/p-1019071433.html
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font font = workbook.createFont();
            font.setFontName("宋体");
            font.setFontHeightInPoints((short) 12);//设置字体大小
            font.setBold(true);
            //边框
            style.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
            style.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
            style.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
            style.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框
            style.setFont(font);
            int i=0;
            for(String key:headFieldName.keySet()){
                row.createCell(i).setCellValue(headFieldName.get(key));
                row.getCell(i).setCellStyle(style);
                headColLengthSumMap.put(i,stringLength(headFieldName.get(key)));
                i++;
            }
        }
        // 写数据
        Map<Integer,Integer> colLengthSumMap = new HashMap<>();
        for (int i = 0; i < data.size(); i++) { // i-行号
            Row row = sheet.createRow(lastRowNum + i + 1);
            Map<String,Object> objData = data.get(i);

            int j=0;// j-列号
            for(String key:headFieldName.keySet()){
                Object valueObject = objData.get(key);
                String value;

                if(valueObject == null){
                    value = "";
                }else if (valueObject instanceof BigDecimal) {
                    value = ((BigDecimal)valueObject).setScale(6,BigDecimal.ROUND_HALF_UP).toString();
                    Double v = Double.valueOf(value);
                    row.createCell(j).setCellValue(v);
                }else if (valueObject instanceof LocalDate) {
                    value = ((LocalDate)valueObject).toString();
                    row.createCell(j).setCellValue(value);
                } else {
                    value = valueObject.toString();
                    row.createCell(j).setCellValue(value);
                }
                saveCellLength(colLengthSumMap,j,value.length());
                j++;
            }
        }

        //设置列宽为所有记录该列的平均字符长度
        if (writeHead) {
            for (Integer colNum : colLengthSumMap.keySet()) {
                int argWidth = colLengthSumMap.get(colNum) / data.size();
                int width = headColLengthSumMap.get(colNum) > argWidth ?
                        headColLengthSumMap.get(colNum) + 4 : argWidth + 4;
                sheet.setColumnWidth(colNum, width * 256);
//            System.out.println("设置第"+colNum+"列，宽度为"+width*256);
            }
        }
    }

    private static void saveCellLength(Map<Integer,Integer> colLengthSumMap, int colNum, int length){
        if(colLengthSumMap.containsKey(colNum)){
            colLengthSumMap.put(colNum,colLengthSumMap.get(colNum)+length);
        }else{
            colLengthSumMap.put(colNum,length);
        }
    }

    public static int stringLength(String s){
        if(s==null) return 0;
        String chineseString = "[\u0391-\uFFE5]";
        int len=0;
        for(int i=0;i<s.length();i++){
            String temp = s.substring(i,i+1);
            if(temp.matches(chineseString)){
                len += 2;
            }else{
                len += 1;
            }
        }
        return len;
    }
}
