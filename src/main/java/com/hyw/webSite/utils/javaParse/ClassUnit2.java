package com.hyw.webSite.utils.javaParse;

import com.github.javaparser.ast.*;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.modules.*;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ClassUnit2 extends VoidVisitorAdapter<Object> {
    Logger log  = LoggerFactory.getLogger(ClassUnit.class);

    private JavaClassCode2 javaClassInfo = new JavaClassCode2();

    //=========================body====================================

    @Override
    public void visit(AnnotationDeclaration n, Object arg) {
        System.out.println("AnnotationDeclaration" +"============="+ n.toString());
        super.visit(n, arg);
    }

    @Override
    public void visit(AnnotationMemberDeclaration n, Object arg) {
        System.out.println("AnnotationMemberDeclaration" +"============="+ n.toString());
        super.visit(n, arg);

    }

    /**
     *
     * @param n
     * @param arg
     */
    @Override
    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
        javaClassInfo.setClassOrInterfaceDeclaration(n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ConstructorDeclaration n, Object arg) {
        System.out.println("ConstructorDeclaration" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(EnumConstantDeclaration n, Object arg) {
        System.out.println("EnumConstantDeclaration" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(EnumDeclaration n, Object arg) {
        System.out.println("EnumDeclaration" +"============="+ n.toString());
        super.visit(n, arg);

    }

    /**
     * 类属性定义
     * @param n
     * @param arg
     */
    @Override
    public void visit(FieldDeclaration n, Object arg) {
//        System.out.println("FieldDeclaration" +"============="+ n.toString());
        //FieldDeclaration=============@Autowired
        //private ClaimService claimService;
        //Modifier=============private
        //VariableDeclarator=============claimService
        //SimpleName=============claimService
        //ClassOrInterfaceType=============ClaimService
        //SimpleName=============ClaimService
        //MarkerAnnotationExpr=============@Autowired
        javaClassInfo.getFieldDeclarationList().add(n);
        super.visit(n, arg);
    }

    @Override
    public void visit(InitializerDeclaration n, Object arg) {
        System.out.println("InitializerDeclaration" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(MethodDeclaration n, Object arg) {
        System.out.println("MethodDeclaration" +"============="+ n.toString());
        super.visit(n, arg);
    }

    @Override
    public void visit(Parameter n, Object arg) {
        System.out.println("Parameter" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(VariableDeclarator n, Object arg) {
        System.out.println("VariableDeclarator" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ReceiverParameter n, Object arg) {
        System.out.println("ReceiverParameter" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(RecordDeclaration n, Object arg) {
        System.out.println("RecordDeclaration" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(CompactConstructorDeclaration n, Object arg) {
        System.out.println("CompactConstructorDeclaration" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ModuleDeclaration n, Object arg) {
        System.out.println("ModuleDeclaration" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ModuleRequiresDirective n, Object arg) {
        System.out.println("ModuleRequiresDirective" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ModuleExportsDirective n, Object arg) {
        System.out.println("ModuleExportsDirective" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ModuleProvidesDirective n, Object arg) {
        System.out.println("ModuleProvidesDirective" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ModuleUsesDirective n, Object arg) {
        System.out.println("ModuleUsesDirective" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ModuleOpensDirective n, Object arg) {
        System.out.println("ModuleOpensDirective" +"============="+ n.toString());
        super.visit(n, arg);

    }

    /**
     * CompilationUnit 全代码
     * @param n
     * @param arg
     */
    @Override
    public void visit(CompilationUnit n, Object arg) {
//        System.out.println("CompilationUnit" +"============="+ n.toString());
        super.visit(n, arg);
    }

    /**
     * Package
     * @param n
     * @param arg
     */
    @Override
    public void visit(PackageDeclaration n, Object arg) {
        javaClassInfo.setPackageDeclaration(n);
        super.visit(n, arg);
    }

    @Override
    public void visit(ArrayCreationLevel n, Object arg) {
        System.out.println("ArrayCreationLevel" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(NodeList n, Object arg) {
        System.out.println("NodeList" +"============="+ n.toString());
        super.visit(n, arg);

    }

    /**
     * import
     * @param n
     * @param arg
     */
    @Override
    public void visit(ImportDeclaration n, Object arg) {
        javaClassInfo.getImportDeclarationList().add(n);
        super.visit(n, arg);
    }

    @Override
    public void visit(Modifier n, Object arg) {
        System.out.println("Modifier" +"============="+ n.toString());
        super.visit(n, arg);
    }

    @Override
    public void visit(ArrayAccessExpr n, Object arg) {
        System.out.println("ArrayAccessExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ArrayCreationExpr n, Object arg) {
        System.out.println("ArrayCreationExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ArrayInitializerExpr n, Object arg) {
        System.out.println("ArrayInitializerExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(AssignExpr n, Object arg) {
        System.out.println("AssignExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(BinaryExpr n, Object arg) {
        System.out.println("BinaryExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(BooleanLiteralExpr n, Object arg) {
        System.out.println("BooleanLiteralExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(CastExpr n, Object arg) {
        System.out.println("CastExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(CharLiteralExpr n, Object arg) {
        System.out.println("CharLiteralExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ClassExpr n, Object arg) {
        System.out.println("ClassExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ConditionalExpr n, Object arg) {
        System.out.println("ConditionalExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(DoubleLiteralExpr n, Object arg) {
        System.out.println("DoubleLiteralExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(EnclosedExpr n, Object arg) {
        System.out.println("EnclosedExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(FieldAccessExpr n, Object arg) {
        System.out.println("FieldAccessExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(InstanceOfExpr n, Object arg) {
        System.out.println("InstanceOfExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(IntegerLiteralExpr n, Object arg) {
        System.out.println("IntegerLiteralExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(LongLiteralExpr n, Object arg) {
        System.out.println("LongLiteralExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(MarkerAnnotationExpr n, Object arg) {
        System.out.println("MarkerAnnotationExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(MemberValuePair n, Object arg) {
        System.out.println("MemberValuePair" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(MethodCallExpr n, Object arg) {
        System.out.println("MethodCallExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(NameExpr n, Object arg) {
        System.out.println("NameExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(NormalAnnotationExpr n, Object arg) {
        System.out.println("NormalAnnotationExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(NullLiteralExpr n, Object arg) {
        System.out.println("NullLiteralExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ObjectCreationExpr n, Object arg) {
        System.out.println("ObjectCreationExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    /**
     * 按java分隔符拆分得到的名词
     * @param n
     * @param arg
     */
    @Override
    public void visit(Name n, Object arg) {
//        System.out.println("Name" +"============="+ n.toString());
        super.visit(n, arg);
    }

    @Override
    public void visit(SimpleName n, Object arg) {
//        System.out.println("SimpleName" +"============="+ n.toString());
        super.visit(n, arg);
    }

    @Override
    public void visit(SingleMemberAnnotationExpr n, Object arg) {
        System.out.println("SingleMemberAnnotationExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(StringLiteralExpr n, Object arg) {
        System.out.println("StringLiteralExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(SuperExpr n, Object arg) {
        System.out.println("SuperExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ThisExpr n, Object arg) {
        System.out.println("ThisExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(UnaryExpr n, Object arg) {
        System.out.println("UnaryExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(VariableDeclarationExpr n, Object arg) {
        System.out.println("VariableDeclarationExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(LambdaExpr n, Object arg) {
        System.out.println("LambdaExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(MethodReferenceExpr n, Object arg) {
        System.out.println("MethodReferenceExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(TypeExpr n, Object arg) {
        System.out.println("TypeExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(SwitchExpr n, Object arg) {
        System.out.println("SwitchExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(TextBlockLiteralExpr n, Object arg) {
        System.out.println("TextBlockLiteralExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(PatternExpr n, Object arg) {
        System.out.println("PatternExpr" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(AssertStmt n, Object arg) {
        System.out.println("AssertStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(BlockStmt n, Object arg) {
        System.out.println("BlockStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(BreakStmt n, Object arg) {
        System.out.println("BreakStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(CatchClause n, Object arg) {
        System.out.println("CatchClause" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ContinueStmt n, Object arg) {
        System.out.println("ContinueStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(DoStmt n, Object arg) {
        System.out.println("DoStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(EmptyStmt n, Object arg) {
        System.out.println("EmptyStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ExplicitConstructorInvocationStmt n, Object arg) {
        System.out.println("ExplicitConstructorInvocationStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ExpressionStmt n, Object arg) {
        System.out.println("ExpressionStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ForEachStmt n, Object arg) {
        System.out.println("ForEachStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ForStmt n, Object arg) {
        System.out.println("ForStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(IfStmt n, Object arg) {
        System.out.println("IfStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(LabeledStmt n, Object arg) {
        System.out.println("LabeledStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ReturnStmt n, Object arg) {
        System.out.println("ReturnStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(SwitchEntry n, Object arg) {
        System.out.println("SwitchEntry" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(SwitchStmt n, Object arg) {
        System.out.println("SwitchStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(SynchronizedStmt n, Object arg) {
        System.out.println("SynchronizedStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ThrowStmt n, Object arg) {
        System.out.println("ThrowStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(TryStmt n, Object arg) {
        System.out.println("TryStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(LocalClassDeclarationStmt n, Object arg) {
        System.out.println("LocalClassDeclarationStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(LocalRecordDeclarationStmt n, Object arg) {
        System.out.println("LocalRecordDeclarationStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(WhileStmt n, Object arg) {
        System.out.println("WhileStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(UnparsableStmt n, Object arg) {
        System.out.println("UnparsableStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(YieldStmt n, Object arg) {
        System.out.println("YieldStmt" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ClassOrInterfaceType n, Object arg) {
        System.out.println("ClassOrInterfaceType" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(PrimitiveType n, Object arg) {
        System.out.println("PrimitiveType" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(ArrayType n, Object arg) {
        System.out.println("ArrayType" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(IntersectionType n, Object arg) {
        System.out.println("IntersectionType" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(UnionType n, Object arg) {
        System.out.println("UnionType" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(TypeParameter n, Object arg) {
        System.out.println("TypeParameter" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(UnknownType n, Object arg) {
        System.out.println("UnknownType" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(VoidType n, Object arg) {
        System.out.println("VoidType" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(WildcardType n, Object arg) {
        System.out.println("WildcardType" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(VarType n, Object arg) {
        System.out.println("VarType" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(BlockComment n, Object arg) {
        System.out.println("BlockComment" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(JavadocComment n, Object arg) {
        System.out.println("JavadocComment" +"============="+ n.toString());
        super.visit(n, arg);

    }

    @Override
    public void visit(LineComment n, Object arg) {
        System.out.println("LineComment" +"============="+ n.toString());
        super.visit(n, arg);

    }



}

@Data
class JavaClassCode2{
    //========Body=======================================
    AnnotationDeclaration annotationDeclaration; 
    AnnotationMemberDeclaration annotationMemberDeclaration; 
    ClassOrInterfaceDeclaration classOrInterfaceDeclaration; 
    ConstructorDeclaration constructorDeclaration; 
    EnumConstantDeclaration enumConstantDeclaration; 
    EnumDeclaration enumDeclaration; 
    List<FieldDeclaration> fieldDeclarationList = new ArrayList<>();
    InitializerDeclaration initializerDeclaration; 
    MethodDeclaration methodDeclaration; 
    Parameter parameter; 
    VariableDeclarator variableDeclarator;
    ReceiverParameter receiverParameter;
    RecordDeclaration recordDeclaration;
    CompactConstructorDeclaration compactConstructorDeclaration;
//========Module=======================================
    ModuleDeclaration moduleDeclaration;
    ModuleRequiresDirective moduleRequiresDirective;
    ModuleExportsDirective moduleExportsDirective;
    ModuleProvidesDirective moduleProvidesDirective;
    ModuleUsesDirective moduleUsesDirective;
    ModuleOpensDirective moduleOpensDirective;
//=====Ast==========================================
    CompilationUnit compilationUnit;
    PackageDeclaration packageDeclaration;
    ArrayCreationLevel arrayCreationLevel;
    NodeList nodeList;
    List<ImportDeclaration> importDeclarationList = new ArrayList<>();
    Modifier modifier;
//======Expr=========================================
    ArrayAccessExpr arrayAccessExpr;
    ArrayCreationExpr arrayCreationExpr;
    ArrayInitializerExpr arrayInitializerExpr;
    AssignExpr assignExpr;
    BinaryExpr binaryExpr;
    BooleanLiteralExpr booleanLiteralExpr;
    CastExpr castExpr;
    CharLiteralExpr charLiteralExpr;
    ClassExpr classExpr;
    ConditionalExpr conditionalExpr;
    DoubleLiteralExpr doubleLiteralExpr;
    EnclosedExpr enclosedExpr;
    FieldAccessExpr fieldAccessExpr;
    InstanceOfExpr instanceOfExpr;
    IntegerLiteralExpr integerLiteralExpr;
    LongLiteralExpr longLiteralExpr;
    MarkerAnnotationExpr markerAnnotationExpr;
    MemberValuePair memberValuePair;
    MethodCallExpr methodCallExpr;
    NameExpr nameExpr;
    NormalAnnotationExpr normalAnnotationExpr;
    NullLiteralExpr nullLiteralExpr;
    ObjectCreationExpr objectCreationExpr;
    Name name;
    SimpleName simpleName;
    SingleMemberAnnotationExpr singleMemberAnnotationExpr;
    StringLiteralExpr stringLiteralExpr;
    SuperExpr superExpr;
    ThisExpr thisExpr;
    UnaryExpr unaryExpr;
    VariableDeclarationExpr variableDeclarationExpr;
    LambdaExpr lambdaExpr;
    MethodReferenceExpr methodReferenceExpr;
    TypeExpr typeExpr;
    SwitchExpr switchExpr;
    TextBlockLiteralExpr textBlockLiteralExpr;
    PatternExpr patternExpr;
//=======Stmt========================================
    AssertStmt assertStmt;
    BlockStmt blockStmt;
    BreakStmt breakStmt;
    CatchClause catchClause;
    ContinueStmt continueStmt;
    DoStmt doStmt;
    EmptyStmt emptyStmt;
    ExplicitConstructorInvocationStmt explicitConstructorInvocationStmt;
    ExpressionStmt expressionStmt;
    ForEachStmt forEachStmt;
    ForStmt forStmt;
    IfStmt ifStmt;
    LabeledStmt labeledStmt;
    ReturnStmt returnStmt;
    SwitchEntry switchEntry;
    SwitchStmt switchStmt;
    SynchronizedStmt synchronizedStmt;
    ThrowStmt throwStmt;
    TryStmt tryStmt;
    LocalClassDeclarationStmt localClassDeclarationStmt;
    LocalRecordDeclarationStmt localRecordDeclarationStmt;
    WhileStmt whileStmt;
    UnparsableStmt unparsableStmt;
    YieldStmt yieldStmt;
//=======Type========================================
    ClassOrInterfaceType classOrInterfaceType;
    PrimitiveType primitiveType;
    ArrayType arrayType;
    IntersectionType intersectionType;
    UnionType unionType;
    TypeParameter typeParameter;
    UnknownType unknownType;
    VoidType voidType;
    WildcardType wildcardType;
    VarType varType;
//=======Comment========================================
    BlockComment blockComment;
    JavadocComment javadocComment;
    LineComment lineComment;
}

class FieldInfo{
    private String modifier; // Modifier
    private String name; // VariableDeclarator
    private String type; // ClassOrInterfaceType
    private String normalAnnotationExpr; // NormalAnnotationExpr
}
