package Frontend;

import AST.*;
import Parser.MxBaseVisitor;
import Parser.MxParser;
import Util.Type;
import Util.Position;
import org.antlr.v4.runtime.ParserRuleContext;
import AST.BinaryExprNode.BinaryOperator;
import AST.PrefixExprNode.PrefixOperator;
import AST.SuffixExprNode.SuffixOperator;

import java.util.ArrayList;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {


    @Override public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        ProgramNode root = new ProgramNode(new Position(ctx));
        for (MxParser.ProgramDeclContext tmp: ctx.programDecl())
            root.sectionList.add((ProgramDeclNode) visit(tmp));
        return root;
    }

    @Override public ASTNode visitProgramDecl(MxParser.ProgramDeclContext ctx) {
        if (ctx.varDecl() != null)
            return visit(ctx.varDecl());
        else if (ctx.funcDecl() != null)
            return visit(ctx.funcDecl());
        else if (ctx.classDecl() != null)
            return visit(ctx.classDecl());
        else return null;
    }

    @Override public ASTNode visitVarDecl(MxParser.VarDeclContext ctx) {
        TypeNode type = null;
        VarListNode varlist = null;
        if (ctx.type() != null)
            type = (TypeNode) visit(ctx.type());
        if (ctx.variablelist() != null)
            varlist = (VarListNode) visit(ctx.variablelist());
        return new VarDeclNode(type, varlist, new Position(ctx));
    }

    @Override public ASTNode visitVariablelist(MxParser.VariablelistContext ctx) {
        VarListNode varlist = new VarListNode(new Position(ctx));
        for (MxParser.VariableContext tmp: ctx.variable())
            varlist.Varlist.add((VarNode) visit(tmp));
        return varlist;
    }

    @Override public ASTNode visitVariable(MxParser.VariableContext ctx) {
        ExprNode init = null;
        String name = null;
        if (ctx.expression() != null)
            init = (ExprNode) visit(ctx.expression());
        else init = null;
        if (ctx.Identifier() != null)
            name = ctx.Identifier().getText();
        return new VarNode(name, null, init, new Position(ctx));
    }

    @Override public ASTNode visitFuncDecl(MxParser.FuncDeclContext ctx) {
        TypeNode type = null;
        String name = null;
        VarListNode parameterlist = null;
        BlockStmtNode suite = null;
        if (ctx.type() != null)
            type = (TypeNode) visit(ctx.type());
        if (ctx.Identifier() != null)
            name = ctx.Identifier().getText();
        if (ctx.parameterlist() != null)
            parameterlist = (VarListNode) visit(ctx.parameterlist());
        if (ctx.suite() != null)
            suite = (BlockStmtNode) visit(ctx.suite());
        return new FuncDeclNode(type, name, parameterlist, suite, new Position(ctx));
    }

    @Override public ASTNode visitClassDecl(MxParser.ClassDeclContext ctx) {
        String name = null;
        if (ctx.Identifier() != null)
            name = ctx.Identifier().getText();
        ClassDeclNode tmpclass = new ClassDeclNode(name, new Position(ctx));
        for (MxParser.VariablelistContext tmp: ctx.variablelist())
            tmpclass.Varlist.add((VarDeclNode) visit(tmp));
        for (MxParser.FuncDeclContext tmp: ctx.funcDecl())
            tmpclass.Funclist.add((FuncDeclNode) visit(tmp));
        for (MxParser.ConstructDeclContext tmp: ctx.constructDecl())
            tmpclass.Constructor.add((ConstructorDeclNode) visit(tmp));
        return tmpclass;
    }

    @Override public ASTNode visitConstructDecl(MxParser.ConstructDeclContext ctx) {
        String name = null;
        VarListNode parameterlist = null;
        BlockStmtNode suite = null;
        if (ctx.Identifier() != null)
            name = ctx.Identifier().getText();
        if (ctx.parameterlist() != null)
            parameterlist = (VarListNode) visit(ctx.parameterlist());
        if (ctx.suite() != null)
            suite = (BlockStmtNode) visit(ctx.suite());
        return new ConstructorDeclNode(name, parameterlist, suite, new Position(ctx));
    }

    @Override public ASTNode visitParameterlist(MxParser.ParameterlistContext ctx) {
        VarListNode parameterlist = new VarListNode(new Position(ctx));
        for (MxParser.ParameterContext tmp: ctx.parameter())
            parameterlist.Varlist.add((VarNode) visit(tmp));
        return parameterlist;
    }

    @Override public ASTNode visitParameter(MxParser.ParameterContext ctx) {
        TypeNode type = null;
        String name = null;
        if (ctx.type() != null)
            type = (TypeNode) visit(ctx.type());
        if (ctx.Identifier() != null)
            name = ctx.Identifier().getText();
        return new VarNode(name, type, null, new Position(ctx));
    }

    @Override public ASTNode visitType(MxParser.TypeContext ctx) {

    }
}
