package com.hyw.webSite.utils.javaParse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.body.*;
import com.hyw.webSite.utils.CollectionUtil;
import com.hyw.webSite.utils.StringUtil;
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

    private JavaClassInfo javaClassInfo = new JavaClassInfo();

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
        javaClassInfo.getImportList().add(n.toString()
                .replaceAll("\t","")
                .replaceAll("import ","")
                .replaceAll(";","")
                .trim());
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
        super.visit(n, arg);
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
        super.visit(n, arg);
    }

    /**
     * 成员方法
     */
    @Override
    public void visit(MethodDeclaration n, Object arg) {
        JavaClassMethodInfo classMethodInfo = new JavaClassMethodInfo();
        classMethodInfo.setName(n.getNameAsString());//方法名

        //所有参数
        NodeList<Parameter> parameters = n.getParameters();
        for (Parameter parameter : parameters) {
            JavaClassMethodParamInfo javaClassMethodParamInfo = new JavaClassMethodParamInfo();
            javaClassMethodParamInfo.setName(parameter.getNameAsString());
            javaClassMethodParamInfo.setType(parameter.getTypeAsString());
            javaClassMethodParamInfo.setTypeFullName(getFullName(parameter.getTypeAsString(),javaClassInfo));
            List<String> annotations = new ArrayList<>();
            for(AnnotationExpr annotationExpr:parameter.getAnnotations()){
                annotations.add(annotationExpr.toString());
            }
            javaClassMethodParamInfo.setAnnotations(annotations);
            classMethodInfo.getParamList().add(javaClassMethodParamInfo);
        }
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
                if (imp.endsWith(name)) {
                    return imp;
                }
            }
        }
        return name;
    }
}
