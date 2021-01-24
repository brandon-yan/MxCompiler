package Frontend;

import AST.*;
import Util.Type;
import Util.entity.FunctionEntity;
import Util.entity.VariableEntity;
import Util.error.SemanticError;
import Util.scope.*;

import java.util.ArrayList;
import java.util.HashMap;

public class SymbolCollector implements ASTVisitor {
    public GlobalScope gScope;
    public Scope currentScope = null;
    public SymbolCollector(GlobalScope gScope) {
        this.gScope = gScope;
    }
    @Override
    public void visit(ProgramNode it) {
        currentScope = gScope;
        it.sectionList.forEach(section -> section.accept(this));
    }

    @Override public void visit(TypeNode it) {}
    @Override public void visit(VarDeclNode it) {}
    @Override public void visit(VarListNode it) {}
    @Override public void visit(VarNode it) {}
    @Override public void visit(FuncDeclNode it) {
        ArrayList<VariableEntity> parameters = new ArrayList<>();
        if (it.parameterlist != null)
        for (VarNode tmp : it.parameterlist.Varlist) {
            parameters.add(new VariableEntity(tmp.name, tmp.type, tmp.init));
        }
        FunctionEntity func = new FunctionEntity(it.identifier, it.type, parameters, it.suite);
        if (!gScope.containsType(it.type.typename))
            throw new SemanticError("undefined type", it.pos);
        if (currentScope == gScope && gScope.containsType(it.identifier))
            throw new SemanticError("funcname error", it.pos);
        currentScope.defineFunction(func, it.pos);
    }
    @Override public void visit(ClassDeclNode it) {
        if (currentScope != gScope)
            throw new SemanticError("local class error", it.pos);
        if (gScope.containsVariable(it.identifier, false) || gScope.containsFunction(it.identifier, false))
            throw new SemanticError("classname error", it.pos);
        Type myclass = new Type(it.identifier, 0);
        ClassScope classscope = new ClassScope(currentScope, it.identifier);
        currentScope = classscope;
        it.Varlist.forEach(tmp -> tmp.accept(this));
        it.Funclist.forEach(tmp -> tmp.accept(this));
        myclass.setClassScope(classscope);
        currentScope = currentScope.parentScope;
        gScope.defineClass(it.identifier, myclass, it.pos);
    }
    @Override public void visit(ConstructorDeclNode it) {}

    @Override public void visit(BlockStmtNode it) {}
    @Override public void visit(ExprStmtNode it) {}
    @Override public void visit(IfStmtNode it) {}
    @Override public void visit(ForStmtNode it) {}
    @Override public void visit(WhileStmtNode it) {}
    @Override public void visit(BreakStmtNode it) {}
    @Override public void visit(ContinueStmtNode it) {}
    @Override public void visit(ReturnStmtNode it) {}

    @Override public void visit(AssignExprNode it) {}
    @Override public void visit(ArrayExprNode it) {}
    @Override public void visit(BinaryExprNode it) {}
    @Override public void visit(FuncCallExprNode it) {}
    @Override public void visit(IdentifierExprNode it) {}
    @Override public void visit(MemberExprNode it) {}
    @Override public void visit(MethodExprNode it) {}
    @Override public void visit(NewExprNode it) {}
    @Override public void visit(PrefixExprNode it) {}
    @Override public void visit(SuffixExprNode it) {}
    @Override public void visit(ThisExprNode it) {}

    @Override public void visit(IntLiteralNode it) {}
    @Override public void visit(BoolLiteralNode it) {}
    @Override public void visit(StringLiteralNode it) {}
    @Override public void visit(NullLiteralNode it) {}

    @Override public void visit(VarDeclStmtNode it) {}
}