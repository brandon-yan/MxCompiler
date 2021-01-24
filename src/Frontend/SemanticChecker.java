package Frontend;

import AST.*;
import Util.Position;
import Util.Type;
import Util.entity.FunctionEntity;
import Util.entity.VariableEntity;
import Util.error.SemanticError;
import Util.scope.*;

import java.util.ArrayList;

public class SemanticChecker implements ASTVisitor {
    public Scope currentScope;
    public Scope stringScope;
    public GlobalScope gScope;
    public TypeNode currentRettype;
    public String currentFunctionName;
    public Type currentClassType;
    public boolean containsRet;
    public boolean isConstructor;

    public SemanticChecker(GlobalScope gScope) {
        this.currentScope = gScope;
        this.stringScope = new Scope(gScope);
        this.gScope = gScope;
        this.currentRettype = null;
        this.currentFunctionName = null;
        this.currentClassType = null;
        this.containsRet = false;
        this.isConstructor = false;
    }
    @Override
    public void visit(ProgramNode it) {
        Position pos = new Position();
        ArrayList<VariableEntity> parameters = new ArrayList<>();
        parameters = new ArrayList<>();
        FunctionEntity builtin = new FunctionEntity("length", new TypeNode(pos, "int", 0), parameters, null);
        stringScope.defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        parameters.add(new VariableEntity("left", new TypeNode(pos,"int", 0), null));
        parameters.add(new VariableEntity("right", new TypeNode(pos,"int", 0), null));
        builtin = new FunctionEntity("substring", new TypeNode(pos, "string", 0), parameters, null);
        stringScope.defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        builtin = new FunctionEntity("parseInt", new TypeNode(pos, "int", 0), parameters, null);
        stringScope.defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        parameters.add(new VariableEntity("pos", new TypeNode(pos,"int", 0), null));
        builtin = new FunctionEntity("ord", new TypeNode(pos, "int", 0), parameters, null);
        stringScope.defineFunction(builtin, pos);

        currentScope = gScope;
        it.sectionList.forEach(section -> section.accept(this));
        if (!gScope.containsFunction("main", false))
            throw new SemanticError("lack main", it.pos);
    }

    @Override
    public void visit(TypeNode it) {
        if (!gScope.containsType(it.typename))
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
        if (it.type.typename.equals("void"))
            throw new SemanticError("void variable", it.pos);
        if (it.init != null) {
            it.init.accept(this);
            if (!it.type.typename.equals(it.init.type.typename) && !it.init.type.typename.equals("null"))
                throw new SemanticError("unmatch init", it.pos);
            if ((it.type.typename.equals("int") || it.type.typename.equals("bool"))&& it.type.dimension == 0 && it.init.type.typename.equals("null"))
                throw new SemanticError("unmatch init", it.pos);
        }
        it.type.type = gScope.getType(it.type.typename);
        VariableEntity var = new VariableEntity(it.name, it.type, it.init);
        currentScope.defineVariable(var, it.pos);
    }

    @Override public void visit(FuncDeclNode it) {
        containsRet = false;
        currentScope = new FunctionScope(currentScope);
        it.type.accept(this);
        currentRettype = it.type;
        currentFunctionName = it.identifier;
        if (it.parameterlist != null)
            it.parameterlist.accept(this);
        if (it.suite != null)
            it.suite.accept(this);
        currentScope = currentScope.parentScope;
        if (it.identifier.equals("main")) {
            containsRet = true;
            if (!currentRettype.typename.equals( "int"))
                throw new SemanticError("undefined main function", it.pos);
            if (it.parameterlist != null && !it.parameterlist.Varlist.isEmpty())
                throw new SemanticError("undefined main function", it.pos);
        }
        if (!it.type.typename.equals("void") && !containsRet)
            throw  new SemanticError("lack return", it.pos);
        if (currentScope != gScope && !(currentScope instanceof ClassScope))
            throw new SemanticError("local function", it.pos);
        containsRet = false;
        currentRettype = null;
        currentFunctionName = null;
    }
    @Override public void visit(ClassDeclNode it) {
        if (gScope.containsVariable(it.identifier, false) || gScope.containsFunction(it.identifier, false))
            throw new SemanticError("classname error", it.pos);
        currentClassType = gScope.getType(it.identifier);
        currentScope = new ClassScope(currentScope, it.identifier);
        if (it.Varlist != null)
            it.Varlist.forEach(node -> node.accept(this));
        if (it.Funclist != null)
            it.Funclist.forEach(node -> node.accept(this));
        if (it.Constructor != null)
            it.Constructor.forEach(node -> node.accept(this));
        currentScope = currentScope.parentScope;
        currentClassType = null;
    }
    @Override public void visit(ConstructorDeclNode it) {
        isConstructor = true;
        containsRet = false;
        if (!(currentScope instanceof ClassScope))
            throw new SemanticError("undefined function", it.pos);
        if (!it.identifier.equals(currentScope.classname))
            throw new SemanticError("constructor error", it.pos);
        currentScope = new FunctionScope(currentScope);
        if (it.parameterlist != null)
            it.parameterlist.accept(this);
        if (it.suite != null)
            it.suite.accept(this);
        currentScope = currentScope.parentScope;
        if (containsRet)
            throw new SemanticError("consturctor error", it.pos);
        isConstructor = false;
        containsRet = false;
    }

    @Override public void visit(BlockStmtNode it) {
        if (!it.stmts.isEmpty()) {
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
            if (!it.conditionexpr.type.typename.equals( "bool"))
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
            if (!it.conditionexpr.type.typename.equals( "bool"))
                throw new SemanticError("condition error", it.pos);
        }
        if (it.increaseexpr != null)
            it.increaseexpr.accept(this);
        currentScope = new LoopScope(currentScope);
        if (it.forstmt != null)
            it.forstmt.accept(this);
        currentScope = currentScope.parentScope;
    }
    @Override public void visit(WhileStmtNode it) {
        if (it.conditionexpr != null) {
            it.conditionexpr.accept(this);
            if (!it.conditionexpr.type.typename.equals("bool"))
                throw new SemanticError("condition error", it.pos);
        }
        currentScope = new LoopScope(currentScope);
        it.whilestmt.accept(this);
        currentScope = currentScope.parentScope;
    }
    @Override public void visit(BreakStmtNode it) {
        if (!(currentScope instanceof LoopScope))
            throw new SemanticError("break out of loop", it.pos);
    }
    @Override public void visit(ContinueStmtNode it) {
        if (!(currentScope instanceof LoopScope))
            throw new SemanticError("continue out of loop", it.pos);
    }
    @Override public void visit(ReturnStmtNode it) {
        containsRet = true;
        if (it.value != null) {
            it.value.accept(this);
            if (!it.value.type.typename.equals(currentRettype.typename))
                throw new SemanticError("unmatched return type", it.pos);
            if (it.value.type.dimension != currentRettype.dimension)
                throw new SemanticError("unmatched return dimension", it.pos);
        }
        else {
            if (isConstructor) {
                containsRet = false;
                return;
            }
            else if (!currentRettype.typename.equals("void") && !currentFunctionName.equals("main"))
                throw new SemanticError("lack return value", it.pos);
        }
    }

    @Override public void visit(AssignExprNode it) {
        it.lhs.accept(this);
        it.rhs.accept(this);
        if (it.rhs.type.typename.equals("null") && it.lhs.type.dimension == 0 && ((it.lhs.type.typename.equals("int")) || (it.lhs.type.typename.equals("bool"))))
            throw new SemanticError("unmatched assign", it.pos);
        if ((!it.lhs.type.typename.equals(it.rhs.type.typename) || it.lhs.type.dimension != it.rhs.type.dimension) && !it.rhs.type.typename.equals("null"))
            throw new SemanticError("unmatched assign", it.pos);
        if (!it.lhs.isAssignable())
            throw new SemanticError("not assignable", it.pos);
        it.type = it.rhs.type;
    }

    @Override public void visit(ArrayExprNode it) {
        it.name.accept(this);
        it.index.accept(this);
        if (!it.index.type.typename.equals("int") || it.index.type.dimension != 0)
            throw new SemanticError("index error", it.pos);
        if (it.name.type.dimension == 0)
            throw new SemanticError("not an array", it.pos);
        it.type = new TypeNode(it.pos, it.name.type.typename, it.name.type.dimension - 1);
        it.type.type = it.name.type.type;
    }

    @Override public void visit(BinaryExprNode it) {
        it.lhs.accept(this);
        it.rhs.accept(this);
        switch (it.opCode) {
            case sub, mul, div, mod, leftshift, rightshift, bitand, bitor, bitxor -> {
                if (!it.lhs.type.typename.equals(it.rhs.type.typename))
                    throw new SemanticError("unmatched type", it.pos);
                if (!it.lhs.type.typename.equals("int"))
                    throw new SemanticError("unmatched binaryop", it.pos);
                it.type = new TypeNode(it.pos, "int", 0);
            }
            case add -> {
                if (!it.lhs.type.typename.equals(it.rhs.type.typename))
                    throw new SemanticError("unmatched type", it.pos);
                if (!it.lhs.type.typename.equals("int") && !it.lhs.type.typename.equals("string"))
                    throw new SemanticError("unmatched binaryop", it.pos);
                it.type = it.lhs.type;
            }
            case less, lessequal, greater, greatequal -> {
                if (!it.lhs.type.typename.equals(it.rhs.type.typename))
                    throw new SemanticError("unmatched type", it.pos);
                if (!it.lhs.type.typename.equals("int") && !it.lhs.type.typename.equals("string"))
                    throw new SemanticError("unmatched binaryop", it.pos);
                it.type = new TypeNode(it.pos, "bool", 0);
            }
            case equal, notequal -> {
                if (it.rhs.type.typename.equals("null") && ((it.lhs.type.typename.equals("int")) || (it.lhs.type.typename.equals("bool"))))
                    throw new SemanticError("unmatched equal", it.pos);
                if (it.lhs.type.typename.equals("null") && ((it.rhs.type.typename.equals("int")) || (it.rhs.type.typename.equals("bool"))))
                    throw new SemanticError("unmatched equal", it.pos);
                if (!it.lhs.type.typename.equals(it.rhs.type.typename))
                    throw new SemanticError("unmatched type", it.pos);
                it.type = new TypeNode(it.pos, "bool", 0);
            }
            case logicand, logicor -> {
                if (!it.lhs.type.typename.equals(it.rhs.type.typename))
                    throw new SemanticError("unmatched type", it.pos);
                if (!it.lhs.type.typename.equals("bool"))
                    throw new SemanticError("unmatched binaryop", it.pos);
                it.type = new TypeNode(it.pos, "bool", 0);
            }
        }
    }

    @Override public void visit(FuncCallExprNode it) {
        FunctionEntity function = currentScope.getFuncEntity(it.funcname);
        if (function == null) {
            if (currentClassType != null && currentClassType.containsMethods(it.funcname))
                function = currentClassType.getMethod(it.funcname);
            else throw new SemanticError("undefined function", it.pos);
        }
        if (it.parameters != null)
            it.parameters.forEach(para -> para.accept(this));

        ArrayList<VariableEntity> actual = function.parameters;
        ArrayList<ExprNode> formal = it.parameters;
        int siz = actual.size();
        if (siz != formal.size())
            throw new SemanticError("parameters count error", it.pos);
        for (int i = 0; i < siz; ++i) {
            VariableEntity tmp1 = actual.get(i);
            ExprNode tmp2 = formal.get(i);
            if (!tmp1.vartype.typename.equals(tmp2.type.typename) || tmp1.vartype.dimension != tmp2.type.dimension)
                throw new SemanticError("parameters type error", it.pos);
        }
        it.type = function.functype;
    }

    @Override public void visit(IdentifierExprNode it) {
        String name = it.identifier;
        VariableEntity var = currentScope.getVarEntity(name);
        if (var == null)
            throw new SemanticError("undefined variable", it.pos);
        it.type = var.vartype;
    }

    @Override public void visit(MemberExprNode it) {
        it.expr.accept(this);
        if (it.expr.type.type.typename != Type.type.CLASS)
            throw new SemanticError("member access error", it.pos);
        if (it.expr.type.type.containsMembers(it.member)) {
            VariableEntity member = it.expr.type.type.getMember(it.member);
            if (member == null)
                throw new SemanticError("member access error", it.pos);
            it.type = member.vartype;
        }
        else throw new SemanticError("member access error", it.pos);
    }

    @Override public void visit(MethodExprNode it) {
        it.expr.accept(this);
        if (it.parameters != null)
            it.parameters.forEach(para -> para.accept(this));
        if (it.expr.type.type.typename != Type.type.CLASS && it.expr.type.type.typename != Type.type.STRING) {
            if (it.expr.type.dimension == 0 || !it.methodname.equals("size"))
                throw new SemanticError("array method access error", it.pos);
            it.type = new TypeNode(it.pos, "int", 0);
        }
        else if (it.expr.type.type.typename == Type.type.STRING) {
            if (it.expr.type.dimension == 0) {
                FunctionEntity method = stringScope.getFuncEntity(it.methodname);
                if (method == null)
                    throw new SemanticError("string method access error", it.pos);
                ArrayList<VariableEntity> actual = method.parameters;
                ArrayList<ExprNode> formal = it.parameters;
                int siz = actual.size();
                if (siz != formal.size())
                    throw new SemanticError("string method parameters count error", it.pos);
                for (int i = 0; i < siz; ++i) {
                    VariableEntity tmp1 = actual.get(i);
                    ExprNode tmp2 = formal.get(i);
                    if (!tmp1.vartype.typename.equals(tmp2.type.typename) || tmp1.vartype.dimension != tmp2.type.dimension)
                        throw new SemanticError("string method parameters type error", it.pos);
                }
                it.type = method.functype;
            }
            else {
                if (!it.methodname.equals("size"))
                    throw new SemanticError("method access error", it.pos);
            }
        }
        else {
            if (it.expr.type.type.containsMethods(it.methodname)) {
                FunctionEntity method = it.expr.type.type.getMethod(it.methodname);
                if (method == null)
                    throw new SemanticError("method access error", it.pos);
                ArrayList<VariableEntity> actual = method.parameters;
                ArrayList<ExprNode> formal = it.parameters;
                int siz = actual.size();
                if (siz != formal.size())
                    throw new SemanticError("method parameters count error", it.pos);
                for (int i = 0; i < siz; ++i) {
                    VariableEntity tmp1 = actual.get(i);
                    ExprNode tmp2 = formal.get(i);
                    if (!tmp1.vartype.typename.equals(tmp2.type.typename) || tmp1.vartype.dimension != tmp2.type.dimension)
                        throw new SemanticError("method parameters type error", it.pos);
                }
                it.type = method.functype;
            }
            else throw new SemanticError("method access error", it.pos);
        }

    }

    @Override public void visit(NewExprNode it) {
        for (ExprNode siz : it.arraysize) {
            siz.accept(this);
            if (!siz.type.typename.equals("int"))
                throw new SemanticError("new error", it.pos);
        }
        if (gScope.containsType(it.type.typename))
            it.type.type = gScope.getType(it.type.typename);
    }

    @Override public void visit(PrefixExprNode it) {
        it.expr.accept(this);
        switch (it.opCode) {
            case prefixadd, prefixsub -> {
                if (!it.expr.type.typename.equals("int") || !it.expr.isAssignable())
                    throw new SemanticError("unmatched prefix", it.pos);
                it.type = new TypeNode(it.pos, "int", 0);
            }
            case positive, negative, bitnot -> {
                if (!it.expr.type.typename.equals("int"))
                    throw new SemanticError("unmatched prefix", it.pos);
                it.type = new TypeNode(it.pos, "int", 0);
            }
            case logicnot -> {
                if (!it.expr.type.typename.equals("bool"))
                    throw new SemanticError("unmatched prefix", it.pos);
                it.type = new TypeNode(it.pos, "bool", 0);
            }
        }
    }

    @Override public void visit(SuffixExprNode it) {
        it.expr.accept(this);
        if (!it.expr.type.typename.equals("int") || !it.expr.isAssignable())
            throw new SemanticError("unmatched suffix", it.pos);
        it.type = new TypeNode(it.pos, "int", 0);
    }

    @Override public void visit(ThisExprNode it) {
        if (currentClassType == null)
            throw new SemanticError("this out of class", it.pos);
        it.type = new TypeNode(it.pos, currentClassType.classname, 0);
        it.type.type = currentClassType;
    }

    @Override public void visit(VarDeclStmtNode it) {
        it.type.accept(this);
        it.varlist.accept(this);
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
