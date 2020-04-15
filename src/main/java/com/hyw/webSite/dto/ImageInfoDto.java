package com.hyw.webSite.dto;

import lombok.Data;

@Data
public class ImageInfoDto {

    private String exifMake;//0x010f Make 厂商
    private String exifModel;//0x0110 Model 机型
    private String exifChangedDateTime; //0x0132 Date/Time 修改时间
    private String exifOriginalDateTime; // 0x9003 Date/Time Original 拍摄时间
    private String exifDigitizedDateTime; // 0x9004 Date/Time Digitized 数字化时间
    private String exifImageWidth; //0xa002 Exif Image Width Exif图像宽度
    private String exifImageHeight; //0xa003 Exif Image Height Exif图像高度

    private String gpsLatitudeRef; //GPS Latitude Ref 纬度南S/北N
    private String gpsLatitude; //GPS Latitude 纬度
    private String gpsLongitudeRef; //GPS Longitude Ref 经度东E/西W
    private String gpsLongitude; //GPS Longitude 经度

    private String fileFileName; //File Name 文件名
    private String fileFileSize; //File Size文件大小
    private String fileFileModifiedDate; //File Modified Date 文件修改时间
}
