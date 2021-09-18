package com.hyw.webSite.utils.javaParse;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.api.R;
import com.github.javaparser.StaticJavaParser;
import com.hyw.webSite.funbean.RequestFunImpl.ReadJavaCode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class JavaUtil {

    public static JavaClassInfo getJavaClassInfo(String classPath){
        FileInputStream in = null;
        try {
            in = new FileInputStream(classPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(in == null) return null;
        ClassUnit classUnit = new ClassUnit();
        classUnit.visit(StaticJavaParser.parse(in),null);
        return classUnit.getJavaClassInfo();
    }

    public static void main(String[] args){
//        ReadJavaCode readJavaCode = new ReadJavaCode();
//        ReadJavaCode.QryVariable var = new ReadJavaCode.QryVariable();
////        var.setRootPath("D:\\Java\\DaShuSource\\caes_develop_001\\src\\main\\java\\com\\dashuf\\caes");
//        var.setRootPath("D:\\Java\\DaShuSource\\caes_release_001\\src\\main\\java\\com\\dashuf\\caes\\web\\controller\\webapi\\ClaimController.java");
////        readJavaCode.execLogic(null,var);
        String classPath = "D:\\Java\\DaShuSource\\caes_release_001\\src\\main\\java\\com\\dashuf\\caes\\web\\controller\\webapi\\ClaimController.java";
//        String classPath = "D:\\Java\\DaShuSource\\caes_release_001\\src\\main\\java\\com\\dashuf\\caes\\dto\\BackBillDto.java";
//        String classPath = "D:\\Java\\HywSource\\webSite\\src\\test\\java\\com\\hyw\\webSite\\TestDto.java";

        FileInputStream in = null;
        try {
            in = new FileInputStream(classPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(in == null) return;
//        ClassUnit2 classUnit = new ClassUnit2();
        ClassUnit classUnit = new ClassUnit();
        classUnit.visit(StaticJavaParser.parse(in),null);

        JavaClassInfo javaClassInfo = classUnit.getJavaClassInfo();

        System.out.println(JSON.toJSONString(javaClassInfo));
    }
}
