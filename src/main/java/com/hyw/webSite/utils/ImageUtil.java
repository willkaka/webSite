package com.hyw.webSite.utils;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.hyw.webSite.dto.ImageInfoDto;
import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {
    /**
     * 按尺寸原比例缩放图片
     * @param source 输入源
     * @param output 输出源
     * @param width 256
     * @param height 256
     * @throws IOException
     */
    public static void imgThumb(String source, String output, int width, int height) throws IOException {
        Thumbnails.of(source).size(width, height).toFile(output);
    }

    /**
     * 按照比例进行缩放
     * @param source 输入源
     * @param output 输出源
     * @param scale  比例
     * @throws IOException
     */
    public static void imgScale(String source, String output, double scale) throws IOException {
        Thumbnails.of(source).scale(scale).toFile(output);
    }

    /**
     * 生成图片缩略图，并返回缩略图文件
     * @param file 需要生成缩略图的图片
     * @param ouputFilePath 缩略图保存路径（包含名称）
     * @param width 缩略图宽度
     * @param height 缩略图高度
     * @return 缩略图文件
     */
    public static File getImgThumb(File file,String ouputFilePath,int width,int height){
        //如果图片大小已小于预期值，则直接返回
        if(ImageUtil.getImgWidth(file) <= width && ImageUtil.getImgHeight(file) <= height) {
            return file;
        }

        //如果输出的缩略图已经存在，则不再处理，直接返回
        File thumbFile = new File(ouputFilePath);
        if(thumbFile.isFile()) {
            return new File(ouputFilePath);
        }

        //String ouputFilePath="E:\\ftp_dir\\XX_"+file.getName()+".jpeg";
        try {
            imgThumb(file.getAbsolutePath(),ouputFilePath,width,height);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(ouputFilePath);
    }

    /**
     * 获取图片宽度
     * @param file  图片文件
     * @return 宽度
     */
    public static int getImgWidth(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int ret = -1;
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getWidth(null); // 得到源图宽
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    /**
     * 获取图片高度
     * @param file  图片文件
     * @return 高度
     */
    public static int getImgHeight(File file) {
        InputStream is = null;
        BufferedImage src = null;
        int ret = -1;
        try {
            is = new FileInputStream(file);
            src = javax.imageio.ImageIO.read(is);
            ret = src.getHeight(null); // 得到源图高
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 取图片的EXIF信息
     * @param file 图片文件
     */
    private static ImageInfoDto getImageExif(File file) {
        if(!FileUtil.isImage(file)) return null;
        ImageInfoDto imageInfoDto = new ImageInfoDto();
        try {
            Metadata metadata = JpegMetadataReader.readMetadata(file);
            for (Directory directory : metadata.getDirectories()){
                for (Tag tag : directory.getTags()){
                    System.out.print(tag.getTagName() + " --> ");
                    System.out.println(tag.getDescription());
                    switch (tag.getTagName()){
                        case "Make"://0x010f Make 厂商
                            imageInfoDto.setExifMake(tag.getDescription());
                            break;
                        case "Model"://0x0110 Model 机型
                            imageInfoDto.setExifModel(tag.getDescription());
                            break;
                        case "Date/Time"://0x0132 Date/Time 修改时间
                            imageInfoDto.setExifChangedDateTime(tag.getDescription());
                            break;
                        case "Date/Time Original":// 0x9003 Date/Time Original 拍摄时间
                            imageInfoDto.setExifOriginalDateTime(tag.getDescription());
                            break;
                        case "Date/Time Digitized":// 0x9004 Date/Time Digitized 数字化时间
                            imageInfoDto.setExifDigitizedDateTime(tag.getDescription());
                            break;
                        case "Exif Image Width"://0xa002 Exif Image Width Exif图像宽度
                            imageInfoDto.setExifImageWidth(tag.getDescription());
                            break;
                        case "Exif Image Height"://0xa003 Exif Image Height Exif图像高度
                            imageInfoDto.setExifImageHeight(tag.getDescription());
                            break;
                        case "GPS Latitude Ref"://GPS Latitude Ref 纬度南S/北N
                            imageInfoDto.setGpsLatitudeRef(tag.getDescription());
                            break;
                        case "GPS Latitude"://GPS Latitude 纬度
                            imageInfoDto.setGpsLatitude(tag.getDescription());
                            break;
                        case "GPS Longitude Ref"://GPS Longitude Ref 经度东E/西W
                            imageInfoDto.setGpsLongitudeRef(tag.getDescription());
                            break;
                        case "GPS Longitude"://GPS Longitude 经度
                            imageInfoDto.setGpsLongitude(tag.getDescription());
                            break;
                        case "File Name"://File Name 文件名
                            imageInfoDto.setFileFileName(tag.getDescription());
                            break;
                        case "File Size"://File Size文件大小
                            imageInfoDto.setFileFileSize(tag.getDescription());
                            break;
                        case "File Modified Date"://File Modified Date 文件修改时间
                            imageInfoDto.setFileFileModifiedDate(tag.getDescription());
                            break;
                    }
                }

                if (directory.hasErrors()){
                    for (String error : directory.getErrors()){
                        System.err.println("ERROR: " + error);
                    }
                }
            }
        } catch (JpegProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageInfoDto;
    }

    public static void main(String args[]){
        File file = new File("E:\\2017-05-07 171706.jpg");
        ImageInfoDto imageInfoDto = getImageExif(file);
        System.out.println(imageInfoDto);
    }
}


