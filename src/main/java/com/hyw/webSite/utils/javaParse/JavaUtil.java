package com.hyw.webSite.utils.javaParse;

import com.alibaba.fastjson.JSON;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.FileUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.utils.javaParse.dto.*;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavaUtil {

    public static List<JavaClassInfo> getJavaClassInfo(String projectRootPath){// .../src/main/java
        List<JavaClassInfo> javaClassInfoList = new ArrayList<>();

        File file = new File(projectRootPath);
        List<String> filePathList = FileUtil.getFilePathListWithType(file,"java");
        for(String javaClassPath:filePathList){
            javaClassInfoList.add(getJavaClassInfo(javaClassPath, projectRootPath, javaClassInfoList));
        }
        return javaClassInfoList;
    }


    public static JavaClassInfo getJavaClassInfo(String classPath,String projectRootPath, List<JavaClassInfo> javaClassInfoList){
        ClassUnit classUnit = new ClassUnit();
        classUnit.visit(classPath, projectRootPath, javaClassInfoList);
        return classUnit.getJavaClassInfo();
    }

    public static void main(String[] args){
        scanModel();
//        scanOnePgm();
//        scanProject();
    }

    private static void scanModel(){
        String projectRootPath = "D:\\ideaSpace\\DashufSource\\caes_test\\src\\main\\java\\com\\dashuf\\caes\\model";
        File file = new File(projectRootPath);
        List<String> filePathList = FileUtil.getFilePathListWithType(file,"java");
        for(String classPath:filePathList){
            ClassUnit classUnit = new ClassUnit();
            classUnit.visit(classPath,null,null);
            JavaClassInfo javaClassInfo = classUnit.getJavaClassInfo();

            System.out.println("DROP table if exists "+ StringUtil.camelCaseToUnderline(javaClassInfo.getName()) + ";");
            System.out.println("CREATE TABLE "+ StringUtil.camelCaseToUnderline(javaClassInfo.getName()) + " (");
            for(JavaClassFieldInfo fieldInfo:javaClassInfo.getJavaClassFieldInfoList()){
                if("serialVersionUID".equals(fieldInfo.getFieldName())) continue;
                System.out.print("  "+StringUtil.camelCaseToUnderline(fieldInfo.getFieldName()) + " ");
                if("String".equals(fieldInfo.getFieldType())){
                    System.out.print("varchar(32) ");
                }else if("LocalDate".equals(fieldInfo.getFieldType())){
                    System.out.print("date ");
                }else if("LocalDateTime".equals(fieldInfo.getFieldType())){
                    System.out.print("datetime ");
                }else if("long".equals(fieldInfo.getFieldType())){
                    System.out.print("int(11) ");
                }else if("BigDecimal".equals(fieldInfo.getFieldType())){
                    System.out.print("decimal(18,4) ");
                }

                System.out.println("COMMENT '"+fieldInfo.getFieldDesc() + "',");
            }
            System.out.println("  created_by varchar(64) DEFAULT 'System' COMMENT '创建人',\n" +
                    "  created_date datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                    "  updated_by varchar(64) DEFAULT 'System' COMMENT '更新人',\n" +
                    "  updated_date datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                    "  version_val int(11) DEFAULT 1 COMMENT '版本',\n" +
                    "  record_ind char(1) DEFAULT 'A' COMMENT '逻辑删除标志',");
            String keyName = javaClassInfo.getJavaClassFieldInfoList().get(1).getFieldName();
            System.out.println(" PRIMARY KEY ("+StringUtil.camelCaseToUnderline(keyName)+")");

            String comment = javaClassInfo.getComment();
            comment = comment.substring(0,comment.indexOf("@"));
            System.out.println(") COMMENT='"+comment+"';");
            System.out.println(" ");

        }
    }

    private static void scanOnePgm(){
//        String classPath = "D:\\Java\\DaShuSource\\caes_release_001\\src\\main\\java\\com\\dashuf\\caes\\web\\controller\\webapi\\ClaimController.java";
//        String classPath = "D:\\Java\\DaShuSource\\caes_release_001\\src\\main\\java\\com\\dashuf\\caes\\dto\\BackBillDto.java";
//        String classPath = "D:\\Java\\HywSource\\webSite\\src\\test\\java\\com\\hyw\\webSite\\TestDto.java";
//        String classPath = "E:\\ideaSpace\\DashufSource\\caes\\src\\main\\java\\com\\dashuf\\caes\\web\\Urls.java";
        String classPath = "D:\\ideaSpace\\DashufSource\\caes_test\\src\\main\\java\\com\\dashuf\\caes\\model\\ConfigBusinessDate.java";

        ClassUnit classUnit = new ClassUnit();
        classUnit.visit(classPath,null,null);
        JavaClassInfo javaClassInfo = classUnit.getJavaClassInfo();

        for(JavaClassFieldInfo fieldInfo:javaClassInfo.getJavaClassFieldInfoList()){
            if("serialVersionUID".equals(fieldInfo.getFieldName())) continue;
            System.out.print(javaClassInfo.getName() + "\t");
            System.out.print(fieldInfo.getFieldName() + "\t");
            System.out.print(fieldInfo.getFieldType() + "\t");
            System.out.println(fieldInfo.getFieldDesc() + "\t");
        }

//        System.out.println(JSON.toJSONString(javaClassInfo));
    }

    private static void scanProject(){
        List<JavaClassInfo> javaClassInfoList = getJavaClassInfo("D:\\ideaSpace\\DashufSource\\caes_test\\src\\main\\java\\com\\dashuf\\caes\\model");

        List<InterfaceInfo2> interfaceInfo2List = new ArrayList<>();
        for(JavaClassInfo javaClassInfo:javaClassInfoList){
            if(javaClassInfo.getAnnotationList().contains("@Controller") ||
                    javaClassInfo.getAnnotationList().contains("@RestController")
            ){
                List<JavaClassMethodInfo> methodInfoList = javaClassInfo.getMethodInfoList();
                for(JavaClassMethodInfo javaClassMethodInfo:methodInfoList){
                    InterfaceInfo2 interfaceInfo = new InterfaceInfo2();
                    interfaceInfo.setClassName(javaClassInfo.getName());
                    interfaceInfo.setClassPath((javaClassInfo.getPackageStr()+"."+javaClassInfo.getName()).replace(".","/"));
                    interfaceInfo.setMethodName(javaClassMethodInfo.getName());

                    //接口路径
                    String path = null;
                    String method = null;
                    for(String annotationStr:javaClassMethodInfo.getAnnotationList()){
                        if(annotationStr.indexOf("@PostMapping")>=0){
                            method = "post";
                            path = getStringFromQuotationMark(annotationStr);
                        }else if(annotationStr.indexOf("@GetMapping")>=0){
                            method = "get";
                            path = getStringFromQuotationMark(annotationStr);
                        }else if(annotationStr.indexOf("@RequestMapping")>=0){
                            method = "";
                            path = getStringFromQuotationMark(annotationStr);
                        }
                    }
                    interfaceInfo.setReqMethod(method);
                    interfaceInfo.setReqPath(path);

                    //接口输入参数
                    List<JavaClassFieldInfo> fields = new ArrayList<>();
                    for(JavaClassMethodParamInfo argDef:javaClassMethodInfo.getInputParamList()){
                        JavaClassFieldInfo interfaceField = new JavaClassFieldInfo();
                        interfaceField.setArgString(null);

                        interfaceField.setFieldName(argDef.getName());
                        interfaceField.setFieldType(argDef.getType());
                        interfaceField.setFieldTypeFullName(argDef.getTypeFullName());
                        interfaceField.setSubTypeList(argDef.getSubTypeList());
                        for(JavaClassTypeInfo javaClassTypeInfo:interfaceField.getSubTypeList()){
                            List<JavaClassFieldInfo> javaClassFieldInfoList = getDtoFields(javaClassTypeInfo.getTypeFullName(),javaClassInfoList);
                            if(CollectionUtil.isNotEmpty(javaClassFieldInfoList)){
                                javaClassTypeInfo.setTypeFieldList(javaClassFieldInfoList);
                            }
                        }

                        List<JavaClassFieldInfo> javaClassFieldInfoList = getDtoFields(interfaceField.getFieldTypeFullName(),javaClassInfoList);
                        if(CollectionUtil.isNotEmpty(javaClassFieldInfoList)){
                            fields.addAll(javaClassFieldInfoList);
                        }else {
                            fields.add(interfaceField);
                        }
                    }
                    interfaceInfo.setInputFields(fields);

                    //接口输出参数
                    List<JavaClassFieldInfo> outputFields = new ArrayList<>();
                    for(JavaClassMethodParamInfo argDef:javaClassMethodInfo.getOutputParamList()){
                        JavaClassFieldInfo interfaceField = new JavaClassFieldInfo();
                        interfaceField.setArgString(null);

                        interfaceField.setFieldName(argDef.getName());
                        interfaceField.setFieldType(argDef.getType());
                        interfaceField.setFieldTypeFullName(argDef.getTypeFullName());
                        interfaceField.setSubTypeList(argDef.getSubTypeList());

                        List<JavaClassFieldInfo> javaClassFieldInfoList = getDtoFields(interfaceField.getFieldTypeFullName(),javaClassInfoList);
                        if(CollectionUtil.isNotEmpty(javaClassFieldInfoList)){
                            outputFields.addAll(javaClassFieldInfoList);
                        }else {
                            outputFields.add(interfaceField);
                        }
                    }
                    interfaceInfo.setOutputFields(outputFields);

                    //接口输出参数

                    interfaceInfo2List.add(interfaceInfo);
                }
            }
        }

        for(InterfaceInfo2 interfaceInfo2:interfaceInfo2List){
            for(JavaClassFieldInfo javaClassFieldInfo:interfaceInfo2.getInputFields()){
                javaClassFieldInfo.setArgString(null);
            }
            for(JavaClassFieldInfo javaClassFieldInfo:interfaceInfo2.getOutputFields()){
                javaClassFieldInfo.setArgString(null);
            }
        }

        System.out.println(JSON.toJSONString(interfaceInfo2List));
    }


    private static List<JavaClassFieldInfo> getDtoFields(String dtoClassName,List<JavaClassInfo> javaClassInfoList){
        for(JavaClassInfo javaClassInfo:javaClassInfoList){
            if(dtoClassName.equals(javaClassInfo.getFullName())){
                return javaClassInfo.getJavaClassFieldInfoList();
            }
        }
        return new ArrayList<>();
    }

    /**
     * 从双引号中取字符串
     * @param s 输入字符串
     */
    private static String getStringFromQuotationMark(String s){
        int beg = s.indexOf("(");
        int end = s.indexOf(")",beg+1);
        return s.substring(beg+1,end).replaceAll("\"","");
    }
}

@Data
class InterfaceInfo2{
    private String className;
    private String classPath;
    private String methodName;
    private String reqMethod;
    private String reqPath;
    private List<JavaClassFieldInfo> inputFields;
    private List<JavaClassFieldInfo> outputFields;
}