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
//        Type stringType = new Type("string");
//        Function func = new Function("l_string_length");
//        func.retType = Module.i32T;
//        func.addParameter(new Parameter(Module.stringT, "str"));
//        IRmodule.builtinFunctions.put("l_string_length", func);
//
//        func = new Function("l_string_substring");
//        func.retType = Module.stringT;
//        func.addParameter(new Parameter(Module.stringT, "str"));
//        func.addParameter(new Parameter(Module.i32T, "left"));
//        func.addParameter(new Parameter(Module.i32T, "right"));
//        IRmodule.builtinFunctions.put("l_string_substring", func);
//
//        func = new Function("l_string_parseInt");
//        func.retType = Module.i32T;
//        func.addParameter(new Parameter(Module.stringT, "str"));
//        IRmodule.builtinFunctions.put("l_string_parseInt", func);
//
//        func = new Function("l_string_ord");
//        func.retType = Module.i32T;
//        func.addParameter(new Parameter(Module.stringT, "str"));
//        func.addParameter(new Parameter(Module.i32T, "pos"));
//        IRmodule.builtinFunctions.put("l_string_ord", func);

        for (ProgramDeclNode section: it.sectionList) {
            if (section instanceof ClassDeclNode) {
                ClassDeclNode tmp = (ClassDeclNode) section;
                ClassType tmpIRtype = new ClassType(tmp.identifier);
                IRmodule.types.put(tmp.identifier, tmpIRtype);
            }
        }
        for (ProgramDeclNode section: it.sectionList) {
            if (section instanceof ClassDeclNode ) {
                ClassDeclNode tmpClassNode = (ClassDeclNode) section;
                ClassType tmpIRtype = IRmodule.types.get(tmpClassNode.identifier);

                for (VarDeclNode tmpVarDecl: tmpClassNode.Varlist) {
                    VarListNode tmpVarList = tmpVarDecl.varlist;
                    for (VarNode tmpVar: tmpVarList.Varlist) {
                        tmpIRtype.addMember(IRmodule.getIRType(tmpVar.type), tmpVar.name);
                    }
                }
                IRmodule.types.replace(tmpClassNode.identifier, tmpIRtype);

                FunctionType tmpFuncType = new FunctionType(new VoidType());
                if (tmpClassNode.Constructor != null)
                    tmpFuncType.parameters.add(tmpIRtype);
                String tmpFuncName = tmpClassNode.identifier + "." + tmpClassNode.identifier;

                Function tmpIRfunction = new Function(tmpFuncName);
                tmpIRfunction.retType = tmpFuncType;
                if (tmpClassNode.Constructor == null) tmpIRfunction.builtin = true;
                else {
                    Parameter tmpClassPtr = new Parameter(new PointerType(tmpIRtype), "this");
                    tmpIRfunction.addParameter(tmpClassPtr);
                }
                IRmodule.functions.put(tmpFuncName, tmpIRfunction);
            }
        }

        for (ProgramDeclNode section: it.sectionList) {
            if (section instanceof FuncDeclNode) {
                FuncDeclNode tmpFuncNode = (FuncDeclNode) section;
                FunctionType tmpFuncType = new FunctionType(IRmodule.getIRType(tmpFuncNode.type));
                VarListNode tmpParList = tmpFuncNode.parameterlist;
                for (VarNode tmpPar: tmpParList.Varlist)
                    tmpFuncType.parameters.add(IRmodule.getIRType(tmpPar.type));
                String tmpFuncName = tmpFuncNode.identifier;
                Function tmpIRfunction = new Function(tmpFuncName);
                tmpIRfunction.retType = tmpFuncType;
                for (VarNode tmpPar: tmpParList.Varlist) {
                    Parameter tmpParameter = new Parameter(IRmodule.getIRType(tmpPar.type), tmpPar.name);
                    tmpIRfunction.addParameter(tmpParameter);
                }
                IRmodule.functions.put(tmpFuncName, tmpIRfunction);
            }
        }

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
        }
        for (ProgramDeclNode section: it.sectionList) {
            if (section instanceof FuncDeclNode)
                section.accept(this);
        }

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

            AllocateInst tmp = new AllocateInst(IRbasicblock, new PointerType(tmpParType), tmpReg);
            IRbasicblock.addInst(tmp);
            StoreInst tmp1 = new StoreInst(IRbasicblock, tmpReg, tmpPar);
            IRbasicblock.addInst(tmp1);
            idAddrMap.addIdAddr(tmpPar.name, tmpReg);
        }
        IRfunction.retValue = new Register(new PointerType(IRmodule.getIRType(it.type)), tmpFunName + "ret_val" + (regCnt++));

        if (it.identifier.equals("main")) {
            Function mainFunc = IRmodule.functions.get("g_init");
            CallInst tmp = new CallInst(IRbasicblock, mainFunc, null);
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
        if (ifFalseBlock == null)
            it.conditionexpr.falseBlock = ifDestBlock;
        else it.conditionexpr.falseBlock = ifFalseBlock;
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
        if (it.increaseexpr != null) {
            tmp = new BranchInst(IRbasicblock, null, forIncrBlock, null);
            IRbasicblock.addInst(tmp);
        }
        else {
            if (it.conditionexpr != null) {
                tmp = new BranchInst(IRbasicblock, null, forCondBlock, null);
                IRbasicblock.addInst(tmp);
            }
            else {
                tmp = new BranchInst(IRbasicblock, null, forDestBlock, null);
                IRbasicblock.addInst(tmp);
            }
        }
        idAddrMap = idAddrMap.parentMap;

        if (it.increaseexpr != null) {
            idAddrMap = new IdExprAddrMap(idAddrMap);
            IRbasicblock = forIncrBlock;
            IRfunction.addBasicBlock(IRbasicblock);
            it.increaseexpr.accept(this);
            BranchInst tmp1;
            if (it.conditionexpr != null) {
                tmp1 = new BranchInst(IRbasicblock, null, forCondBlock, null);
                IRbasicblock.addInst(tmp1);
            }
            else {
                tmp1 = new BranchInst(IRbasicblock, null, forSuiteBlock, null);
                IRbasicblock.addInst(tmp1);
            }
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
        BranchInst tmp;
        if (it.conditionexpr != null) {
            tmp = new BranchInst(IRbasicblock, null, whileCondBlock, null);
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
        StoreInst tmp = new StoreInst(IRbasicblock, it.rhs.ExprRet, it.lhs.ExprRet);
        IRbasicblock.addInst(tmp);
    }

    @Override public void visit(ArrayExprNode it) {
        it.name.accept(this);
        it.index.accept(this);
        Register regRet= new Register(it.name.ExprRet.IRtype, "getElementPtr" + (regCnt++));
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
        String regName;
        Register regRet = null;
        if ((it.opCode != BinaryExprNode.BinaryOperator.logicand) && (it.opCode != BinaryExprNode.BinaryOperator.logicor)) {
            it.lhs.accept(this);
            it.rhs.accept(this);
        }
        switch (it.opCode) {
            case sub -> {
                regName = "sub";
                regRet = new Register(new IntType(32), regName + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.sub, regRet);
                IRbasicblock.addInst(tmp);
            }
            case mul -> {
                regName = "mul";
                regRet = new Register(new IntType(32), regName + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.mul, regRet);
                IRbasicblock.addInst(tmp);
            }
            case div -> {
                regName = "sdiv";
                regRet = new Register(new IntType(32), regName + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.sdiv, regRet);
                IRbasicblock.addInst(tmp);
            }
            case mod -> {
                regName = "srem";
                regRet = new Register(new IntType(32), regName + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.srem, regRet);
                IRbasicblock.addInst(tmp);
            }
            case leftshift -> {
                regName = "shl";
                regRet = new Register(new IntType(32), regName + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.shl, regRet);
                IRbasicblock.addInst(tmp);
            }
            case rightshift -> {
                regName = "ashr";
                regRet = new Register(new IntType(32), regName + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.ashr, regRet);
                IRbasicblock.addInst(tmp);
            }
            case bitand -> {
                regName = "and";
                regRet = new Register(new IntType(32), regName + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.and, regRet);
                IRbasicblock.addInst(tmp);
            }
            case bitor -> {
                regName = "or";
                regRet = new Register(new IntType(32), regName + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.or, regRet);
                IRbasicblock.addInst(tmp);
            }
            case bitxor -> {
                regName = "xor";
                regRet = new Register(new IntType(32), regName + (regCnt++));
                BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.xor, regRet);
                IRbasicblock.addInst(tmp);
            }
            case add -> {
                regName = "add";
                if (it.lhs.type.typename.equals("int")) {
                    regRet = new Register(new IntType(32), regName + (regCnt++));
                    BinaryOpInst tmp = new BinaryOpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, BinaryOpInst.BinaryOp.add, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new PointerType(new IntType(8)), regName + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringAdd");
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet);
                    tmp.parameters.add(it.lhs.ExprRet);
                    tmp.parameters.add(it.rhs.ExprRet);
                    IRbasicblock.addInst(tmp);
                }
            }
            case less -> {
                regName = "slt";
                if (it.lhs.type.typename.equals("int")) {
                    regRet = new Register(new IntType(1), regName + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.slt, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), regName + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringLess");
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet);
                    tmp.parameters.add(it.lhs.ExprRet);
                    tmp.parameters.add(it.rhs.ExprRet);
                    IRbasicblock.addInst(tmp);
                }
            }
            case lessequal -> {
                regName = "sle";
                if (it.lhs.type.typename.equals("int")) {
                    regRet = new Register(new IntType(1), regName + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.sle, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), regName + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringLessEqual");
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet);
                    tmp.parameters.add(it.lhs.ExprRet);
                    tmp.parameters.add(it.rhs.ExprRet);
                    IRbasicblock.addInst(tmp);
                }
            }
            case greater -> {
                regName = "sgt";
                if (it.lhs.type.typename.equals("int")) {
                    regRet = new Register(new IntType(1), regName + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.sgt, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), regName + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringGreat");
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet);
                    tmp.parameters.add(it.lhs.ExprRet);
                    tmp.parameters.add(it.rhs.ExprRet);
                    IRbasicblock.addInst(tmp);
                }
            }
            case greatequal -> {
                regName = "sge";
                if (it.lhs.type.typename.equals("int")) {
                    regRet = new Register(new IntType(1), regName + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.sge, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), regName + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringGreatEqual");
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet);
                    tmp.parameters.add(it.lhs.ExprRet);
                    tmp.parameters.add(it.rhs.ExprRet);
                    IRbasicblock.addInst(tmp);
                }
            }
            case equal -> {
                regName = "eq";
                if (it.lhs.type.typename.equals("int") || it.lhs.type.typename.equals("bool")) {
                    regRet = new Register(new IntType(1), regName + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.eq, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), regName + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringEqual");
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet);
                    tmp.parameters.add(it.lhs.ExprRet);
                    tmp.parameters.add(it.rhs.ExprRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("null")) {
                    if (it.rhs.type.typename.equals("null"))
                        it.ExprRet = new ConstBool(new IntType(1), true);
                    else {
                        regRet = new Register(new IntType(1), regName + (regCnt++));
                        IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.eq, regRet);
                        IRbasicblock.addInst(tmp);
                        it.ExprRet = regRet;
                    }
                }
                else if (it.rhs.type.typename.equals("null")) {
                    regRet = new Register(new IntType(1), regName + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.eq, regRet);
                    IRbasicblock.addInst(tmp);
                    it.ExprRet = regRet;
                }
            }
            case notequal -> {
                regName = "ne";
                if (it.lhs.type.typename.equals("int") || it.lhs.type.typename.equals("bool")) {
                    regRet = new Register(new IntType(1), regName + (regCnt++));
                    IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.ne, regRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("string")) {
                    regRet= new Register(new IntType(1), regName + (regCnt++));
                    Function tmpFunc = IRmodule.functions.get("g_stringNotEqual");
                    CallInst tmp = new CallInst(IRbasicblock, tmpFunc, regRet);
                    tmp.parameters.add(it.lhs.ExprRet);
                    tmp.parameters.add(it.rhs.ExprRet);
                    IRbasicblock.addInst(tmp);
                }
                else if (it.lhs.type.typename.equals("null")) {
                    if (it.rhs.type.typename.equals("null"))
                        it.ExprRet = new ConstBool(new IntType(1), false);
                    else {
                        regRet = new Register(new IntType(1), regName + (regCnt++));
                        IcmpInst tmp = new IcmpInst(IRbasicblock, it.lhs.ExprRet, it.rhs.ExprRet, IcmpInst.IcmpOp.ne, regRet);
                        IRbasicblock.addInst(tmp);
                        it.ExprRet = regRet;
                    }
                }
                else if (it.rhs.type.typename.equals("null")) {
                    regRet = new Register(new IntType(1), regName + (regCnt++));
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

                it.ExprRet = regRet;
                if ((it.opCode != BinaryExprNode.BinaryOperator.logicand) && (it.opCode != BinaryExprNode.BinaryOperator.logicor) && it.trueBlock != null) {
                    BranchInst tmp = new BranchInst(IRbasicblock, it.ExprRet, it.trueBlock, it.falseBlock);
                    IRbasicblock.addInst(tmp);
                }
            }

        }
    }
    @Override public void visit(FuncCallExprNode it) {}
    @Override public void visit(LambdaExprNode it) {}
    @Override public void visit(IdentifierExprNode it) {}
    @Override public void visit(MemberExprNode it) {}
    @Override public void visit(MethodExprNode it) {}

    public Register MallocArray (IRType type, int dimension, NewExprNode it) {
        return null;
    }
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