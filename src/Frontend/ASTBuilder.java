package Frontend;

import AST.*;
import Parser.MxBaseVisitor;
import Parser.MxParser;
import Util.Type;
import Util.Position;
import Util.error.SemanticError;
import Util.error.SyntaxError;
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
        for (VarNode tmp : varlist.Varlist) {
            tmp.type = type;
        }
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
        if (ctx.varDecl() != null)
        for (MxParser.VarDeclContext tmp: ctx.varDecl())
            tmpclass.Varlist.add((VarDeclNode) visit(tmp));
        if (ctx.funcDecl() != null)
        for (MxParser.FuncDeclContext tmp: ctx.funcDecl())
            tmpclass.Funclist.add((FuncDeclNode) visit(tmp));
        if (ctx.constructDecl() != null)
        for (MxParser.ConstructDeclContext tmp: ctx.constructDecl())
            tmpclass.Constructor.add((ConstructorDeclNode) visit(tmp));
        if (tmpclass.Constructor.size() > 1)
            throw new SemanticError("redefine constructor", new Position(ctx));
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
        if (ctx.parameter() != null)
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
        if (ctx.arraytype() != null)
            return visit(ctx.arraytype());
        else if (ctx.noarraytype() != null)
            return visit(ctx.noarraytype());
        else if (ctx.Void() != null)
            return new TypeNode(new Position(ctx), "void", 0);
        else return null;
    }

    @Override public ASTNode visitArraytype(MxParser.ArraytypeContext ctx) {
        String typename = null;
        int dimension = 0;
        if (ctx.noarraytype() != null) {
            TypeNode type = (TypeNode) visit(ctx.noarraytype());
            typename = type.getTypename();
        }
        if (ctx.LeftBracket() != null)
            dimension = ctx.LeftBracket().size();
        return new TypeNode(new Position(ctx), typename, dimension);
    }

    @Override public ASTNode visitNoarraytype(MxParser.NoarraytypeContext ctx) {
        String typename = null;
        if (ctx.Int() != null) {
            typename = "int";
        }
        else if (ctx.Bool() != null) {
            typename = "Bool";
        }
        else if (ctx.String() != null) {
            typename = "string";
        }
        else if (ctx.Identifier() != null) {
            typename = ctx.Identifier().getText();
        }
        return new TypeNode(new Position(ctx), typename, 0);
    }

    @Override public ASTNode visitSuite(MxParser.SuiteContext ctx) {
        BlockStmtNode suite = new BlockStmtNode(new Position(ctx));
        if (ctx.statement() != null)
        for (MxParser.StatementContext tmp: ctx.statement())
            suite.stmts.add((StmtNode) visit(tmp));
        return suite;
    }

    @Override public ASTNode visitStatement(MxParser.StatementContext ctx) {
        if (ctx.suite() != null)
            return visit(ctx.suite());
        if (ctx.varDeclStmt() != null)
            return visit(ctx.varDeclStmt());
        if (ctx.ifStmt() != null)
            return visit(ctx.ifStmt());
        if (ctx.forStmt() != null)
            return visit(ctx.forStmt());
        if (ctx.whileStmt() != null)
            return visit(ctx.whileStmt());
        if (ctx.flowStmt() != null)
            return visit(ctx.flowStmt());
        if (ctx.expression() != null) {
            ExprNode expr = (ExprNode) visit(ctx.expression());
            return new ExprStmtNode(expr, new Position(ctx));
        }
        else return null;
    }

    @Override public ASTNode visitVarDeclStmt(MxParser.VarDeclStmtContext ctx) {
        TypeNode type = null;
        VarListNode varlist = null;
        if (ctx.varDecl() != null) {
            VarDeclNode varDecl = (VarDeclNode) visit(ctx.varDecl());
            type = varDecl.type;
            varlist = varDecl.varlist;
        }
        return new VarDeclStmtNode(type, varlist, new Position(ctx));
    }

    @Override public ASTNode visitIfStmt(MxParser.IfStmtContext ctx) {
        ExprNode condition = null;
        StmtNode truestmt = null;
        StmtNode falsestmt = null;
        if (ctx.expression() != null)
            condition = (ExprNode) visit(ctx.expression());
        if (ctx.trueStmt != null)
            truestmt = (StmtNode) visit(ctx.trueStmt);
        if (ctx.falseStmt != null)
            falsestmt = (StmtNode) visit(ctx.falseStmt);
        return new IfStmtNode(condition, truestmt, falsestmt, new Position(ctx));
    }

    @Override public ASTNode visitForStmt(MxParser.ForStmtContext ctx) {
        ExprNode init = null;
        ExprNode condition = null;
        ExprNode increase = null;
        StmtNode forstmt = null;
        if (ctx.init != null)
            init = (ExprNode) visit(ctx.init);
        if (ctx.condition != null)
            condition = (ExprNode) visit(ctx.condition);
        if (ctx.increase != null)
            increase = (ExprNode) visit(ctx.increase);
        if (ctx.statement() != null)
            forstmt = (StmtNode) visit(ctx.statement());
        return new ForStmtNode(init, condition, increase, forstmt, new Position(ctx));
    }

    @Override public ASTNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
        ExprNode condition = null;
        StmtNode whilestmt = null;
        if (ctx.expression() != null)
            condition = (ExprNode) visit(ctx.expression());
        if (ctx.statement() != null)
            whilestmt = (StmtNode) visit(ctx.statement());
        return new WhileStmtNode(condition, whilestmt, new Position(ctx));
    }

    @Override public ASTNode visitFlowStmt(MxParser.FlowStmtContext ctx) {
        ExprNode value = null;
        if (ctx.Break() != null)
            return new BreakStmtNode(new Position(ctx));
        else if (ctx.Continue() != null)
            return new ContinueStmtNode(new Position(ctx));
        else if (ctx.Return() != null) {
            if (ctx.expression() != null) {
                value = (ExprNode) visit(ctx.expression());
                return new ReturnStmtNode(value, new Position(ctx));
            }
            else return new ReturnStmtNode(null, new Position(ctx));
        }
        return null;
    }

    @Override public ASTNode visitAtomExpr(MxParser.AtomExprContext ctx) {
        return visit(ctx.primary());
    }

    @Override public ASTNode visitBinaryExpr(MxParser.BinaryExprContext ctx) {
        ExprNode lhs = null;
        ExprNode rhs = null;
        BinaryOperator op = null;
        if (ctx.lhs != null)
            lhs = (ExprNode) visit(ctx.lhs);
        if (ctx.rhs != null)
            rhs = (ExprNode) visit(ctx.rhs);
        if (ctx.op != null) {
            switch (ctx.op.getText()) {
                case "+" :
                    op = BinaryOperator.add;
                    break;
                case "-" :
                    op = BinaryOperator.sub;
                    break;
                case "*" :
                    op = BinaryOperator.mul;
                    break;
                case "/" :
                    op = BinaryOperator.div;
                    break;
                case "%" :
                    op = BinaryOperator.mod;
                    break;
                case ">" :
                    op = BinaryOperator.greater;
                    break;
                case "<" :
                    op = BinaryOperator.less;
                    break;
                case ">=" :
                    op = BinaryOperator.greatequal;
                    break;
                case "<=" :
                    op = BinaryOperator.lessequal;
                    break;
                case "&&" :
                    op = BinaryOperator.logicand;
                    break;
                case "||" :
                    op = BinaryOperator.logicor;
                    break;
                case "&" :
                    op = BinaryOperator.bitand;
                    break;
                case "|" :
                    op = BinaryOperator.bitor;
                    break;
                case "^" :
                    op = BinaryOperator.bitxor;
                    break;
                case ">>" :
                    op = BinaryOperator.rightshift;
                    break;
                case "<<" :
                    op = BinaryOperator.leftshift;
                    break;
                case "==" :
                    op = BinaryOperator.equal;
                    break;
                case "!=" :
                    op = BinaryOperator.notequal;
                    break;
                default:
                    op = null;
            }
        }
        return new BinaryExprNode(lhs, rhs, op, new Position(ctx));
    }

    @Override public ASTNode visitSuffixExpr(MxParser.SuffixExprContext ctx) {
        ExprNode lhs = null;
        SuffixOperator op = null;
        if (ctx.lhs != null)
            lhs = (ExprNode) visit(ctx.lhs);
        if (ctx.op != null) {
            switch (ctx.op.getText()) {
                case "++" :
                    op = SuffixOperator.suffixadd;
                    break;
                case "--" :
                    op = SuffixOperator.suffixsub;
                    break;
                default:
                    op = null;
            }
        }
        return new SuffixExprNode(lhs, op, new Position(ctx));
    }

    @Override public ASTNode visitPrefixExpr(MxParser.PrefixExprContext ctx) {
        ExprNode expr = null;
        PrefixOperator op = null;
        if (ctx.expression() != null)
            expr = (ExprNode) visit(ctx.expression());
        if (ctx.op != null) {
            switch (ctx.op.getText()) {
                case "++" :
                    op = PrefixOperator.prefixadd;
                    break;
                case "--" :
                    op = PrefixOperator.prefixsub;
                    break;
                case "+" :
                    op = PrefixOperator.positive;
                    break;
                case "-" :
                    op = PrefixOperator.negative;
                    break;
                case "!" :
                    op = PrefixOperator.logicnot;
                    break;
                case "~" :
                    op = PrefixOperator.bitnot;
                    break;
                default:
                    op = null;
            }
        }
        return new PrefixExprNode(expr, op, new Position(ctx));
    }

    @Override public ASTNode visitAssignExpr(MxParser.AssignExprContext ctx) {
        ExprNode lhs = null;
        ExprNode rhs = null;
        if (ctx.lhs != null)
            lhs = (ExprNode) visit(ctx.lhs);
        if (ctx.rhs != null)
            rhs = (ExprNode) visit(ctx.rhs);
        return new AssignExprNode(lhs, rhs, new Position(ctx));
    }

    @Override public ASTNode visitNewExpr(MxParser.NewExprContext ctx) {
        return visit(ctx.creator());
    }

    @Override public ASTNode visitMemberExpr(MxParser.MemberExprContext ctx) {
        ExprNode expr = null;
        String name = null;
        if (ctx.expression() != null)
            expr = (ExprNode) visit(ctx.expression());
        if (ctx.Identifier() != null)
            name = ctx.Identifier().getText();
        return new MemberExprNode(expr, name, new Position(ctx));
    }

    @Override public ASTNode visitMethodExpr(MxParser.MethodExprContext ctx) {
        ExprNode expr = null;
        String name = null;
        if (ctx.expression() != null)
            expr = (ExprNode) visit(ctx.expression());
        if (ctx.Identifier() != null)
            name = ctx.Identifier().getText();
        if (ctx.expressionlist() != null) {
            FuncCallExprNode func = (FuncCallExprNode) visit(ctx.expressionlist());
            return new MethodExprNode(expr, name, func.parameters, new Position(ctx));
        }
        else return new MethodExprNode(expr, name, new ArrayList<>(), new Position(ctx));
    }

    @Override public ASTNode visitFunccallExpr(MxParser.FunccallExprContext ctx) {
        String funcname = null;
        FuncCallExprNode funccall = null;
        if (ctx.Identifier() != null)
            funcname = ctx.Identifier().getText();
        if (ctx.expressionlist() != null) {
            funccall = (FuncCallExprNode) visit(ctx.expressionlist());
            funccall.funcname = funcname;
        }
        else
            funccall = new FuncCallExprNode(funcname, new ArrayList<>(), new Position(ctx));
        return funccall;
    }

    @Override public ASTNode visitArrayExpr(MxParser.ArrayExprContext ctx) {
        ExprNode name = null;
        ExprNode index = null;
        if (ctx.array != null)
            name = (ExprNode) visit(ctx.array);
        if (ctx.index != null)
            index = (ExprNode) visit(ctx.index);
        return new ArrayExprNode(name, index, new Position(ctx));
    }

    @Override public ASTNode visitExpressionlist(MxParser.ExpressionlistContext ctx) {
        ArrayList<ExprNode> parameters = new ArrayList<>();
        if (ctx.expression() != null)
        for (MxParser.ExpressionContext tmp: ctx.expression())
            parameters.add((ExprNode) visit(tmp));
        return new FuncCallExprNode(null, parameters, new Position(ctx));
    }

    @Override public ASTNode visitErrorCreator(MxParser.ErrorCreatorContext ctx) {
        throw new SyntaxError("ErrorCreator", new Position(ctx));
    }

    @Override public ASTNode visitArrayCreator(MxParser.ArrayCreatorContext ctx) {
        TypeNode type = null;
        int dimension = 0;
        if (ctx.noarraytype() != null)
            type = (TypeNode) visit(ctx.noarraytype());
        if (ctx.LeftBracket() != null)
            dimension = ctx.LeftBracket().size();
        NewExprNode array = new NewExprNode(type, dimension, new Position(ctx));
        if (ctx.expression() != null)
        for (MxParser.ExpressionContext tmp: ctx.expression())
            array.arraysize.add((ExprNode) visit(tmp));
        return array;
    }

    @Override public ASTNode visitClassCreator(MxParser.ClassCreatorContext ctx) {
        TypeNode type = null;
        if (ctx.noarraytype() != null)
            type = (TypeNode) visit(ctx.noarraytype());
        return new NewExprNode(type, 0, new Position(ctx));
    }

    @Override public ASTNode visitBasicCreator(MxParser.BasicCreatorContext ctx) {
        TypeNode type = null;
        if (ctx.noarraytype() != null)
            type = (TypeNode) visit(ctx.noarraytype());
        return new NewExprNode(type, 0, new Position(ctx));
    }

    @Override public ASTNode visitPrimary(MxParser.PrimaryContext ctx) {
        if (ctx.expression() != null)
            return visit(ctx.expression());
        else if (ctx.Identifier() != null)
            return new IdentifierExprNode(ctx.Identifier().getText(), new Position(ctx));
        else if (ctx.literal() != null)
            return visit(ctx.literal());
        else if (ctx.This() != null)
            return new ThisExprNode(new Position(ctx));
        else return null;
    }

    @Override public ASTNode visitLiteral(MxParser.LiteralContext ctx) {
        if (ctx.IntegerConstant() != null)
            return new IntLiteralNode(Integer.parseInt(ctx.IntegerConstant().getText()), new Position(ctx));
        else if (ctx.BoolConstant() != null)
            return new BoolLiteralNode(Boolean.parseBoolean(ctx.BoolConstant().getText()), new Position(ctx));
        else if (ctx.StringConstant() != null)
            return new StringLiteralNode(ctx.StringConstant().getText().substring(1, ctx.StringConstant().getText().length() - 1), new Position(ctx));
        else if (ctx.NullConstant() != null)
            return new NullLiteralNode(new Position(ctx));
        else return null;
    }

}
