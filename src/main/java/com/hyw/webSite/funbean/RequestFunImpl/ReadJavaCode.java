package com.hyw.webSite.funbean.RequestFunImpl;

import com.alibaba.fastjson.JSON;
import com.hyw.webSite.dbservice.DataService;
import com.hyw.webSite.exception.BizException;
import com.hyw.webSite.funbean.abs.RequestFunUnit;
import com.hyw.webSite.funbean.abs.RequestPubDto;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.FileUtil;
import com.hyw.webSite.utils.excel.ExcelTemplateUtil;
import com.hyw.webSite.utils.javaParse.*;
import com.hyw.webSite.utils.javaParse.dto.JavaClassFieldInfo;
import com.hyw.webSite.utils.javaParse.dto.JavaClassInfo;
import com.hyw.webSite.utils.javaParse.dto.JavaClassMethodInfo;
import com.hyw.webSite.utils.javaParse.dto.JavaClassMethodParamInfo;
import com.hyw.webSite.web.dto.RequestDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service("readJavaCode")
@Slf4j
public class ReadJavaCode extends RequestFunUnit<String, ReadJavaCode.QryVariable> {

    @Autowired
    private ExcelTemplateUtil excelTemplateUtil;

    @Autowired
    private DataService dataService;

    private static final String[] splitSignal = {""};

    /**
     * 输入参数检查
     * @param variable 参数
     */
    @Override
    public void checkVariable(QryVariable variable){
        //输入检查
        BizException.trueThrow(Objects.isNull(variable.getRootPath()),"导入文件不允许为空值!");
    }

    /**
     * 执行自定义逻辑
     * @param requestDto 请求dto
     * @param variable 参数
     * @return D
     */
    @Override
    public String execLogic(RequestDto requestDto, QryVariable variable) {
        String rootPath = variable.getRootPath();

        List<InterfaceInfo> interfaceInfoList = new ArrayList<>();
        File file = new File(rootPath);
        List<String> filePathList = FileUtil.getFilePathListWithType(file,"java");
        Map<String, JavaClassInfo> javaClassInfoMap = new HashMap<>();
        for(String javaFilePath:filePathList){
            JavaClassInfo javaClassInfo = JavaUtil.getJavaClassInfo(javaFilePath,null,null);
            String classPath = (javaClassInfo.getPackageStr()+"."+javaClassInfo.getName());
            javaClassInfoMap.put(classPath,javaClassInfo);
            if(javaClassInfo.getAnnotationList().contains("@Controller") ||
                    javaClassInfo.getAnnotationList().contains("@RestController")
            ){
                List<JavaClassMethodInfo> methodInfoList = javaClassInfo.getMethodInfoList();
                for(JavaClassMethodInfo javaClassMethodInfo:methodInfoList){
                    InterfaceInfo interfaceInfo = new InterfaceInfo();
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

                    List<JavaClassFieldInfo> fields = new ArrayList<>();
                    for(JavaClassMethodParamInfo argDef:javaClassMethodInfo.getInputParamList()){
                        JavaClassFieldInfo interfaceField = new JavaClassFieldInfo();
                        interfaceField.setArgString(null);

                        interfaceField.setFieldName(argDef.getName());
                        interfaceField.setFieldType(argDef.getType());

                        for(String importStr:javaClassInfo.getImportList()){
                            if(importStr.contains(interfaceField.getFieldType())){
                                interfaceField.setFieldType(importStr);
                                break;
                            }
                        }

                        fields.add(interfaceField);
                    }
                    interfaceInfo.setFields(fields);
//                    log.info(JSON.toJSONString(interfaceInfo));
                    interfaceInfoList.add(interfaceInfo);
                }
            }
        }

        for(InterfaceInfo interfaceInfo:interfaceInfoList){
            List<JavaClassFieldInfo> newJavaClassFieldInfoList = new ArrayList<>();
            for(JavaClassFieldInfo interfaceField:interfaceInfo.getFields()){
                String classPath = interfaceField.getFieldType();
                if(javaClassInfoMap.containsKey(classPath)){
                    List<JavaClassFieldInfo> fieldList = javaClassInfoMap.get(classPath).getJavaClassFieldInfoList();
                    newJavaClassFieldInfoList.addAll(fieldList);
                }
            }
            if(CollectionUtil.isNotEmpty(newJavaClassFieldInfoList)){
                interfaceInfo.setFields(newJavaClassFieldInfoList);
            }
        }

        log.info(JSON.toJSONString(interfaceInfoList));

        return "";
    }

    /**
     * 从双引号中取字符串
     * @param s 输入字符串
     */
    private String getStringFromQuotationMark(String s){
        int beg = s.indexOf("(");
        int end = s.indexOf(")",beg+1);
        return s.substring(beg+1,end).replaceAll("\"","");
    }

    /**
     * 输入输出参数
     */
    @Getter
    @Setter
    @Accessors(chain = true)
    public static class QryVariable extends RequestPubDto {
        private String rootPath;
    }
}

@Data
class InterfaceInfo{
    private String className;
    private String classPath;
    private String methodName;
    private String reqMethod;
    private String reqPath;
    private List<JavaClassFieldInfo> fields;
}
