package com.hyw.webSite.utils.javaParse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.type.Type;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.FileUtil;
import com.hyw.webSite.utils.StringUtil;
import com.hyw.webSite.utils.javaParse.dto.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

@Getter
public class ClassUnit extends VoidVisitorAdapter<Object> {
    Logger log  = LoggerFactory.getLogger(ClassUnit.class);

    private String classPath;
    private String projectRootPath;
    private List<JavaClassInfo> javaClassInfoList;
    private JavaClassInfo javaClassInfo = new JavaClassInfo();

    private boolean exist = false;

    public void visit(String classPath,String projectRootPath, List<JavaClassInfo> javaClassInfoList){
        this.classPath = classPath;
//        System.out.println(classPath);
        this.projectRootPath = projectRootPath;
        this.javaClassInfoList = javaClassInfoList;
        FileInputStream in = null;
        try {
            in = new FileInputStream(classPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(in == null) return;
        this.visit(StaticJavaParser.parse(in),null);
    }

    /**
     * 包名
     */
    @Override
    public void visit(PackageDeclaration n, Object arg) {
        javaClassInfo.setPackageStr(n.getName().toString());
        super.visit(n, arg);
    }

    /**
     * 支持包
     */
    @Override
    public void visit(ImportDeclaration n, Object arg) {
        String importClassName = n.toString()
                .replaceAll("\t","")
                .replaceAll("import ","")
                .replaceAll(";","")
                .trim();
        //对于*的内容需要提前加载
        if(importClassName.endsWith(".*")){
            boolean flag = false;
            for(JavaClassInfo javaClassInfo:this.javaClassInfoList){
                if(javaClassInfo.getPackageStr().startsWith(importClassName.replace(".*",""))){
                    javaClassInfo.getImportList().add(javaClassInfo.getFullName());
                    flag = true;
                }
            }

            if(!flag) {
                String importClassPath = this.projectRootPath + "\\" + importClassName.replace(".*", "").replaceAll("\\.", "\\\\").trim();
                File file = new File(importClassPath);
                List<String> filePathList = FileUtil.getFilePathListWithType(file, "java");
                for (String javaClassPath2 : filePathList) {
                    ClassUnit classUnit = new ClassUnit();
                    classUnit.visit(javaClassPath2, projectRootPath, javaClassInfoList);
                    JavaClassInfo javaClassInfo2 = classUnit.getJavaClassInfo();
                    if (!exist) javaClassInfoList.add(javaClassInfo2);
                    this.javaClassInfo.getImportList().add(javaClassInfo2.getFullName());
                }
            }
        }else {
            javaClassInfo.getImportList().add(importClassName);
        }

        super.visit(n, arg);
    }

    /**
     * 类
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        javaClassInfo.setName(n.getNameAsString());//类名
        javaClassInfo.setFullName(javaClassInfo.getPackageStr()+"."+javaClassInfo.getName());
        javaClassInfo.setArgList(null); //  arg

        //获取类的注解
        NodeList<AnnotationExpr> annotations = n.getAnnotations();
        //判断是否有注解
        if(annotations.size()>0) {
            for (AnnotationExpr annotation : annotations) {
                javaClassInfo.getAnnotationList().add(annotation.toString()); // 类注解
            }
        }
        //获取类的注释
        Optional<JavadocComment> javadocComment = n.getJavadocComment();
        javaClassInfo.setComment(getCommentContent(javadocComment)); // 类注释

        if(null != this.javaClassInfoList) {
            for (JavaClassInfo javaClassInfo : this.javaClassInfoList) {
                if (StringUtil.isNotBlank(javaClassInfo.getFullName()) &&
                        javaClassInfo.getFullName().equals(this.javaClassInfo.getFullName())) {
                    exist = true;
                    break;
                }
            }
        }
        if(!exist) super.visit(n, arg);
    }

    /**
     * 成员变量的注解
     */
    @Override
    public void visit(FieldDeclaration n, Object arg) {
//        System.out.println("成员变量的注解:位于第"+n.getRange().get().begin.line+"行");
        JavaClassFieldInfo javaClassFieldInfo = new JavaClassFieldInfo();
        javaClassFieldInfo.setArgString(n.toString());
        javaClassFieldInfo.setFieldName(n.getVariable(0).getNameAsString());
        javaClassFieldInfo.setFieldType(n.getVariable(0).getTypeAsString());
        javaClassFieldInfo.setFieldTypeFullName(getFullName(javaClassFieldInfo.getFieldType(),javaClassInfo));
        javaClassFieldInfo.setFieldInit(n.getVariable(0).getInitializer().isPresent()
                ?n.getVariable(0).getInitializer().get().toString()
                :null);
//        javaClassFieldInfo.setFieldAttr(n.getAnnotations().forEach(a->{a.getName()}););
        javaClassFieldInfo.setFieldDesc(getCommentContent(n.getComment()));
        javaClassInfo.getJavaClassFieldInfoList().add(javaClassFieldInfo);

        setFieldValue(javaClassInfo);
        super.visit(n, arg);
    }

    /**
     * 成员方法
     */
    @Override
    public void visit(MethodDeclaration n, Object arg) {
        JavaClassMethodInfo classMethodInfo = new JavaClassMethodInfo();
        classMethodInfo.setName(n.getNameAsString());//方法名

        //所有输入参数
        NodeList<Parameter> parameters = n.getParameters();
        for (Parameter parameter : parameters) {
            JavaClassMethodParamInfo javaClassMethodParamInfo = new JavaClassMethodParamInfo();
            javaClassMethodParamInfo.setName(parameter.getNameAsString());
            javaClassMethodParamInfo.setType(parameter.getTypeAsString());

            //type child nodes
            Type type = parameter.getType();
            List<Node> nodeList = type.getChildNodes();
            for(Node node:nodeList){
                String typeName = getCommentContent(node.getTokenRange());
                if(javaClassMethodParamInfo.getType().equals(typeName)) continue;
                JavaClassTypeInfo javaClassTypeInfo = new JavaClassTypeInfo();
                javaClassTypeInfo.setTypeName(typeName);
                javaClassTypeInfo.setTypeFullName(getFullName(typeName,javaClassInfo));
                javaClassMethodParamInfo.getSubTypeList().add(javaClassTypeInfo);
            }

            javaClassMethodParamInfo.setTypeFullName(getFullName(parameter.getTypeAsString(),javaClassInfo));
            List<String> annotations = new ArrayList<>();
            for(AnnotationExpr annotationExpr:parameter.getAnnotations()){
                annotations.add(annotationExpr.toString());
            }
            javaClassMethodParamInfo.setAnnotations(annotations);
            classMethodInfo.getInputParamList().add(javaClassMethodParamInfo);
        }

        //输出
        Type outputType = n.getType();
        JavaClassMethodParamInfo javaClassMethodParamInfo = new JavaClassMethodParamInfo();
        javaClassMethodParamInfo.setName(getCommentContent(outputType.getTokenRange()));
        javaClassMethodParamInfo.setType(getCommentContent(outputType.getTokenRange()));
        javaClassMethodParamInfo.setTypeFullName(getFullName(javaClassMethodParamInfo.getType(),javaClassInfo));
        List<Node> nodeList = outputType.getChildNodes();
        for(Node node:nodeList){
            String typeName = getCommentContent(node.getTokenRange());
            if(javaClassMethodParamInfo.getType().equals(typeName)) continue;
            JavaClassTypeInfo javaClassTypeInfo = new JavaClassTypeInfo();
            javaClassTypeInfo.setTypeName(typeName);
            javaClassTypeInfo.setTypeFullName(getFullName(typeName,javaClassInfo));
            javaClassMethodParamInfo.getSubTypeList().add(javaClassTypeInfo);
        }
        classMethodInfo.getOutputParamList().add(javaClassMethodParamInfo);

        //获取方法的注解
        NodeList<AnnotationExpr> annotations = n.getAnnotations();
        //判断是否有注解
        if(annotations.size()>0) {
            for (AnnotationExpr annotation : annotations) {
                classMethodInfo.getAnnotationList().add(annotation.toString());
            }
        }
        //获取方法的注释
        Optional<JavadocComment> javadocComment = n.getJavadocComment();
        classMethodInfo.setComment(getCommentContent(javadocComment));

        javaClassInfo.getMethodInfoList().add(classMethodInfo);
        super.visit(n, arg);
    }

    /**
     * 变量
     */
    @Override
    public void visit(VariableDeclarator n, Object arg) {
//        System.out.println("方法内定义的变量:"+n.toString());

        //bContentService  成员变量
        //bContentDO = bContentService.get(cid)  方法内部的变量
        super.visit(n, arg);
    }

    private String getCommentContent(Optional optional){
        String s = optional.isPresent()?optional.get().toString():null;
        return getCommentContent(s);
    }

    private String getCommentContent(String s){
        if(StringUtil.isBlank(s)) return null;
        s=s.replaceAll("/\\*\\*","").replaceAll("/\\*","").replaceAll("\\*/","");
        boolean lineBeg = false;
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<s.length();i++){
            if(i+2<=s.length() && "\r\n".equals(s.substring(i,i+2))){
                lineBeg = true;
                i++;
            }else if(i+1<=s.length() && " ".equals(s.substring(i,i+1)) && lineBeg){

            }else if(i+1<=s.length() && "*".equals(s.substring(i,i+1)) && lineBeg){

            }else{
                lineBeg = false;
                sb.append(s.substring(i,i+1));
            }
        }
        return sb.toString().trim();
    }

    private String getFullName(String name,JavaClassInfo javaClassInfo){
        List<String> importList = javaClassInfo.getImportList();
        if(CollectionUtil.isNotEmpty(importList)) {
            for (String imp : importList) {
                if (StringUtil.isNotBlank(imp) && imp.endsWith("."+name)) {
                    return imp;
                }
            }
        }
        return name;
    }

    private void setFieldValue(JavaClassInfo javaClassInfo){
        List<JavaClassFieldInfo> javaClassFieldInfoList = javaClassInfo.getJavaClassFieldInfoList();
        if(CollectionUtil.isEmpty(javaClassFieldInfoList)) return;
        for(JavaClassFieldInfo javaClassFieldInfo:javaClassFieldInfoList){

        }
    }
}
