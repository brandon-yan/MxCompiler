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
    public Scope currentScope;
    public Type currentClass;
    public SymbolCollector(GlobalScope gScope) {
        this.gScope = gScope;
        this.currentScope = null;
        this.currentClass = null;
    }
    @Override
    public void visit(ProgramNode it) {
        currentScope = gScope;
        it.sectionList.forEach(section -> section.accept(this));
    }

    @Override public void visit(TypeNode it) {}
    @Override public void visit(VarDeclNode it) {
        it.varlist.accept(this);
    }
    @Override public void visit(VarListNode it) {
        it.Varlist.forEach(node -> node.accept(this));
    }
    @Override public void visit(VarNode it) {
        if (currentClass != null) {
            if (gScope.containsType(it.type.typename))
                it.type.type = gScope.getType(it.type.typename);
            VariableEntity member = new VariableEntity(it.name, it.type, it.init);
            currentClass.addMembers(member);
        }
    }
    @Override public void visit(FuncDeclNode it) {
        ArrayList<VariableEntity> parameters = new ArrayList<>();
        if (it.parameterlist != null)
        for (VarNode tmp : it.parameterlist.Varlist) {
            if (gScope.containsType(tmp.type.typename))
                tmp.type.type = gScope.getType(tmp.type.typename);
            else throw new SemanticError("undefined type of parameter", it.pos);
            parameters.add(new VariableEntity(tmp.name, tmp.type, tmp.init));
        }
        if (gScope.containsType(it.type.typename))
            it.type.type = gScope.getType(it.type.typename);
        else throw new SemanticError("undefined type of function", it.pos);
        FunctionEntity func = new FunctionEntity(it.identifier, it.type, parameters, it.suite);
        if (currentScope == gScope && gScope.containsType(it.identifier))
            throw new SemanticError("funcname error", it.pos);
        currentScope.defineFunction(func, it.pos);
        if (currentClass != null)
            currentClass.addMethods(func);
    }
    @Override public void visit(ClassDeclNode it) {
        if (gScope.containsVariable(it.identifier, false) || gScope.containsFunction(it.identifier, false))
            throw new SemanticError("classname error", it.pos);
        currentClass = gScope.getType(it.identifier);
        currentScope = new ClassScope(currentScope, it.identifier);
        if (it.Varlist != null)
            it.Varlist.forEach(node -> node.accept(this));
        if (it.Funclist != null)
            it.Funclist.forEach(node -> node.accept(this));
        currentScope = currentScope.parentScope;
        currentClass = null;
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