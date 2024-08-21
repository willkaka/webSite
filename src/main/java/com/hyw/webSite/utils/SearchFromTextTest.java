package com.hyw.webSite.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class SearchFromTextTest {

    public static void main(String[] args){
//        File dirFile = new File("D:\\002385\\20_生产维护单");
        File dirFile = new File("D:\\002385\\20_生产维护单");
        List<String> pathList = readDirFile(dirFile,"LT2022081100076939");
//        System.out.println(pathList);
    }

    private static List<String> readDirFile(File dir,String searchText){
        if(!dir.isDirectory()) return new ArrayList<>();

        File [] files = dir.listFiles();
        if(files == null || files.length == 0) return new ArrayList<>();

        List<String> subFilePathList = new ArrayList<>();
        for(File file:files){
            if(file.isDirectory()){
                subFilePathList.addAll( readDirFile(file,searchText) );
            }else{
                if(file.getName().contains(searchText)){
                    subFilePathList.add(file.getPath());
                }else {
                    String filePath = readFile(file, searchText);
                    if (StringUtils.isNotBlank(filePath)) {
                        subFilePathList.add(filePath);
                    }
                }
            }
        }
        return subFilePathList;
    }

    private static String readFile(File file,String searchText){
        if(file.getName().endsWith(".txt") || file.getName().endsWith(".text")){
            return readTextFileContent(file,searchText);
//        }else if(file.getName().endsWith(".xls") || file.getName().endsWith(".xlsx")){
//            return readExcelFileContent(file,searchText);
        }else{
            return null;
        }
    }


    private static String readWordDocxFileContent(File file, String searchText){
        try (XWPFDocument doc = new XWPFDocument(new FileInputStream(file))) {
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            String documentText = extractor.getText();

            if (documentText.contains(searchText)) {
                System.out.println(file.getPath());
                return file.getPath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private static String readWordDocFileContent(File file, String searchText){
//        try (FileInputStream fis = new FileInputStream(file);
//             HWPFDocument doc = new HWPFDocument(fis);
//             WordExtractor extractor = new WordExtractor(doc)) {
//
//            String documentText = extractor.getText();
//
//            if (documentText.contains(searchText)) {
//                System.out.println(file.getPath());
//                return file.getPath();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private static String readExcelFileContent(File file, String searchText){
        try (Workbook workbook = WorkbookFactory.create(Files.newInputStream(file.toPath()))) {
            int sheetCount = workbook.getNumberOfSheets();
            for(int sheetNo = 0;sheetNo < sheetCount;sheetNo++) {
                Sheet sheet = workbook.getSheetAt(sheetNo); // 获取第一个工作表

                for (Row row : sheet) {
                    for (Cell cell : row) {
                        if(cell.getCellType() == CellType.STRING.getCode()) {
                            String cellValue = cell.getStringCellValue();
                            if (cellValue.contains(searchText)) {
                                System.out.println(file.getPath());
                                return file.getPath();
                            }
                        }
                    }
                }
            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String readTextFileContent(File file, String searchText){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.contains(searchText)){
                    System.out.println(file.getPath());
                    return file.getPath();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
