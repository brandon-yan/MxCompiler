package Backend;

import AST.*;
import MIR.*;
import MIR.Instruction.*;
import MIR.Module;
import MIR.Operand.*;
import MIR.TypeSystem.*;
import Parser.MxParser;
import Util.*;
import Util.scope.GlobalScope;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Stack;

public class IRBuilder implements ASTVisitor{

    public boolean isparamter;
    public GlobalScope gScope;
    public Module IRmodule;
    public Function IRfunction;
    public BasicBlock IRbasicblock;
    public String className;
    public Stack<BasicBlock> breakStack = new Stack<>();
    public Stack<BasicBlock> continueStack = new Stack<>();
    public boolean inFunction = false;
    public static int blockCnt, regCnt, loopCnt;
    public IdExprAddrMap idAddrMap;

    public IRBuilder(GlobalScope gScope, Module module) {
        isparamter = false;
        this.gScope = gScope;
        this.IRmodule = module;
        this.IRfunction = null;
        this.IRbasicblock = null;
        this.className = null;
        blockCnt = 0;
        regCnt = 0;
        loopCnt = 0;
    }

    @Override
    public void visit(ProgramNode it) {
        ClassType stringType = new ClassType("string");
        IRmodule.types.put("String", stringType);
        Function func = new Function("l_string_length");
        func.retType = new FunctionType(Module.i32T);
        func.addParameter(new Parameter(Module.stringT, "str"));
        func.builtin = true;
        IRmodule.functions.put("l_string_length", func);

        func = new Function("l_string_substring");
        func.retType = new FunctionType(Module.stringT);
        func.addParameter(new Parameter(Module.stringT, "str"));
        func.addParameter(new Parameter(Module.i32T, "left"));
        func.addParameter(new Parameter(Module.i32T, "right"));
        func.builtin = true;
        IRmodule.functions.put("l_string_substring", func);

        func = new Function("l_string_parseInt");
        func.retType = new FunctionType(Module.i32T);
        func.addParameter(new Parameter(Module.stringT, "str"));
        func.builtin = true;
        IRmodule.functions.put("l_string_parseInt", func);

        func = new Function("l_string_ord");
        func.retType = new FunctionType(Module.i32T);
        func.addParameter(new Parameter(Module.stringT, "str"));
        func.addParameter(new Parameter(Module.i32T, "pos"));
        func.builtin = true;
        IRmodule.functions.put("l_string_ord", func);


        //collect classes
        for (ProgramDeclNode section: it.sectionList) {
            if (section instanceof ClassDeclNode) {
                ClassDeclNode tmp = (ClassDeclNode) section;
                ClassType tmpIRtype = new ClassType(tmp.identifier);
                IRmodule.types.put(tmp.identifier, tmpIRtype);
            }
        }

        //add classes
        for (ProgramDeclNode section: it.sectionList) {
            if (section instanceof ClassDeclNode) {
                ClassDeclNode tmpClassNode = (ClassDeclNode) section;
                ClassType tmpIRtype = IRmodule.types.get(tmpClassNode.identifier);

                //add members
                for (VarDeclNode tmpVarDecl: tmpClassNode.Varlist) {
                    VarListNode tmpVarList = tmpVarDecl.varlist;
                    for (VarNode tmpVar: tmpVarList.Varlist)
                        tmpIRtype.addMember(IRmodule.getIRType(tmpVar.type), tmpVar.name);
                }
                IRmodule.types.replace(tmpClassNode.identifier, tmpIRtype);

                //add constructor
                String tmpFuncName = tmpClassNode.identifier + "." + tmpClassNode.identifier;
                FunctionType tmpFuncType = new FunctionType(new VoidType());
                Function tmpIRfunction = new Function(tmpFuncName);
                if (tmpClassNode.Constructor.size() != 0) {
                    tmpFuncType.parameters.add(tmpIRtype);
                    Parameter tmpClassPtr = new Parameter(new PointerType(tmpIRtype), "this");
                    tmpIRfunction.addParameter(tmpClassPtr);
                    tmpClassPtr.needPtr = true;
                }
                else
                    tmpIRfunction.builtin = true;
                tmpIRfunction.retType = tmpFuncType;
                IRmodule.functions.put(tmpFuncName, tmpIRfunction);

                //add methods
                for (FuncDeclNode tmpFunc: tmpClassNode.Funclist) {
                    tmpFuncName = tmpClassNode.identifier + "." + tmpFunc.identifier;
                    tmpFuncType = new FunctionType(IRmodule.getIRType(tmpFunc.type));
                    tmpFuncType.parameters.add(tmpIRtype);
                    if (tmpFunc.parameterlist != null) {
                        VarListNode parameters = tmpFunc.parameterlist;
                        for (VarNode par: parameters.Varlist)
                            tmpFuncType.parameters.add(IRmodule.getIRType(par.type));
                    }
                    tmpIRfunction = new Function(tmpFuncName);
                    tmpIRfunction.retType = tmpFuncType;

                    Parameter tmpPtr = new Parameter(new PointerType(tmpIRtype), "this");
                    tmpIRfunction.parameters.add(tmpPtr);
                    tmpPtr.needPtr = true;
                    if (tmpFunc.parameterlist != null) {
                        VarListNode parameters = tmpFunc.parameterlist;
                        for (VarNode par: parameters.Varlist)
                            tmpIRfunction.parameters.add(new Parameter(IRmodule.getIRType(par.type), par.name));
                    }
                    IRmodule.functions.put(tmpFuncName, tmpIRfunction);
                }
            }
        }

        //add functions
        for (ProgramDeclNode section: it.sectionList) {
            if (section instanceof FuncDeclNode) {
                FuncDeclNode tmpFuncNode = (FuncDeclNode) section;
                String tmpFuncName = tmpFuncNode.identifier;
                FunctionType tmpFuncType = new FunctionType(IRmodule.getIRType(tmpFuncNode.type));
                VarListNode tmpParList = tmpFuncNode.parameterlist;
                if (tmpParList != null)
                    for (VarNode tmpPar: tmpParList.Varlist)
                        tmpFuncType.parameters.add(IRmodule.getIRType(tmpPar.type));
                Function tmpIRfunction = new Function(tmpFuncName);
                tmpIRfunction.retType = tmpFuncType;
                if (tmpParList != null)
                    for (VarNode tmpPar: tmpParList.Varlist) {
                        Parameter tmpParameter = new Parameter(IRmodule.getIRType(tmpPar.type), tmpPar.name);
                        tmpIRfunction.addParameter(tmpParameter);
                    }
                IRmodule.functions.put(tmpFuncName, tmpIRfunction);
            }
        }

        //add global variables
        Function tmpIRfunction = IRmodule.functions.get("g_init");
        IRfunction = tmpIRfunction;
        IRbasicblock = tmpIRfunction.entry;
        for (ProgramDeclNode section: it.sectionList) {
            if (section instanceof VarDeclNode)
                section.accept(this);
        }

        Instruction tmp = new BranchInst(IRbasicblock, null, IRfunction.exit, null);
        IRbasicblock.addInst(tmp);
        IRbasicblock = IRfunction.exit;
        tmp = new ReturnInst(IRbasicblock, new VoidType(), null);
        IRbasicblock.addInst(tmp);

        IRfunction = null;
        IRbasicblock= null;
        for (ProgramDeclNode section: it.sectionList) {
            if (section instanceof ClassDeclNode)
                section.accept(this);
            if (section instanceof FuncDeclNode)
                section.accept(this);
        }
//        for (ProgramDeclNode section: it.sectionList) {
//            if (section instanceof FuncDeclNode)
//                section.accept(this);
//        }

    }

    @Override public void visit(TypeNode it) {}
    @Override public void visit(VarDeclNode it) {
        it.type.accept(this);
        it.varlist.accept(this);
    }
    @Override public void visit(VarListNode it) {
        it.Varlist.forEach(node -> node.accept(this));
    }
    @Override public void visit(VarNode it) {
        IRType tmpType = IRmodule.getIRType(it.type);
        if (className == null && !inFunction) {
            //global
            GlobalVariable tmpVar = new GlobalVariable(tmpType, it.name, null);
            if (it.init != null) {
                it.init.accept(this);
                tmpVar.init = it.init.ExprRet;
                Instruction tmp = new StoreInst(IRbasicblock, tmpVar, tmpVar.init);
                IRbasicblock.addInst(tmp);
            }
            IRmodule.global.put(tmpVar.name, tmpVar);
        }
        else if (idAddrMap != null) {
            //local
            Register tmpReg = new Register(new PointerType(tmpType), it.name + ".addr" + (regCnt++));
            if (!idAddrMap.IdAddrMap.containsKey(it.name)) {
                idAddrMap.addIdAddr(it.name, tmpReg);
                Instruction tmp = new AllocateInst(IRbasicblock, new PointerType(tmpType), tmpReg);
                IRbasicblock.addInst(tmp);
            }
            if (it.init != null) {
                it.init.accept(this);
                Instruction tmp = new StoreInst(IRbasicblock, tmpReg, it.init.ExprRet);
                IRbasicblock.addInst(tmp);
            }
        }
    }
    @Override public void visit(FuncDeclNode it) {
        inFunction = true;
        idAddrMap = new IdExprAddrMap(idAddrMap);

        String tmpFunName;
        if (className == null)
            tmpFunName = it.identifier;
        else tmpFunName = className + "." +it.identifier;
        IRfunction = IRmodule.functions.get(tmpFunName);
        IRbasicblock = IRfunction.entry;

        for (int i = 0; i < IRfunction.parameters.size(); ++i) {
            Parameter tmpPar = IRfunction.parameters.get(i);
            IRType tmpParType = tmpPar.IRtype;
            Register tmpReg = new Register(new PointerType(tmpParType), tmpPar.name + ".addr" + (regCnt++));
            tmpReg.needPtr = tmpPar.needPtr;

            AllocateInst tmp = new AllocateInst(IRbasicblock, new PointerType(tmpParType), tmpReg);
            IRbasicblock.addInst(tmp);
            StoreInst tmp1 = new StoreInst(IRbasicblock, tmpReg, tmpPar);
            IRbasicblock.addInst(tmp1);
            idAddrMap.addIdAddr(tmpPar.name, tmpReg);
        }
        IRfunction.retValue = new Register(new PointerType(IRmodule.getIRType(it.type)), tmpFunName + "ret_val" + (regCnt++));

        if (it.identifier.equals("main")) {
            Function mainFunc = IRmodule.functions.get("g_init");
            ArrayList<Operand> parameters = new ArrayList<>();
            CallInst tmp = new CallInst(IRbasicblock, mainFunc, null, parameters);
            IRbasicblock.addInst(tmp);
        }

        if (it.suite != null)
            it.suite.accept(this);

        BranchInst tmp = new BranchInst(IRbasicblock, null, IRfunction.exit, null);
        IRbasicblock.addInst(tmp);

        IRbasicblock = IRfunction.exit;
        IRType tmpType = new VoidType();
        if (IRfunction.retValue != null) {
            if (IRfunction.retValue.IRtype instanceof PointerType)
                tmpType = ((PointerType)IRfunction.retValue.IRtype).point;
            else
                tmpType = IRfunction.retValue.IRtype;
        }

        ReturnInst tmp1 = new ReturnInst(IRbasicblock, tmpType, IRfunction.retValue);
        IRbasicblock.addInst(tmp1);

        inFunction = false;
        idAddrMap = idAddrMap.parentMap;
        IRfunction = null;
        IRbasicblock = null;

    }
    @Override public void visit(ClassDeclNode it) {
        className = it.identifier;
        if (it.Varlist != null)
            it.Varlist.forEach(node -> node.accept(this));
        if (it.Funclist != null)
            it.Funclist.forEach(node -> node.accept(this));
        if (it.Constructor != null)
            it.Constructor.forEach(node -> node.accept(this));
        className = null;
    }

    @Override public void visit(ConstructorDeclNode it) {
        inFunction = true;
        idAddrMap = new IdExprAddrMap(idAddrMap);

        String tmpFunName = className + "." + className;
        IRfunction = IRmodule.functions.get(tmpFunName);
        IRbasicblock = IRfunction.entry;

        for (int i = 0; i < IRfunction.parameters.size(); ++i) {
            Parameter tmpPar = IRfunction.parameters.get(i);
            IRType tmpParType = tmpPar.IRtype;
            Register tmpReg = new Register(new PointerType(tmpParType), tmpPar.name + ".addr" + (regCnt++));
            tmpReg.needPtr = tmpPar.needPtr;

            AllocateInst tmp = new AllocateInst(IRbasicblock, new PointerType(tmpParType), tmpReg);
            IRbasicblock.addInst(tmp);
            StoreInst tmp1 = new StoreInst(IRbasicblock, tmpReg, tmpPar);
            IRbasicblock.addInst(tmp1);
            idAddrMap.addIdAddr(tmpPar.name, tmpReg);
        }

        if (it.suite != null)
            it.suite.accept(this);

        BranchInst tmp = new BranchInst(IRbasicblock, null, IRfunction.exit, null);
        IRbasicblock.addInst(tmp);

        IRbasicblock = IRfunction.exit;

        ReturnInst tmp1 = new ReturnInst(IRbasicblock, new VoidType(), null);
        IRbasicblock.addInst(tmp1);

        inFunction = false;
        idAddrMap = idAddrMap.parentMap;
        IRfunction = null;
        IRbasicblock = null;
    }

    @Override public void visit(BlockStmtNode it) {
        for (StmtNode stmt: it.stmts) {
            if (stmt != null)
                stmt.accept(this);
        }
    }
    @Override public void visit(ExprStmtNode it) {
        it.expr.accept(this);
    }
    @Override public void visit(IfStmtNode it) {
        BasicBlock ifTrueBlock = new BasicBlock("if_true_block" + (blockCnt++));
        ifTrueBlock.thisFunction = IRfunction;
        BasicBlock ifFalseBlock = null;
        if (it.elsestmt != null) {
            ifFalseBlock = new BasicBlock("if_false_block" + (blockCnt++));
            ifFalseBlock.thisFunction= IRfunction;
        }
        BasicBlock ifDestBlock = new BasicBlock("if_dest_block" + (blockCnt++));

        it.conditionexpr.trueBlock = ifTrueBlock;
        it.conditionexpr.falseBlock = Objects.requireNonNullElse(ifFalseBlock, ifDestBlock);
        it.conditionexpr.accept(this);

        if (it.thenstmt != null) {
            IRbasicblock = ifTrueBlock;
            idAddrMap = new IdExprAddrMap(idAddrMap);
            IRfunction.addBasicBlock(ifTrueBlock);
            it.thenstmt.accept(this);
            BranchInst tmp = new BranchInst(IRbasicblock, null, ifDestBlock, null);
            IRbasicblock.addInst(tmp);
            idAddrMap = idAddrMap.parentMap;
        }

        if (it.elsestmt != null) {
            IRbasicblock = ifFalseBlock;
            idAddrMap = new IdExprAddrMap(idAddrMap);
            IRfunction.addBasicBlock(ifFalseBlock);
            it.elsestmt.accept(this);
            BranchInst tmp = new BranchInst(IRbasicblock, null, ifDestBlock, null);
            IRbasicblock.addInst(tmp);
            idAddrMap = idAddrMap.parentMap;
        }

        IRbasicblock = ifDestBlock;
        IRfunction.addBasicBlock(IRbasicblock);
    }
    @Override public void visit(ForStmtNode it) {
        loopCnt++;
        BasicBlock forInitBlock, forCondBlock, forIncrBlock;
        if (it.initexpr != null) {
            forInitBlock = new BasicBlock("for_init_block" + (blockCnt)++);
            forInitBlock.thisFunction = IRfunction;
        }
        else forInitBlock = null;
        if (it.conditionexpr != null) {
            forCondBlock = new BasicBlock("for_cond_block" + (blockCnt)++);
            forCondBlock.thisFunction = IRfunction;
        }
        else forCondBlock = null;
        if (it.increaseexpr != null) {
            forIncrBlock = new BasicBlock("for_incr_block" + (blockCnt)++);
            forIncrBlock.thisFunction = IRfunction;
        }
        else forIncrBlock = null;
        BasicBlock forDestBlock = new BasicBlock("for_dest_block" + (blockCnt)++);
        forDestBlock.thisFunction = IRfunction;
        BasicBlock forSuiteBlock = new BasicBlock("for_suite_block" + (blockCnt)++);
        forSuiteBlock.thisFunction = IRfunction;

        if (it.initexpr != null) {
            BranchInst tmp = new BranchInst(IRbasicblock, null, forInitBlock, null);
            IRbasicblock.addInst(tmp);
            IRbasicblock = forInitBlock;
            IRfunction.addBasicBlock(IRbasicblock);
            idAddrMap = new IdExprAddrMap(idAddrMap);
            it.initexpr.accept(this);
            idAddrMap = idAddrMap.parentMap;
        }

        if (it.conditionexpr != null) {
            BranchInst tmp = new BranchInst(IRbasicblock, null, forCondBlock, null);
            IRbasicblock.addInst(tmp);
            IRbasicblock = forCondBlock;
            IRfunction.addBasicBlock(IRbasicblock);
            idAddrMap = new IdExprAddrMap(idAddrMap);
            it.conditionexpr.trueBlock = forSuiteBlock;
            it.conditionexpr.falseBlock = forDestBlock;
            it.conditionexpr.accept(this);
            idAddrMap = idAddrMap.parentMap;
        }
        else {
            BranchInst tmp = new BranchInst(IRbasicblock, null, forSuiteBlock, null);
            IRbasicblock.addInst(tmp);
        }

        idAddrMap = new IdExprAddrMap(idAddrMap);
        IRbasicblock = forSuiteBlock;
        IRfunction.addBasicBlock(forSuiteBlock);
        if (it.increaseexpr != null)
            continueStack.push(forIncrBlock);
        else
            continueStack.push(forDestBlock);
        breakStack.push(forDestBlock);

        if (it.forstmt != null)
            it.forstmt.accept(this);

        BranchInst tmp;
        if (it.increaseexpr != null)
            tmp = new BranchInst(IRbasicblock, null, forIncrBlock, null);
        else {
            if (it.conditionexpr != null)
                tmp = new BranchInst(IRbasicblock, null, forCondBlock, null);
            else
                tmp = new BranchInst(IRbasicblock, null, forDestBlock, null);
        }
        IRbasicblock.addInst(tmp);
        idAddrMap = idAddrMap.parentMap;

        if (it.increaseexpr != null) {
            idAddrMap = new IdExprAddrMap(idAddrMap);
            IRbasicblock = forIncrBlock;
            IRfunction.addBasicBlock(IRbasicblock);
            it.increaseexpr.accept(this);
            BranchInst tmp1;
            if (it.conditionexpr != null)
                tmp1 = new BranchInst(IRbasicblock, null, forCondBlock, null);
            else
                tmp1 = new BranchInst(IRbasicblock, null, forSuiteBlock, null);
            IRbasicblock.addInst(tmp1);
            idAddrMap = idAddrMap.parentMap;
        }
        IRbasicblock = forDestBlock;
        IRfunction.addBasicBlock(IRbasicblock);
        continueStack.pop();
        breakStack.pop();
    }
    @Override public void visit(WhileStmtNode it) {
        BasicBlock whileCondBlock;
        if (it.conditionexpr != null) {
            whileCondBlock = new BasicBlock("while_cond_block" + (blockCnt)++);
            whileCondBlock.thisFunction = IRfunction;
        }
        else whileCondBlock = null;
        BasicBlock whileDestBlock = new BasicBlock("while_dest_block" + (blockCnt)++);
        whileDestBlock.thisFunction = IRfunction;
        BasicBlock whileSuiteBlock = new BasicBlock("while_suite_block" + (blockCnt)++);
        whileSuiteBlock.thisFunction = IRfunction;

        if (it.conditionexpr != null) {
            BranchInst tmp = new BranchInst(IRbasicblock, null, whileCondBlock, null);
            IRbasicblock.addInst(tmp);

            IRbasicblock = whileCondBlock;
            IRfunction.addBasicBlock(IRbasicblock);
            it.conditionexpr.trueBlock = whileSuiteBlock;
            it.conditionexpr.falseBlock = whileDestBlock;
            it.conditionexpr.accept(this);
        }

        IRbasicblock = whileSuiteBlock;
        IRfunction.addBasicBlock(IRbasicblock);
        idAddrMap = new IdExprAddrMap(idAddrMap);

        if (it.conditionexpr != null)
            continueStack.push(whileCondBlock);
        breakStack.push(whileDestBlock);

        if (it.whilestmt != null)
            it.whilestmt.accept(this);

        if (it.conditionexpr != null) {
            BranchInst tmp = new BranchInst(IRbasicblock, null, whileCondBlock, null);
            IRbasicblock.addInst(tmp);
        }

        if (it.conditionexpr != null)
            continueStack.pop();
        breakStack.pop();
        idAddrMap = idAddrMap.parentMap;

        IRbasicblock = whileDestBlock;
        IRfunction.addBasicBlock(IRbasicblock);

    }
    @Override public void visit(BreakStmtNode it) {
        BranchInst tmp = new BranchInst(IRbasicblock, null, breakStack.peek(), null);
        IRbasicblock.addInst(tmp);
    }
    @Override public void visit(ContinueStmtNode it) {
        BranchInst tmp = new BranchInst(IRbasicblock, null, continueStack.peek(), null);
        IRbasicblock.addInst(tmp);
    }
    @Override public void visit(ReturnStmtNode it) {
        if (it.value != null) {
            it.value.accept(this);
            MoveInst tmp = new MoveInst(IRbasicblock, IRfunction.retValue, it.value.ExprRet);
            IRbasicblock.addInst(tmp);
        }
        BranchInst tmp1 = new BranchInst(IRbasicblock, null, IRfunction.exit, null);
        IRbasicblock.addInst(tmp1);
    }

    @Override public void visit(AssignExprNode it) {
        it.lhs.accept(this);
        it.rhs.accept(this);
        it.ExprRet = it.rhs.ExprRet;
        StoreInst tmp = new StoreInst(IRbasicblock, it.lhs.ExprLRet, it.rhs.ExprRet);
        IRbasicblock.addInst(tmp);
    }

    @Override public void visit(ArrayExprNode it) {
        it.name.accept(this);
        it.index.accept(this);
        Register regRet= new Register(it.name.ExprRet.IRtype, "getElementPtr" + (regCnt++));
        regRet.needPtr = true;
        ArrayList<Operand> tmpPtrIndex = new ArrayList<>();
        tmpPtrIndex.add(it.index.ExprRet);
        GetElementPtrInst tmp = new GetElementPtrInst(IRbasicblock, regRet, it.name.ExprRet, tmpPtrIndex);
        IRbasicblock.addInst(tmp);

        it.ExprLRet = regRet;
        Register tmpLoadRet = new Register(((PointerType) it.name.ExprRet.IRtype).point, "GEP_Load" + (regCnt++));
        LoadInst tmp1 = new LoadInst(IRbasicblock, tmpLoadRet, regRet);
        IRbasicblock.addInst(tmp1);
        it.ExprRet = tmpLoadRet;
        if (it.trueBlock != null) {
            BranchInst tmp2 = new BranchInst(IRbasicblock, it.ExprRet, it.trueBlock, it.falseBlock);
            IRbasicblock.addInst(tmp2);
        }
    }
    @Override public void visit(BinaryExprNode it) {
        Register regRet = null;
        if ((it.opCode != BinaryExprNode.BinaryOperator.logicand) && (it.opCode != BinaryExprNode.BinaryOperator.logicor)) {
            it.lhs.accept(this);
            it.rhs.accept(this);
        }
        switch (it.opCode) {
            case sub -> {
                regRet = new Register(new IntType(32), "sub" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.sub, regRet);
                IRbasicblock.addInst(tmp);
            }
            case mul -> {
                regRet = new Register(new IntType(32), "mul" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.mul, regRet);
                IRbasicblock.addInst(tmp);
            }
            case div -> {
                regRet = new Register(new IntType(32), "sdiv" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.sdiv, regRet);
                IRbasicblock.addInst(tmp);
            }
            case mod -> {
                regRet = new Register(new IntType(32), "srem" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.srem, regRet);
                IRbasicblock.addInst(tmp);
            }
            case leftshift -> {
                regRet = new Register(new IntType(32), "shl" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.shl, regRet);
                IRbasicblock.addInst(tmp);
            }
            case rightshift -> {
                regRet = new Register(new IntType(32), "ashr" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.ashr, regRet);
                IRbasicblock.addInst(tmp);
            }
            case bitand -> {
                regRet = new Register(new IntType(32), "and" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.and, regRet);
                IRbasicblock.addInst(tmp);
            }
            case bitor -> {
                regRet = new Register(new IntType(32), "or" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.or, regRet);
                IRbasicblock.addInst(tmp);
            }
            case bitxor -> {
                regRet = new Register(new IntType(32), "xor" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.xor, regRet);
                IRbasicblock.addInst(tmp);
            }
            case add -> {
                if (it.lhs.type.typename.equals("int")) {
                    regRet = new Register(new IntType(32), "add" + (regCnt++));
                    BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.add, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new PointerType(new IntType(8)), "add" + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringAdd");
                    ArrayList<Operand> parameters = new ArrayList<>();
                    parameters.add(it.lhs.ExprRet);
                    parameters.add(it.rhs.ExprRet);
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet,parameters);
                    IRbasicblock.addInst(tmp);
                }
            }
            case less -> {
                if (it.lhs.type.typename.equals("int")) {
                    regRet = new Register(new IntType(1), "slt" + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.slt, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), "slt" + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringLess");
                    ArrayList<Operand> parameters = new ArrayList<>();
                    parameters.add(it.lhs.ExprRet);
                    parameters.add(it.rhs.ExprRet);
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet,parameters);
                    IRbasicblock.addInst(tmp);
                }
            }
            case lessequal -> {
                if (it.lhs.type.typename.equals("int")) {
                    regRet = new Register(new IntType(1), "sle" + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.sle, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), "sle" + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringLessEqual");
                    ArrayList<Operand> parameters = new ArrayList<>();
                    parameters.add(it.lhs.ExprRet);
                    parameters.add(it.rhs.ExprRet);
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet,parameters);
                    IRbasicblock.addInst(tmp);
                }
            }
            case greater -> {
                if (it.lhs.type.typename.equals("int")) {
                    regRet = new Register(new IntType(1), "sgt" + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.sgt, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), "sgt" + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringGreat");
                    ArrayList<Operand> parameters = new ArrayList<>();
                    parameters.add(it.lhs.ExprRet);
                    parameters.add(it.rhs.ExprRet);
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet,parameters);
                    IRbasicblock.addInst(tmp);
                }
            }
            case greatequal -> {
                if (it.lhs.type.typename.equals("int")) {
                    regRet = new Register(new IntType(1), "sge" + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.sge, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), "sge" + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringGreatEqual");
                    ArrayList<Operand> parameters = new ArrayList<>();
                    parameters.add(it.lhs.ExprRet);
                    parameters.add(it.rhs.ExprRet);
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet,parameters);
                    IRbasicblock.addInst(tmp);
                }
            }
            case equal -> {
                if (it.lhs.type.typename.equals("int") || it.lhs.type.typename.equals("bool")) {
                    regRet = new Register(new IntType(1), "eq" + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.eq, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), "eq" + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringEqual");
                    ArrayList<Operand> parameters = new ArrayList<>();
                    parameters.add(it.lhs.ExprRet);
                    parameters.add(it.rhs.ExprRet);
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet,parameters);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("null")) {
                    if (it.rhs.type.typename.equals("null"))
                        it.ExprRet = new ConstBool(new IntType(1), true);
                    else {
                        regRet = new Register(new IntType(1), "eq" + (regCnt++));
                        IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.eq, regRet);
                        IRbasicblock.addInst(tmp);
                        it.ExprRet = regRet;
                    }
                }
                else if (it.rhs.type.typename.equals("null")) {
                    regRet = new Register(new IntType(1), "eq" + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.eq, regRet);
                    IRbasicblock.addInst(tmp);
                    it.ExprRet = regRet;
                }
            }
            case notequal -> {
                if (it.lhs.type.typename.equals("int") || it.lhs.type.typename.equals("bool")) {
                    regRet = new Register(new IntType(1), "ne" + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.ne, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), "ne" + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringNotEqual");
                    ArrayList<Operand> parameters = new ArrayList<>();
                    parameters.add(it.lhs.ExprRet);
                    parameters.add(it.rhs.ExprRet);
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet,parameters);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("null")) {
                    if (it.rhs.type.typename.equals("null"))
                        it.ExprRet = new ConstBool(new IntType(1), false);
                    else {
                        regRet = new Register(new IntType(1), "ne" + (regCnt++));
                        IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.ne, regRet);
                        IRbasicblock.addInst(tmp);
                        it.ExprRet = regRet;
                    }
                }
                else if (it.rhs.type.typename.equals("null")) {
                    regRet = new Register(new IntType(1), "ne" + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.ne, regRet);
                    IRbasicblock.addInst(tmp);
                    it.ExprRet = regRet;
                }
            }
            case logicand -> {
                if (it.trueBlock != null) {
                    BasicBlock logicAndBlock = new BasicBlock("logicand_block" + (blockCnt++));
                    logicAndBlock.thisFunction = IRfunction;
                    it.lhs.trueBlock = logicAndBlock;
                    it.lhs.falseBlock = it.falseBlock;
                    it.rhs.trueBlock = it.trueBlock;
                    it.rhs.falseBlock = it.falseBlock;
                    it.lhs.accept(this);
                    IRbasicblock = logicAndBlock;
                    IRfunction.addBasicBlock(IRbasicblock);
                    it.rhs.accept(this);
                }
                else {
                    BasicBlock logicAndBlock = new BasicBlock("logicand_block" + (blockCnt++));
                    logicAndBlock.thisFunction = IRfunction;
                    BasicBlock logicAndDestBlock = new BasicBlock("logicand_dest_blcok" + (blockCnt++));
                    logicAndDestBlock.thisFunction = IRfunction;
                    it.lhs.accept(this);
                    BranchInst tmp = new BranchInst(IRbasicblock, it.lhs.ExprRet, logicAndBlock, logicAndDestBlock);
                    IRbasicblock.addInst(tmp);

                    BasicBlock phiBlock1 = IRbasicblock;
                    IRbasicblock = logicAndBlock;
                    IRfunction.addBasicBlock(IRbasicblock);

                    it.rhs.accept(this);
                    BranchInst tmp1 = new BranchInst(IRbasicblock, null, logicAndDestBlock, null);
                    IRbasicblock.addInst(tmp1);

                    BasicBlock phiBlock2 = IRbasicblock;
                    IRbasicblock = logicAndDestBlock;
                    IRfunction.addBasicBlock(IRbasicblock);

                    regRet = new Register(new IntType(1), "logicAnd" + (regCnt)++);
                    ArrayList<BasicBlock> blocks = new ArrayList<>();
                    ArrayList<Operand> values = new ArrayList<>();
                    blocks.add(phiBlock1);
                    blocks.add(phiBlock2);
                    values.add(new ConstBool(new IntType(1), false));
                    values.add(it.rhs.ExprRet);
                    PhiInst tmp2 = new PhiInst(IRbasicblock, blocks, values, regRet);
                    IRbasicblock.addInst(tmp2);
                    it.ExprRet = regRet;
                }
            }
            case logicor -> {
                if (it.trueBlock != null) {
                    BasicBlock logicOrBlock = new BasicBlock("logicor_block" + (blockCnt++));
                    logicOrBlock.thisFunction = IRfunction;
                    it.lhs.trueBlock = logicOrBlock;
                    it.lhs.falseBlock = it.falseBlock;
                    it.rhs.trueBlock = it.trueBlock;
                    it.rhs.falseBlock = it.falseBlock;
                    it.lhs.accept(this);
                    IRbasicblock = logicOrBlock;
                    IRfunction.addBasicBlock(IRbasicblock);
                    it.rhs.accept(this);
                }
                else {
                    BasicBlock logicOrBlock = new BasicBlock("logicor_block" + (blockCnt++));
                    logicOrBlock.thisFunction = IRfunction;
                    BasicBlock logicOrDestBlock = new BasicBlock("logicor_dest_blcok" + (blockCnt++));
                    logicOrDestBlock.thisFunction = IRfunction;
                    it.lhs.accept(this);
                    BranchInst tmp = new BranchInst(IRbasicblock, it.lhs.ExprRet, logicOrBlock, logicOrDestBlock);
                    IRbasicblock.addInst(tmp);

                    BasicBlock phiBlock1 = IRbasicblock;
                    IRbasicblock = logicOrBlock;
                    IRfunction.addBasicBlock(IRbasicblock);

                    it.rhs.accept(this);
                    BranchInst tmp1 = new BranchInst(IRbasicblock, null, logicOrDestBlock, null);
                    IRbasicblock.addInst(tmp1);

                    BasicBlock phiBlock2 = IRbasicblock;
                    IRbasicblock = logicOrDestBlock;
                    IRfunction.addBasicBlock(IRbasicblock);

                    regRet = new Register(new IntType(1), "logicOr" + (regCnt)++);
                    ArrayList<BasicBlock> blocks = new ArrayList<>();
                    ArrayList<Operand> values = new ArrayList<>();
                    blocks.add(phiBlock1);
                    blocks.add(phiBlock2);
                    values.add(new ConstBool(new IntType(1), true));
                    values.add(it.rhs.ExprRet);
                    PhiInst tmp2 = new PhiInst(IRbasicblock, blocks, values, regRet);
                    IRbasicblock.addInst(tmp2);
                    it.ExprRet = regRet;
                }
            }
        }
        it.ExprRet = regRet;
        if ((it.opCode != BinaryExprNode.BinaryOperator.logicand) && (it.opCode != BinaryExprNode.BinaryOperator.logicor) && it.trueBlock != null) {
            BranchInst tmp = new BranchInst(IRbasicblock, it.ExprRet, it.trueBlock, it.falseBlock);
            IRbasicblock.addInst(tmp);
        }
    }
    @Override public void visit(FuncCallExprNode it) {
        String tmpFuncName;
        boolean inClassFunc = false;
        Function tmpIRFunc = null;

        if (className != null) {
            inClassFunc = true;
            tmpFuncName = className + "." + it.funcname;
            tmpIRFunc = IRmodule.functions.get(tmpFuncName);
        }

        if (tmpIRFunc == null) {
            inClassFunc = false;
            tmpIRFunc = IRmodule.functions.get(it.funcname);
            if (tmpIRFunc == null)
                tmpIRFunc = IRmodule.functions.get("g_" + it.funcname);
        }
        FunctionType tmpFuncType = tmpIRFunc.retType;
        Register regRet = null;
        if (!(tmpFuncType.returnType instanceof VoidType))
            regRet = new Register(tmpFuncType, "funccall" + (regCnt++));
        ArrayList<Operand> parameters = new ArrayList<>();
        if (inClassFunc && className != null) {
            Operand tmpOper = null;
            if (idAddrMap.containsIdAddr("this"))
                tmpOper = idAddrMap.getIdAddr("this");
            if (tmpOper != null) {
                tmpOper.needPtr = true;
                Register tmpCast = new Register(((PointerType) tmpOper.IRtype).point, "casttoret" + (regCnt++));
                tmpCast.needPtr = true;
                LoadInst tmp1 = new LoadInst(IRbasicblock, tmpCast, tmpOper);
                IRbasicblock.addInst(tmp1);
                parameters.add(tmpCast);
            }
        }
        for (ExprNode par: it.parameters) {
            par.accept(this);
            parameters.add(par.ExprRet);
        }

        CallInst tmp = new CallInst(IRbasicblock, tmpIRFunc, regRet, parameters);
        IRbasicblock.addInst(tmp);
        it.ExprRet = regRet;

        if (it.trueBlock != null) {
            BranchInst tmp2 = new BranchInst(IRbasicblock, it.ExprRet, it.trueBlock, it.falseBlock);
            IRbasicblock.addInst(tmp2);
        }
    }
    @Override public void visit(LambdaExprNode it) {}
    @Override public void visit(IdentifierExprNode it) {
        if (idAddrMap != null && idAddrMap.containsIdAddr(it.identifier)) {
            //local
            Operand tmpOper = idAddrMap.getIdAddr(it.identifier);
            if (tmpOper.IRtype instanceof PointerType) {
                IRType tmpType = ((PointerType) tmpOper.IRtype).point;
                Register regRet = new Register(tmpType, "load" + (regCnt++));
                regRet.needPtr = tmpOper.needPtr;
                LoadInst tmp = new LoadInst(IRbasicblock, regRet, tmpOper);
                IRbasicblock.addInst(tmp);
                it.ExprRet = regRet;
                it.ExprLRet = tmpOper;
            }
        }

        if (it.ExprRet == null && className != null) {
            //member
            ClassType tmpClassType = IRmodule.types.get(className);
            IRType tmpMemberType = null;
            int tmpIndex;
            for (tmpIndex = 0; tmpIndex < tmpClassType.memberType.size(); tmpIndex++) {
                if (tmpClassType.memberName.get(tmpIndex).equals(it.identifier)) {
                    tmpMemberType = tmpClassType.memberType.get(tmpIndex);
                    break;
                }
            }
            if (tmpMemberType != null && idAddrMap != null && idAddrMap.containsIdAddr("this")) {
                Operand tmpClassOper = idAddrMap.getIdAddr("this");
                tmpClassOper.needPtr = true;
                if (tmpClassOper.IRtype instanceof PointerType) {
                    IRType tmpType1 = ((PointerType) tmpClassOper.IRtype).point;
                    Register regRet1 = new Register(tmpType1, "class_load" + (regCnt++));
                    LoadInst tmp1 = new LoadInst(IRbasicblock, regRet1, tmpClassOper);
                    IRbasicblock.addInst(tmp1);

                    Register regGEP = new Register(new PointerType(tmpMemberType), "class_GEP" + (regCnt++));
                    regGEP.needPtr = true;
                    ArrayList<Operand> tmpPtr = new ArrayList<>();
                    tmpPtr.add(new ConstInt(new IntType(32), 0));
                    tmpPtr.add(new ConstInt(new IntType(32), tmpIndex));
                    GetElementPtrInst tmpGEPinst = new GetElementPtrInst(IRbasicblock, regGEP, regRet1, tmpPtr);
                    IRbasicblock.addInst(tmpGEPinst);

                    Register regRet2 = new Register(tmpMemberType, "class_GEP_load" + (regCnt++));
                    LoadInst tmp2 = new LoadInst(IRbasicblock, regRet2, regGEP);
                    IRbasicblock.addInst(tmp2);
                    it.ExprRet = regRet2;
                    it.ExprLRet = regGEP;
                }
            }
        }
        if (it.ExprRet == null) {
            //global
            GlobalVariable tmpVar = IRmodule.global.get(it.identifier);
            if (tmpVar != null) {
                Register regRet = new Register(tmpVar.IRtype, tmpVar.name + (regCnt++));
                LoadInst tmp = new LoadInst(IRbasicblock, regRet, tmpVar);
                IRbasicblock.addInst(tmp);
                it.ExprRet = regRet;
                it.ExprLRet = tmpVar;
            }
        }
        if (it.trueBlock != null) {
            BranchInst tmp3 = new BranchInst(IRbasicblock, it.ExprRet, it.trueBlock, it.falseBlock);
            IRbasicblock.addInst(tmp3);
        }
    }
    @Override public void visit(MemberExprNode it) {
        it.expr.accept(this);

        IRType tmpType = IRmodule.getIRType(it.expr.type);
        if (tmpType instanceof PointerType)
            tmpType = ((PointerType) tmpType).point;
        if (tmpType instanceof ClassType) {
            ClassType tmpClassType = (ClassType) tmpType;
            //String memberName = tmpClassType.name + "." + it.member;
            IRType tmpMemberType = null;
            int tmpIndex;
            for (tmpIndex = 0; tmpIndex < tmpClassType.memberType.size(); tmpIndex++) {
                if (tmpClassType.memberName.get(tmpIndex).equals(it.member)) {
                    tmpMemberType = tmpClassType.memberType.get(tmpIndex);
                    break;
                }
            }
            if (tmpMemberType != null) {
                Register regRet = new Register(new PointerType(tmpMemberType), "member_GEP_load" + (regCnt++));
                if (tmpMemberType instanceof PointerType)
                    regRet.needPtr = true;
                ArrayList<Operand> tmpPtr = new ArrayList<>();
                tmpPtr.add(new ConstInt(new IntType(32), 0));
                tmpPtr.add(new ConstInt(new IntType(32), tmpIndex));
                GetElementPtrInst tmpGEPinst = new GetElementPtrInst(IRbasicblock, regRet, it.expr.ExprRet, tmpPtr);
                IRbasicblock.addInst(tmpGEPinst);

                Register regRet1 = new Register(tmpMemberType, "member_GEP_load" + (regCnt++));
                LoadInst tmp = new LoadInst(IRbasicblock, regRet1, regRet);
                IRbasicblock.addInst(tmp);
                it.ExprRet = regRet1;
                it.ExprLRet = regRet;
            }
        }
        if (it.trueBlock != null) {
            BranchInst tmp1 = new BranchInst(IRbasicblock, it.ExprRet, it.trueBlock, it.falseBlock);
            IRbasicblock.addInst(tmp1);
        }

    }
    @Override public void visit(MethodExprNode it) {
        it.expr.accept(this);
        for (ExprNode par: it.parameters)
            par.accept(this);
        TypeNode tmpType = it.expr.type;
        if (tmpType.type.typename == Type.type.STRING && tmpType.dimension == 0) {
            String tmpMethodName = "l_string_" + it.methodname;
            Function tmpMethod = IRmodule.functions.get(tmpMethodName);
            FunctionType tmpMethodType = tmpMethod.retType;
            Register regRet = null;
            if (it.type.type.typename != Type.type.VOID)
                regRet = new Register(tmpMethodType.returnType, "method" + (regCnt++));
            ArrayList<Operand> parameters = new ArrayList<>();
            parameters.add(it.expr.ExprRet);
            for (ExprNode par : it.parameters)
                parameters.add(par.ExprRet);
            CallInst tmp = new CallInst(IRbasicblock, tmpMethod, regRet, parameters);
            IRbasicblock.addInst(tmp);
            it.ExprRet = regRet;
        }
        else if (tmpType.type.typename == Type.type.CLASS && tmpType.dimension == 0) {
            String tmpMethodName = tmpType.typename + "." + it.methodname;
            Function tmpMethod = IRmodule.functions.get(tmpMethodName);
            FunctionType tmpMethodType = tmpMethod.retType;
            Register regRet = null;
            if (it.type.type.typename != Type.type.VOID)
                regRet = new Register(tmpMethodType.returnType, "method" + (regCnt++));
            ArrayList<Operand> parameters = new ArrayList<>();
            parameters.add(it.expr.ExprRet);
            for (ExprNode par : it.parameters)
                parameters.add(par.ExprRet);
            CallInst tmp = new CallInst(IRbasicblock, tmpMethod, regRet, parameters);
            IRbasicblock.addInst(tmp);
            it.ExprRet = regRet;
        }
        else if (it.methodname.equals("size") && tmpType.dimension > 0) {
            Operand tmpFuncRet = it.expr.ExprRet;
            Register regRet = new Register(new PointerType(new IntType(32)), "array_size" + (regCnt++));
            regRet.needPtr = true;
            ArrayList<Operand> tmpPtr = new ArrayList<>();
            tmpPtr.add(new ConstInt(new IntType(32), -1));
            GetElementPtrInst tmpGEPinst = new GetElementPtrInst(IRbasicblock, regRet, tmpFuncRet, tmpPtr);
            IRbasicblock.addInst(tmpGEPinst);

            Register regRet1 = new Register(new IntType(32), "array_size_load" + (regCnt++));
            LoadInst tmp = new LoadInst(IRbasicblock, regRet1, regRet);
            IRbasicblock.addInst(tmp);
            it.ExprRet = regRet1;
        }

        if (it.trueBlock != null) {
            BranchInst tmp1 = new BranchInst(IRbasicblock, it.ExprRet, it.trueBlock, it.falseBlock);
            IRbasicblock.addInst(tmp1);
        }

    }

    public Register MallocArray (IRType type, int dim, NewExprNode it) {
        Register tmpSiz1 = new Register(new IntType(32), "new_size1" + (regCnt++));
        Register tmpSiz2 = new Register(new IntType(32), "new_size2" + (regCnt++));
        BinaryOpInst tmp1 = new BinaryOpInst(IRbasicblock, it.arraysize.get(dim).ExprRet, new ConstInt(new IntType(32), 4), BinaryOpInst.BinaryOp.mul, tmpSiz1);
        BinaryOpInst tmp2 = new BinaryOpInst(IRbasicblock, it.arraysize.get(dim).ExprRet, new ConstInt(new IntType(32), 4), BinaryOpInst.BinaryOp.add, tmpSiz2);
        IRbasicblock.addInst(tmp1);
        IRbasicblock.addInst(tmp2);

        Register callReg = new Register(new PointerType(new IntType(32)), "call_malloc" + (regCnt++));
        Function tmpFunc = IRmodule.functions.get("g_malloc");
        ArrayList<Operand> parameters = new ArrayList<>();
        parameters.add(tmpSiz2);
        CallInst tmp3 = new CallInst(IRbasicblock, tmpFunc, callReg, parameters);
        IRbasicblock.addInst(tmp3);

        callReg.needPtr = true;
        StoreInst tmp4 = new StoreInst(IRbasicblock, callReg, it.arraysize.get(dim).ExprRet);
        IRbasicblock.addInst(tmp4);

        Register headReg = new Register(new PointerType(new IntType(32)), "array_head" + (regCnt++));
        ArrayList<Operand> tmpPtr = new ArrayList<>();
        tmpPtr.add(new ConstInt(new IntType(32), 1));
        GetElementPtrInst tmpGEPinst = new GetElementPtrInst(IRbasicblock, headReg, callReg, tmpPtr);
        IRbasicblock.addInst(tmpGEPinst);
        Register arrayAddr = new Register(type, "array_head_cast" + (regCnt++));
        BitCastToInst tmp5 = new BitCastToInst(IRbasicblock, headReg, arrayAddr, type);
        IRbasicblock.addInst(tmp5);

        if (dim < it.arraysize.size() - 1) {
            BasicBlock condBlock = new BasicBlock("cond_block" + (blockCnt++));
            condBlock.thisFunction = IRfunction;
            BasicBlock bodyBlock = new BasicBlock("body_block" + (blockCnt++));
            bodyBlock.thisFunction = IRfunction;
            BasicBlock destBlock = new BasicBlock("dest_block" + (blockCnt++));
            destBlock.thisFunction = IRfunction;

            Register curReg = new Register(type, "subarray_cur" + (regCnt++));
            ArrayList<Operand> tmpPtr1 = new ArrayList<>();
            tmpPtr1.add(new ConstInt(new IntType(32), 1));
            GetElementPtrInst tmpGEP1 = new GetElementPtrInst(IRbasicblock, curReg, callReg, tmpPtr1);
            IRbasicblock.addInst(tmpGEP1);

            Register endReg = new Register(type, "subarray_end" + (regCnt++));
            ArrayList<Operand> tmpPtr2 = new ArrayList<>();
            tmpPtr2.add(it.arraysize.get(dim).ExprRet);
            GetElementPtrInst tmpGEP2 = new GetElementPtrInst(IRbasicblock, endReg, arrayAddr, tmpPtr2);
            IRbasicblock.addInst(tmpGEP2);
            BranchInst tmp6 = new BranchInst(IRbasicblock, null, condBlock, null);
            IRbasicblock.addInst(tmp6);
            IRbasicblock = condBlock;
            IRfunction.addBasicBlock(IRbasicblock);

            Register condReg = new Register(new IntType(1), "subarray_cond" + (regCnt++));
            IcmpInst tmp7 = new IcmpInst(IRbasicblock, curReg, endReg, IcmpInst.IcmpOp.slt, condReg);
            IRbasicblock.addInst(tmp7);
            BranchInst tmp8 = new BranchInst(IRbasicblock, condReg, bodyBlock, destBlock);
            IRbasicblock.addInst(tmp8);
            IRbasicblock = bodyBlock;
            IRfunction.addBasicBlock(bodyBlock);

            if (type instanceof PointerType) {
                Register subArrayAddr = MallocArray(((PointerType)type).point, dim + 1, it);
                curReg.needPtr = true;
                StoreInst tmp9 = new StoreInst(IRbasicblock, curReg, subArrayAddr);
                IRbasicblock.addInst(tmp9);
            }

            Register regRet = new Register(type, "incr_reg" + (regCnt++));
            ArrayList<Operand> tmpPtr3 = new ArrayList<>();
            tmpPtr3.add(new ConstInt(new IntType(32), 1));
            GetElementPtrInst tmpGEP3 = new GetElementPtrInst(IRbasicblock, regRet, curReg, tmpPtr3);
            IRbasicblock.addInst(tmpGEP3);

            MoveInst tmp10 = new MoveInst(IRbasicblock, curReg, regRet);
            IRbasicblock.addInst(tmp10);
            BranchInst tmp11 = new BranchInst(IRbasicblock, null, condBlock, null);
            IRbasicblock.addInst(tmp11);
            IRbasicblock = destBlock;
            IRfunction.addBasicBlock(destBlock);
        }
        return arrayAddr;
    }
    @Override public void visit(NewExprNode it) {
        if (it.dimension == 0) {
            Register newClassRet = new Register(new PointerType(new IntType(32)), "new_class" + (regCnt++));
            Function tmpFunc = IRmodule.functions.get("g_malloc");
            ArrayList<Operand> parameters = new ArrayList<>();
            parameters.add(new ConstInt(new IntType(32), IRmodule.types.get(it.type.typename).size()));
            CallInst tmp = new CallInst(IRbasicblock, tmpFunc, newClassRet, parameters);
            IRbasicblock.addInst(tmp);

            Register castRet = new Register(IRmodule.getIRType(it.type), "class_cast" + (regCnt++));
            BitCastToInst tmp1 = new BitCastToInst(IRbasicblock, newClassRet, castRet, IRmodule.getIRType(it.type));
            IRbasicblock.addInst(tmp1);
            it.ExprRet = castRet;
            it.ExprRet.needPtr = true;

            String tmpClassName = it.type.typename;
            if (IRmodule.functions.containsKey(tmpClassName + "." + tmpClassName)) {
                Function tmpCons = IRmodule.functions.get(tmpClassName + "." + tmpClassName);
                ArrayList<Operand> parameters1 = new ArrayList<>();
                parameters1.add(castRet);
                CallInst tmp2 = new CallInst(IRbasicblock, tmpCons, null, parameters1);
                IRbasicblock.addInst(tmp2);
            }
        }
        else {
            TypeNode tmpTypeNode = it.type;
            tmpTypeNode.dimension = 0;
            IRType tmpIRType = IRmodule.getIRType(tmpTypeNode);
            for (int i = 0; i < it.dimension; ++i)
                tmpIRType = new PointerType(tmpIRType);
            for (int i = 0; i < it.arraysize.size(); ++i)
                it.arraysize.get(i).accept(this);
            it.ExprRet = MallocArray(tmpIRType, 0, it);
        }

    }
    @Override public void visit(PrefixExprNode it) {
        it.expr.accept(this);
        switch (it.opCode) {
            case  prefixadd -> {
                Register regRet = new Register(new IntType(32), "prefixadd" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.expr.ExprRet, new ConstInt(new IntType(32), 1), BinaryOpInst.BinaryOp.add, regRet);
                IRbasicblock.addInst(tmp);
                StoreInst tmp1 = new StoreInst(IRbasicblock, it.expr.ExprLRet, regRet);
                IRbasicblock.addInst(tmp1);
                it.ExprRet = regRet;
                it.ExprLRet = it.expr.ExprLRet;
            }
            case prefixsub -> {
                Register regRet = new Register(new IntType(32), "prefixsub" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.expr.ExprRet, new ConstInt(new IntType(32), 1), BinaryOpInst.BinaryOp.sub, regRet);
                IRbasicblock.addInst(tmp);
                StoreInst tmp1 = new StoreInst(IRbasicblock, it.expr.ExprLRet, regRet);
                IRbasicblock.addInst(tmp1);
                it.ExprRet = regRet;
                it.ExprLRet = it.expr.ExprLRet;
            }
            case positive -> {
                it.ExprRet = it.expr.ExprRet;
            }
            case negative -> {
                if (it.expr.ExprRet instanceof ConstInt) {
                    it.ExprRet = new ConstInt(new IntType(32), -((ConstInt)it.expr.ExprRet).value);
                }
                else if (!(it.expr.ExprRet instanceof ConstBool) && !(it.expr.ExprRet instanceof ConstString) && !(it.expr.ExprRet instanceof ConstNull)) {
                    Register regRet = new Register(new IntType(32), "negative" + (regCnt++));
                    BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, new ConstInt(new IntType(32), 0), it.expr.ExprRet, BinaryOpInst.BinaryOp.sub, regRet);
                    IRbasicblock.addInst(tmp);
                    it.ExprRet = regRet;
                }
            }
            case logicnot -> {
                if (it.trueBlock != null) {
                    it.expr.trueBlock = it.falseBlock;
                    it.expr.falseBlock = it.trueBlock;
                }
                else {
                    Register regRet = new Register(new IntType(1), "logicnot" + (regCnt++));
                    BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, new ConstBool(new IntType(1), true), it.expr.ExprRet, BinaryOpInst.BinaryOp.xor, regRet);
                    IRbasicblock.addInst(tmp);
                    it.ExprRet = regRet;
                }
            }
            case bitnot -> {
                Register regRet = new Register(new IntType(1), "bitnot" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, new ConstInt(new IntType(32), -1), it.expr.ExprRet, BinaryOpInst.BinaryOp.xor, regRet);
                IRbasicblock.addInst(tmp);
                it.ExprRet = regRet;
            }


        }
    }
    @Override public void visit(SuffixExprNode it) {
        it.expr.accept(this);
        switch (it.opCode) {
            case suffixadd -> {
                Register regRet = new Register(new IntType(32), "suffixadd" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.expr.ExprRet, new ConstInt(new IntType(32), 1), BinaryOpInst.BinaryOp.add, regRet);
                IRbasicblock.addInst(tmp);
                StoreInst tmp1 = new StoreInst(IRbasicblock, it.expr.ExprLRet, regRet);
                IRbasicblock.addInst(tmp1);
                it.ExprRet = it.expr.ExprRet;
            }
            case suffixsub -> {
                Register regRet = new Register(new IntType(32), "suffixsub" + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.expr.ExprRet, new ConstInt(new IntType(32), 1), BinaryOpInst.BinaryOp.sub, regRet);
                IRbasicblock.addInst(tmp);
                StoreInst tmp1 = new StoreInst(IRbasicblock, it.expr.ExprLRet, regRet);
                IRbasicblock.addInst(tmp1);
                it.ExprRet = it.expr.ExprRet;
            }
        }
    }
    @Override public void visit(ThisExprNode it) {
        Operand tmpThisPtr = idAddrMap.getIdAddr("this");
        if (tmpThisPtr.IRtype instanceof PointerType) {
            Register regRet = new Register(((PointerType) tmpThisPtr.IRtype).point, "casttoret" + (regCnt++));
            tmpThisPtr.needPtr = true;
            regRet.needPtr = true;
            LoadInst tmp = new LoadInst(IRbasicblock, regRet, tmpThisPtr);
            IRbasicblock.addInst(tmp);
            it.ExprRet = regRet;
        }
    }

    @Override public void visit(VarDeclStmtNode it) {
        it.type.accept(this);
        it.varlist.accept(this);
    }
    @Override public void visit(IntLiteralNode it) {
        it.ExprRet = new ConstInt(new IntType(32), it.value);
    }
    @Override public void visit(BoolLiteralNode it) {
        it.ExprRet = new ConstBool(new IntType(1), it.value);
        if (it.trueBlock != null) {
            BranchInst tmp = new BranchInst(IRbasicblock, it.ExprRet, it.trueBlock, it.falseBlock);
            IRbasicblock.addInst(tmp);
        }
    }
    @Override public void visit(StringLiteralNode it) {
        ConstString tmp;
        String tmpString = it.value;
        tmpString = tmpString.replace("\\n", "\n");
        tmpString = tmpString.replace("\\t", "\t");
        tmpString = tmpString.replace("\\\"", "\\");
        tmpString = tmpString.replace("\\\\", "\\");
        if (IRmodule.constStrings.containsKey(tmpString))
            tmp =  IRmodule.constStrings.get(tmpString);
        else {
            IRType tmpType = new ArrayType(tmpString.length(), new IntType(8));
            ConstString stringRet = new ConstString(tmpType, tmpString);
            IRmodule.constStrings.put(tmpString, stringRet);
            tmp = stringRet;
        }
        Register regRet = new Register(new PointerType(new IntType(8)), "ConstString" + (regCnt++));
        ArrayList<Operand> tmpPtr = new ArrayList<>();
        tmpPtr.add(new ConstInt(new IntType(32), 0));
        tmpPtr.add(new ConstInt(new IntType(32), 0));
        GetElementPtrInst tmpGEPinst = new GetElementPtrInst(IRbasicblock, regRet, tmp, tmpPtr);
        IRbasicblock.addInst(tmpGEPinst);
        it.ExprRet = regRet;
    }
    @Override public void visit(NullLiteralNode it) {
        it.ExprRet = new ConstNull(new VoidType());
    }


}