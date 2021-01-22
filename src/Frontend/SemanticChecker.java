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
    public Type currentRettype;
    public String currentFunctionName;
    public String currentClassName;
    public boolean containsRet;

    public SemanticChecker(GlobalScope gScope) {
        this.currentScope = gScope;
        this.gScope = gScope;
        currentRettype = null;
        currentFunctionName = null;
        currentClassName = null;
        containsRet = false;
    }
    @Override
    public void visit(ProgramNode it) {
        currentScope = gScope;
        it.sectionList.forEach(section -> section.accept(this));
        if (gScope.containsFunction("main", false) == false)
            throw new SemanticError("lack main", it.pos);
    }

    @Override
    public void visit(TypeNode it) {
        if (gScope.containsType(it.typename) == false)
            throw new SemanticError("undefined type", it.pos);
    }

    @Override
    public void visit(VarDeclNode it) {
        it.type.accept(this);
        it.varlist.accept(this);
    }

    @Override
    public void visit(VarListNode it) {
        it.Varlist.forEach(node -> node.accept(this));
    }

    @Override
    public void visit(VarNode it) {
        it.type.accept(this);
        if (it.type.typename == "void")
            throw new SemanticError("void variable", it.pos);
        if (it.init != null) {
            it.init.accept(this);
            if (it.type.typename != it.init.type.typename)
                throw new SemanticError("unmatch init", it.pos);
        }
        VariableEntity var = new VariableEntity(it.name, it.type, it.init);
        currentScope.defineVariable(var, it.pos);
    }

    @Override public void visit(FuncDeclNode it) {
        containsRet = false;
        currentScope = new FunctionScope(currentScope);
        it.type.accept(this);
        currentRettype = it.type.type;
        currentFunctionName = it.identifier;
        if (it.parameterlist != null)
            it.parameterlist.accept(this);
        if (it.suite != null)
            it.suite.accept(this);
        currentScope = currentScope.parentScope;
        if (it.identifier == "main") {
            containsRet = true;
            if (currentRettype.classname != "int")
                throw new SemanticError("undefined main function", it.pos);
            if (it.parameterlist != null && !it.parameterlist.Varlist.isEmpty())
                throw new SemanticError("undefined main function", it.pos);
        }
        if (it.type.typename != "void" && containsRet == false)
            throw  new SemanticError("lack return", it.pos);
        if (currentScope != gScope || !(currentScope instanceof ClassScope))
            throw new SemanticError("local function", it.pos);
        containsRet = false;
        currentRettype = null;
        currentFunctionName = null;
    }
    @Override public void visit(ClassDeclNode it) {
        currentClassName = it.identifier;
        currentScope = new ClassScope(currentScope, it.identifier);
        if (it.Varlist != null)
            it.Varlist.forEach(node -> node.accept(this));
        if (it.Funclist != null)
            it.Funclist.forEach(node -> node.accept(this));
        if (it.Constructor != null)
            it.Constructor.forEach(node -> node.accept(this));
        currentScope = currentScope.parentScope;
        currentClassName = null;
    }
    @Override public void visit(ConstructorDeclNode it) {
        containsRet = false;
        if (currentScope instanceof ClassScope == false)
            throw new SemanticError("undefined function", it.pos);
        if (it.identifier != currentScope.classname)
            throw new SemanticError("constructor error", it.pos);
        currentScope = new FunctionScope(currentScope);
        if (it.parameterlist != null)
            it.parameterlist.accept(this);
        if (it.suite != null)
            it.suite.accept(this);
        currentScope = currentScope.parentScope;
        if (containsRet == true)
            throw new SemanticError("consturctor error", it.pos);
        containsRet = false;
    }

    @Override public void visit(BlockStmtNode it) {
        if (it.stmts.isEmpty() == false) {
            if (currentScope instanceof LoopScope)
                currentScope = new LoopScope(currentScope);
            else if (currentScope instanceof FunctionScope)
                currentScope = new FunctionScope(currentScope);
            else if (currentScope instanceof ClassScope)
                currentScope = new ClassScope(currentScope, currentScope.classname);
            else currentScope = new Scope(currentScope);
            it.stmts.forEach(node -> node.accept(this));
            currentScope = currentScope.parentScope;
        }
    }

    @Override public void visit(ExprStmtNode it) {
        it.expr.accept(this);
    }

    @Override public void visit(IfStmtNode it) {
        if (it.conditionexpr != null) {
            it.conditionexpr.accept(this);
            if (it.conditionexpr.type.typename != "bool")
                throw new SemanticError("condition error", it.pos);
        }
        currentScope = new Scope(currentScope);
        if (it.thenstmt != null)
            it.thenstmt.accept(this);
        currentScope = currentScope.parentScope;
        if (it.elsestmt != null) {
            currentScope = new Scope(currentScope);
            it.elsestmt.accept(this);
            currentScope = currentScope.parentScope;
        }
    }

    @Override public void visit(ForStmtNode it) {
        if (it.initexpr != null)
            it.initexpr.accept(this);
        if (it.conditionexpr != null) {
            it.conditionexpr.accept(this);
            if (it.conditionexpr.type.typename != "bool")
                throw new SemanticError("condition error", it.pos);
        }
        if (it.increaseexpr != null)
            it.increaseexpr.accept(this);
        currentScope = new LoopScope(currentScope);
        it.forstmt.accept(this);
        currentScope = currentScope.parentScope;
    }
    @Override public void visit(WhileStmtNode it) {
        if (it.conditionexpr != null) {
            it.conditionexpr.accept(this);
            if (it.conditionexpr.type.typename != "bool")
                throw new SemanticError("condition error", it.pos);
        }
        currentScope = new LoopScope(currentScope);
        it.whilestmt.accept(this);
        currentScope = currentScope.parentScope;
    }
    @Override public void visit(BreakStmtNode it) {
        if (currentScope instanceof LoopScope == false)
            throw new SemanticError("break out of loop", it.pos);
    }
    @Override public void visit(ContinueStmtNode it) {
        if (currentScope instanceof LoopScope == false)
            throw new SemanticError("continue out of loop", it.pos);
    }
    @Override public void visit(ReturnStmtNode it) {
        containsRet = true;
        if (it.value != null) {
            it.value.accept(this);
            if (it.value.type.typename != currentRettype.classname)
                throw new SemanticError("unmatched return type", it.pos);
            if (it.value.type.dimension != currentRettype.dimension)
                throw new SemanticError("unmatched return dimension", it.pos);
        }
        else if (currentRettype.classname != "void" || currentFunctionName != "main")
            throw new SemanticError("lack return value", it.pos);
    }

    @Override public void visit(AssignExprNode it) {
        it.lhs.accept(this);
        it.rhs.accept(this);
        if (it.rhs.type.typename == "null" && ((it.lhs.type.typename == "int") || (it.lhs.type.typename == "bool")))
            throw new SemanticError("unmatched assign", it.pos);
        if ((it.lhs.type.typename != it.rhs.type.typename || it.lhs.type.dimension != it.rhs.type.dimension) && it.rhs.type.typename != "null")
            throw new SemanticError("unmatched assign", it.pos);
        if (it.lhs.isAssignable() == false)
            throw new SemanticError("not assignable", it.pos);
        it.type = it.rhs.type;
    }

    @Override public void visit(ArrayExprNode it) {
        it.name.accept(this);
        it.index.accept(this);
        if (it.index.type.typename != "int")
            throw new SemanticError("index error", it.pos);
        it.type = new TypeNode(it.pos, it.type.typename, it.type.dimension + 1);
    }

    @Override public void visit(BinaryExprNode it) {
        it.lhs.accept(this);
        it.rhs.accept(this);
        switch (it.opCode) {
            case sub, mul, div, mod, leftshift, rightshift, bitand, bitor, bitxor: {
                if (it.lhs.type.typename != it.rhs.type.typename)
                    throw new SemanticError("unmatched type", it.pos);
                if (it.lhs.type.typename != "int")
                    throw new SemanticError("unmatched binaryop", it.pos);
                it.type = new TypeNode(it.pos, "int", 0);
            }
            case add: {
                if (it.lhs.type.typename != it.rhs.type.typename)
                    throw new SemanticError("unmatched type", it.pos);
                if (it.lhs.type.typename != "int" || it.lhs.type.typename != "string")
                    throw new SemanticError("unmatched binaryop", it.pos);
                it.type = it.lhs.type;
            }
            case less, lessequal, greater, greatequal: {
                if (it.lhs.type.typename != it.rhs.type.typename)
                    throw new SemanticError("unmatched type", it.pos);
                if (it.lhs.type.typename != "int" || it.lhs.type.typename != "string")
                    throw new SemanticError("unmatched binaryop", it.pos);
                it.type = new TypeNode(it.pos, "bool", 0);
            }
            case equal, notequal: {
                if (it.rhs.type.typename == "null" && ((it.lhs.type.typename == "int") || (it.lhs.type.typename == "bool")))
                    throw new SemanticError("unmatched equal", it.pos);
                if (it.lhs.type.typename == "null" && ((it.rhs.type.typename == "int") || (it.rhs.type.typename == "bool")))
                    throw new SemanticError("unmatched equal", it.pos);
                if (it.lhs.type.typename != it.rhs.type.typename)
                    throw new SemanticError("unmatched type", it.pos);
                it.type = new TypeNode(it.pos, "bool", 0);
            }
            case logicand, logicor: {
                if (it.lhs.type.typename != it.rhs.type.typename)
                    throw new SemanticError("unmatched type", it.pos);
                if (it.lhs.type.typename != "bool")
                    throw new SemanticError("unmatched binaryop", it.pos);
                it.type = new TypeNode(it.pos, "bool", 0);
            }
        }
    }

    @Override public void visit(FuncCallExprNode it) {}

    @Override public void visit(IdentifierExprNode it) {}

    @Override public void visit(MemberExprNode it) {}

    @Override public void visit(NewExprNode it) {
        for (ExprNode siz : it.arraysize) {
            siz.accept(this);
            if (siz.type.typename != "int")
                throw new SemanticError("new error", it.pos);
        }
    }

    @Override public void visit(PrefixExprNode it) {
        it.expr.accept(this);
        switch (it.opCode) {
            case prefixadd, prefixsub: {
                if (it.expr.type.typename != "int" || it.expr.isAssignable() == false)
                    throw new SemanticError("unmatched prefix", it.pos);
                it.type = new TypeNode(it.pos, "int", 0);
            }
            case positive, negative, bitnot: {
                if (it.expr.type.typename != "int")
                    throw new SemanticError("unmatched prefix", it.pos);
                it.type = new TypeNode(it.pos, "int", 0);
            }
            case logicnot: {
                if (it.expr.type.typename != "bool")
                    throw new SemanticError("unmatched prefix", it.pos);
                it.type = new TypeNode(it.pos, "bool", 0);
            }
        }
    }

    @Override public void visit(SuffixExprNode it) {
        it.expr.accept(this);
        if (it.expr.type.typename != "int" || it.expr.isAssignable() == false)
            throw new SemanticError("unmatched suffix", it.pos);
        it.type = new TypeNode(it.pos, "int", 0);
    }

    @Override public void visit(ThisExprNode it) {
        if (currentClassName == null)
            throw new SemanticError("this out of class", it.pos);
        it.type = new TypeNode(it.pos, currentClassName, 0);
    }

    @Override public void visit(IntLiteralNode it) {
        it.type = new TypeNode(it.pos, "int", 0);
    }
    @Override public void visit(BoolLiteralNode it) {
        it.type = new TypeNode(it.pos, "bool", 0);
    }
    @Override public void visit(StringLiteralNode it) {
        it.type = new TypeNode(it.pos, "string", 0);
    }
    @Override public void visit(NullLiteralNode it) {
        it.type = new TypeNode(it.pos, "null", 0);
    }
}
