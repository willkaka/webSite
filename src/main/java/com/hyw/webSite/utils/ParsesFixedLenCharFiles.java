package com.hyw.webSite.utils;

import com.hyw.webSite.utils.excel.ExcelUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.util.*;

/**
 * 解析定长字符文件
 * Parses fixed-length character files
 */
public class ParsesFixedLenCharFiles {

    public static void main(String[] args) {
        File dirFile = new File("D:\\002385\\20_生产维护单\\20240606-浦发保费扣款数据核对\\临时文件夹");
        String outFilePath = "D:\\002385\\20_生产维护单\\20240606-浦发保费扣款数据核对\\out.txt";
        String charsetName = "GBK";

        //字段:名称 起始位#长度
        Map<String, String> fieldPosMap = new LinkedHashMap<>();
        fieldPosMap.put("流水号", "107#19#String");      //107, 125  18
        fieldPosMap.put("成功失败code", "208#2#String"); //208, 209  1
        fieldPosMap.put("成功失败描述", "210#48#String"); //210, 257  47
        fieldPosMap.put("成功扣款金额", "142#13#BigDecimal"); //142, 154  12

        List<String> outLineList = readDirFile(dirFile, charsetName, fieldPosMap);
//        System.out.println(pathList);
        writeText(outLineList, outFilePath, fieldPosMap);
//        ExcelUtil.writeRecordIntoExcel(new File(outFilePath),"data",outLineList,null);
    }

    private static List<String> readDirFile(File dir, String charsetName, Map<String, String> fieldPosMap) {
        File[] files;
        if (dir.isDirectory()) {
            files = dir.listFiles();
            if (files == null || files.length == 0) return new ArrayList<>();
        } else {
            files = new File[1];
            files[0] = dir;
        }
        List<String> recordList = new ArrayList<>();
        for (File file : files) {
            if (file.isDirectory()) {
                recordList.addAll(readDirFile(file, charsetName, fieldPosMap));
            } else {
                List<String> subRecordList = readTextFileContent(file, charsetName, fieldPosMap);
                if (CollectionUtils.isNotEmpty(subRecordList)) {
                    recordList.addAll(subRecordList);
                }
            }
        }
        return recordList;
    }

    private static List<String> readTextFileContent(File file, String charsetName, Map<String, String> fieldPosMap) {
        List<String> outLineList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), charsetName))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (StringUtils.isBlank(line)) continue;
                StringBuilder s = new StringBuilder();
                s.append(file.getName()).append("\t");
                for (String fieldName : fieldPosMap.keySet()) {
                    String configValue = fieldPosMap.get(fieldName);
                    s.append(getSubField(charsetName, line, configValue)).append("\t");
                }
                outLineList.add(s.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outLineList;
    }

    private static Object getSubField(String charsetName, String line, String configValue) {
        String[] valueArray = configValue.split("#");
        String valueStr = substringWithChinese(charsetName, line,
                Integer.parseInt(valueArray[0]),
                Integer.parseInt(valueArray[0]) + Integer.parseInt(valueArray[1]) - 1);
        String type = valueArray[2];
        if (valueStr == null) {
            return null;
        } else if ("String".equals(type)) {
            return valueStr.trim();
        } else if ("Integer".equals(type)) {
            return Integer.parseInt(valueStr.trim());
        } else if ("BigDecimal".equals(type)) {
            return new BigDecimal(valueStr).divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP);
        }
        return null;
    }

    private static void writeText(List<String> lineList, String outFilePath,Map<String, String> fieldPosMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFilePath, true))) {
            StringBuilder sb = new StringBuilder();
            sb.append("文件名");
            for (String fieldName : fieldPosMap.keySet()) {
                if(StringUtils.isNotBlank(sb)){
                    sb.append("\t").append(fieldName);
                }else {
                    sb.append(fieldName);
                }
            }
            writer.write(sb.toString());
            writer.newLine(); // 换行

            // 写入日志内容和时间戳
            for (String s : lineList) {
                writer.write(s);
                writer.newLine(); // 换行
            }
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    public static String substringWithChinese(String charsetName, String str, int start, int end) {
        try {
            // 将字符串按照GBK编码进行截取
            byte[] bytes = str.getBytes(charsetName);
            // 截取指定位置的字节
            if (end > bytes.length || start >= bytes.length) {
                return null;
            }
            byte[] extractedBytes = copyBytes(bytes, start, end);
            // 将截取的字节按照GBK编码重新组合成字符串
            return new String(extractedBytes, charsetName);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Charset not supported: " + charsetName, e);
        }
    }

    private static byte[] copyBytes(byte[] bytes, int start, int end) {
        byte[] extractedBytes = new byte[end - start + 1];
        System.arraycopy(bytes, start, extractedBytes, 0, extractedBytes.length);
        return extractedBytes;
    }
}
