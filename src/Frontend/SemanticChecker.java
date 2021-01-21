package Frontend;

import AST.*;
import Util.Type;
import Util.entity.FunctionEntity;
import Util.entity.VariableEntity;
import Util.scope.*;
import Util.error.SemanticError;

import java.util.ArrayList;

public class SemanticChecker implements ASTVisitor {
    public Scope currentScope;
    public GlobalScope gScope;

    public SemanticChecker(GlobalScope gScope) {
        this.currentScope = gScope;
        this.gScope = gScope;
    }
    @Override
    public void visit(ProgramNode it) {
        currentScope = gScope;
        it.sectionList.forEach(section -> section.accept(this));
        if (gScope.containsFunction("main", false) == false)
            throw new SemanticError("lack main", it.pos);
    }

    @Override
    public void visit(TypeNode it) {}

    @Override
    public void visit(VarDeclNode it) {
        it.type.accept(this);
        it.varlist.accept(this);
    }

    @Override
    public void visit(VarListNode it) {
        it.Varlist.forEach(varNode -> varNode.accept(this));
    }

    @Override
    public void visit(VarNode it) {
    }

    @Override public void visit(FuncDeclNode it) {
    };
    @Override public void visit(ClassDeclNode it) {};
    @Override public void visit(ConstructorDeclNode it) {};

    @Override public void visit(BlockStmtNode it) {};
    @Override public void visit(ExprStmtNode it) {};
    @Override public void visit(IfStmtNode it) {};
    @Override public void visit(ForStmtNode it) {};
    @Override public void visit(WhileStmtNode it) {};
    @Override public void visit(BreakStmtNode it) {};
    @Override public void visit(ContinueStmtNode it) {};
    @Override public void visit(ReturnStmtNode it) {};

    @Override public void visit(AssignExprNode it) {};
    @Override public void visit(ArrayExprNode it) {};
    @Override public void visit(BinaryExprNode it) {};
    @Override public void visit(FuncCallExprNode it) {};
    @Override public void visit(IdentifierExprNode it) {};
    @Override public void visit(MemberExprNode it) {};
    @Override public void visit(NewExprNode it) {};
    @Override public void visit(PrefixExprNode it) {};
    @Override public void visit(SuffixExprNode it) {};
    @Override public void visit(ThisExprNode it) {};

    @Override public void visit(IntLiteralNode it) {};
    @Override public void visit(BoolLiteralNode it) {};
    @Override public void visit(StringLiteralNode it) {};
    @Override public void visit(NullLiteralNode it) {};
}
