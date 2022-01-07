package Backend;

import Assembly.Instruction.*;
import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;
import Assembly.RVModule;
import MIR.BasicBlock;
import MIR.Function;
import MIR.Instruction.*;
import MIR.Module;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import static Assembly.RVModule.max_imm;
import static Assembly.RVModule.min_imm;

public class InstSelector implements IRVisitor{

    public Module IRmodule;
    public RVModule RVmodule;
    public RVFunction RVfunction;
    public RVBasicBlock RVbasicblock;
    public InstSelector (Module IRmodule, RVModule RVmodule) {
        this.IRmodule = IRmodule;
        this.RVmodule = RVmodule;
        this.RVfunction = null;
        this.RVbasicblock = null;
    }

    @Override
    public void visit(Module it) {
        for (var tmpVar: it.global.values())
            RVmodule.addGloReg(tmpVar);
        for (var tmpString: it.constStrings.values())
            RVmodule.addString(tmpString);
        for (var tmpFunc: it.functions.values())
            RVmodule.addFunc(tmpFunc);
        for (var tmpFunc: it.functions.values())
            if (!tmpFunc.builtin)
                tmpFunc.accept(this);
    }
    @Override
    public void visit(Function it) {
        RVfunction = RVmodule.RVFuncMap.get(it);
        RVbasicblock = RVmodule.getRVBasicBlock(null);
        RVfunction.addBlock(RVbasicblock);
        RVJumpInst tmp = new RVJumpInst(RVmodule.getRVBasicBlock(it.entry));
        RVbasicblock.addInst(tmp);
        RVbasicblock = RVmodule.getRVBasicBlock(it.entry);

        for (int i = 0; i < Integer.min(8, it.parameters.size()); ++i) {
            RVRegister rd = RVmodule.getRVRegister(it.parameters.get(i), RVbasicblock);
            RVMoveInst tmp1 = new RVMoveInst(rd, RVmodule.getPhyReg("a" + i));
            RVbasicblock.addInst(tmp1);
        }

        int offset = 0;
        for (int i = 8; i < it.parameters.size(); ++i) {
            RVRegister rd = RVmodule.getRVRegister(it.parameters.get(i), RVbasicblock);
            RVLInst tmp2 = new RVLInst(rd, RVmodule.getPhyReg("s0"), new RVImm(offset));
            RVbasicblock.addInst(tmp2);
            offset += 4;
        }

        BasicBlock tmpBBlock = it.entry;
        while (tmpBBlock != null) {
            tmpBBlock.accept(this);
            tmpBBlock = tmpBBlock.nexts;
        }
        RVfunction = null;
        RVbasicblock = null;
    }
    @Override
    public void visit(BasicBlock it) {
        RVbasicblock = RVmodule.getRVBasicBlock(it);
        RVfunction.addBlock(RVbasicblock);
        Instruction tmpInst = it.head;
        while (tmpInst != null) {
            tmpInst.accept(this);
            tmpInst = tmpInst.next;
        }
    }

    @Override
    public void visit(AllocateInst it) {}
    @Override
    public void visit(BinaryOpInst it) {
        RVRegister rs1 = null, rs2 = null;
        RVRegister rd = RVmodule.getRVRegister(it.regRet, RVbasicblock);
        RVImm imm = null;
        rs1 = RVmodule.getRVRegister(it.lhs, RVbasicblock);
        switch (it.opCode) {
            case add -> {
                if (it.rhs instanceof ConstInt && ((ConstInt)it.rhs).value >= min_imm && ((ConstInt)it.rhs).value <= max_imm) {
                    int tmpVal = ((ConstInt)it.rhs).value;
                    RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, rd, rs1, null, new RVImm(tmpVal));
                    RVbasicblock.addInst(tmp);
                }
                else {
                    rs2 = RVmodule.getRVRegister(it.rhs, RVbasicblock);
                    RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, rd, rs1, rs2, null);
                    RVbasicblock.addInst(tmp);
                }
            }
            case sub -> {
                if (it.rhs instanceof ConstInt && ((ConstInt)it.rhs).value >= min_imm && ((ConstInt)it.rhs).value <= max_imm) {
                    int tmpVal = -((ConstInt)it.rhs).value;
                    RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, rd, rs1, null, new RVImm(tmpVal));
                    RVbasicblock.addInst(tmp);
                }
                else {
                    rs2 = RVmodule.getRVRegister(it.rhs, RVbasicblock);
                    RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.sub, rd, rs1, rs2, null);
                    RVbasicblock.addInst(tmp);
                }
            }
            case mul -> {
                rs2 = RVmodule.getRVRegister(it.rhs, RVbasicblock);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.mul, rd, rs1, rs2, null);
                RVbasicblock.addInst(tmp);
            }
            case sdiv -> {
                rs2 = RVmodule.getRVRegister(it.rhs, RVbasicblock);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.div, rd, rs1, rs2, null);
                RVbasicblock.addInst(tmp);
            }
            case srem -> {
                rs2 = RVmodule.getRVRegister(it.rhs, RVbasicblock);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.rem, rd, rs1, rs2, null);
                RVbasicblock.addInst(tmp);
            }
            case shl -> {
                if (it.rhs instanceof ConstInt && ((ConstInt)it.rhs).value >= min_imm && ((ConstInt)it.rhs).value <= max_imm)
                    imm = new RVImm(((ConstInt)it.rhs).value);
                else
                    rs2 = RVmodule.getRVRegister(it.rhs, RVbasicblock);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.sll, rd, rs1, rs2, imm);
                RVbasicblock.addInst(tmp);
            }
            case ashr -> {
                if (it.rhs instanceof ConstInt && ((ConstInt)it.rhs).value >= min_imm && ((ConstInt)it.rhs).value <= max_imm)
                    imm = new RVImm(((ConstInt)it.rhs).value);
                else
                    rs2 = RVmodule.getRVRegister(it.rhs, RVbasicblock);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.sra, rd, rs1, rs2, imm);
                RVbasicblock.addInst(tmp);
            }
            case and -> {
                if (it.rhs instanceof ConstInt && ((ConstInt)it.rhs).value >= min_imm && ((ConstInt)it.rhs).value <= max_imm)
                    imm = new RVImm(((ConstInt)it.rhs).value);
                else if (it.rhs instanceof ConstBool)
                    imm = new RVImm(((ConstBool)it.rhs).value ? 1 : 0);
                else
                    rs2 = RVmodule.getRVRegister(it.rhs, RVbasicblock);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.and, rd, rs1, rs2, imm);
                RVbasicblock.addInst(tmp);
            }
            case or -> {
                if (it.rhs instanceof ConstInt && ((ConstInt)it.rhs).value >= min_imm && ((ConstInt)it.rhs).value <= max_imm)
                    imm = new RVImm(((ConstInt)it.rhs).value);
                else if (it.rhs instanceof ConstBool)
                    imm = new RVImm(((ConstBool)it.rhs).value ? 1 : 0);
                else
                    rs2 = RVmodule.getRVRegister(it.rhs, RVbasicblock);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.or, rd, rs1, rs2, imm);
                RVbasicblock.addInst(tmp);
            }
            case xor -> {
                if (it.rhs instanceof ConstInt && ((ConstInt)it.rhs).value >= min_imm && ((ConstInt)it.rhs).value <= max_imm)
                    imm = new RVImm(((ConstInt)it.rhs).value);
                else if (it.rhs instanceof ConstBool)
                    imm = new RVImm(((ConstBool)it.rhs).value ? 1 : 0);
                else
                    rs2 = RVmodule.getRVRegister(it.rhs, RVbasicblock);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.xor, rd, rs1, rs2, imm);
                RVbasicblock.addInst(tmp);
            }
        }
    }
    @Override
    public void visit(BitCastToInst it) {
        RVRegister rd = RVmodule.getRVRegister(it.regRet, RVbasicblock);
        RVRegister rs1 = RVmodule.getRVRegister(it.oper, RVbasicblock);
        RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, rd, rs1, null, new RVImm(0));
        RVbasicblock.addInst(tmp);
    }
    @Override
    public void visit(BranchInst it) {
        if (it.condition == null) {
            RVBasicBlock jumpBlock = RVmodule.getRVBasicBlock(it.trueblock);
            RVJumpInst tmp = new RVJumpInst(jumpBlock);
            RVbasicblock.addInst(tmp);
        }
        else {
            RVBasicBlock trueBlock = RVmodule.getRVBasicBlock(it.trueblock);
            RVBasicBlock falseBlock = RVmodule.getRVBasicBlock(it.falseblock);
            if (it.condition.defs.size() == 1 && it.condition.defs.iterator().next() instanceof IcmpInst) {
                IcmpInst tmpInst = (IcmpInst) it.condition.defs.iterator().next();
                RVRegister lhsReg = RVmodule.getRVRegister(tmpInst.lhs, RVbasicblock);
                RVRegister rhsReg = RVmodule.getRVRegister(tmpInst.rhs, RVbasicblock);
                switch (tmpInst.opCode) {
                    case eq -> {
                        RVBranchInst tmp = new RVBranchInst(RVInstruction.RVCmpType.eq, lhsReg, rhsReg, trueBlock, falseBlock);
                        RVbasicblock.addInst(tmp);
                    }
                    case ne -> {
                        RVBranchInst tmp = new RVBranchInst(RVInstruction.RVCmpType.ne, lhsReg, rhsReg, trueBlock, falseBlock);
                        RVbasicblock.addInst(tmp);
                    }
                    case slt -> {
                        RVBranchInst tmp = new RVBranchInst(RVInstruction.RVCmpType.lt, lhsReg, rhsReg, trueBlock, falseBlock);
                        RVbasicblock.addInst(tmp);
                    }
                    case sle -> {
                        RVBranchInst tmp = new RVBranchInst(RVInstruction.RVCmpType.le, lhsReg, rhsReg, trueBlock, falseBlock);
                        RVbasicblock.addInst(tmp);
                    }
                    case sgt -> {
                        RVBranchInst tmp = new RVBranchInst(RVInstruction.RVCmpType.gt, lhsReg, rhsReg, trueBlock, falseBlock);
                        RVbasicblock.addInst(tmp);
                    }
                    case sge -> {
                        RVBranchInst tmp = new RVBranchInst(RVInstruction.RVCmpType.ge, lhsReg, rhsReg, trueBlock, falseBlock);
                        RVbasicblock.addInst(tmp);
                    }
                }
            }
            else {
                RVRegister tmpReg = RVmodule.getRVRegister(it.condition, RVbasicblock);
                RVBranchInst tmp = new RVBranchInst(RVInstruction.RVCmpType.ne, tmpReg, null, trueBlock, falseBlock);
                RVbasicblock.addInst(tmp);
            }
        }
    }
    @Override
    public void visit(CallInst it) {
        for (int i = 0; i < Integer.min(8, it.parameters.size()); ++i) {
            RVRegister rs1 = RVmodule.getRVRegister(it.parameters.get(i), RVbasicblock);
            RVMoveInst tmp = new RVMoveInst(RVmodule.getPhyReg("a" + i), rs1);
            RVbasicblock.addInst(tmp);
        }

        int offset = 0;
        for (int i = 8; i < it.parameters.size(); ++i) {
            RVRegister rd = RVmodule.getRVRegister(it.parameters.get(i), RVbasicblock);
            RVLInst tmp = new RVLInst(rd, RVmodule.getPhyReg("sp"), new RVImm(offset));
            RVbasicblock.addInst(tmp);
            offset += 4;
        }

        RVCallInst tmp = new RVCallInst(RVmodule.RVFuncMap.get(it.func));
        RVbasicblock.addInst(tmp);
        if (it.func.retValue != null) {
            RVRegister rd = RVmodule.getRVRegister(it.retVal, RVbasicblock);
            RVMoveInst tmp1 = new RVMoveInst(rd, RVmodule.getPhyReg("a0"));
            RVbasicblock.addInst(tmp1);
        }
    }
    @Override
    public void visit(GetElementPtrInst it) {
        if (it.pointer instanceof GlobalVariable) {
            RVRegister rd = RVmodule.getRVRegister(it.ptrRet, RVbasicblock);
            RVLaInst tmp = new RVLaInst(rd, RVmodule.gloRegMap.get(it.pointer));
            RVbasicblock.addInst(tmp);
        }
        else if (it.pointer instanceof ConstString) {
            RVRegister rd = RVmodule.getRVRegister(it.ptrRet, RVbasicblock);
            RVGloReg rs = null;
            if (RVmodule.gloRegMap.containsKey(it.pointer))
                rs = RVmodule.gloRegMap.get(it.pointer);
            if (rs != null) {
                RVLaInst tmp = new RVLaInst(rd, rs);
                RVbasicblock.addInst(tmp);
            }
        }
        else if (it.pointer.IRtype instanceof PointerType) {
            RVRegister rd = RVmodule.getRVRegister(it.ptrRet, RVbasicblock);
            RVRegister baseReg = RVmodule.getRVRegister(it.pointer, RVbasicblock);
            if (it.ptrIndex.size() == 1) {
                //array
                Operand index = it.ptrIndex.get(0);
                if (index instanceof ConstInt) {
                    int value = ((ConstInt) index).value * 4;
                    if (value <= max_imm && value >= min_imm) {
                        RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, rd, baseReg, null, new RVImm(value));
                        RVbasicblock.addInst(tmp);
                    }
                    else {
                        RVRegister tmprs2 = RVmodule.getRVRegister(new ConstInt(new IntType(32), value), RVbasicblock);
                        RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, rd, baseReg, tmprs2, null);
                        RVbasicblock.addInst(tmp);
                    }
                }
                else if (index instanceof Register || index instanceof Parameter) {
                    RVRegister indexReg = RVmodule.getRVRegister(index, RVbasicblock);
                    RVRegister tmpReg = new RVVirReg(RVModule.virRegCnt++);
                    RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.sll, tmpReg, indexReg, null, new RVImm(2));
                    RVbasicblock.addInst(tmp);
                    RVBinaryOpInst tmp1 = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, rd, baseReg, tmpReg, null);
                    RVbasicblock.addInst(tmp1);
                }
            }
            else if (((PointerType)it.pointer.IRtype).point instanceof ClassType) {
                //class
                ClassType tmpClassType = (ClassType) ((PointerType)it.pointer.IRtype).point;
                int index = ((ConstInt) it.ptrIndex.get(1)).value;
                int offset = tmpClassType.offset(index);
                if (!(tmpClassType.memberType.get(index) instanceof PointerType)) {
                    RVAddrImm tmpImm = new RVAddrImm(offset, baseReg);
                    RVfunction.GEPAddrMap.put(rd, tmpImm);
                }
                else {
                    if (offset <= max_imm && offset >= min_imm) {
                        RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, rd, baseReg, null, new RVImm(offset));
                        RVbasicblock.addInst(tmp);
                    }
                    else {
                        RVRegister tmprs2 = RVmodule.getRVRegister(new ConstInt(new IntType(32), offset), RVbasicblock);
                        RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, rd, baseReg, tmprs2, null);
                        RVbasicblock.addInst(tmp);
                    }
                }
            }
        }
        else {
            //other things
            RVRegister rd = RVmodule.getRVRegister(it.ptrRet, RVbasicblock);
            RVGloReg rs = null;
            if (RVmodule.gloRegMap.containsKey(it.pointer))
                rs = RVmodule.gloRegMap.get(it.pointer);
            if (rs != null) {
                RVLaInst tmp = new RVLaInst(rd, rs);
                RVbasicblock.addInst(tmp);
            }
        }
    }
    @Override
    public void visit(IcmpInst it) {
        if (it.next instanceof BranchInst)
            return;
        RVRegister rd = RVmodule.getRVRegister(it.regRet, RVbasicblock);
        RVRegister rs1 = RVmodule.getRVRegister(it.lhs, RVbasicblock);
        RVRegister rs2 = RVmodule.getRVRegister(it.rhs, RVbasicblock);
        switch (it.opCode) {
            case eq -> {
                RVVirReg tmpReg = new RVVirReg(RVModule.virRegCnt++);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.xor, tmpReg, rs1, rs2, null);
                RVbasicblock.addInst(tmp);
                RVSetzInst tmp1 = new RVSetzInst(RVInstruction.RVCmpType.eq, rd, tmpReg);
                RVbasicblock.addInst(tmp1);
            }
            case ne -> {
                RVVirReg tmpReg = new RVVirReg(RVModule.virRegCnt++);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.xor, tmpReg, rs1, rs2, null);
                RVbasicblock.addInst(tmp);
                RVSetzInst tmp1 = new RVSetzInst(RVInstruction.RVCmpType.ne, rd, tmpReg);
                RVbasicblock.addInst(tmp1);
            }
            case slt -> {
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.slt, rd, rs1, rs2, null);
                RVbasicblock.addInst(tmp);
            }
            case sle -> {
                RVVirReg tmpReg = new RVVirReg(RVModule.virRegCnt++);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.slt, rd, rs2, rs1, null);
                RVbasicblock.addInst(tmp);
                RVBinaryOpInst tmp1 = new RVBinaryOpInst(RVInstruction.RVBinaryType.xor, rd, tmpReg, null, new RVImm(1));
                RVbasicblock.addInst(tmp1);
            }
            case sgt -> {
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.slt, rd, rs2, rs1, null);
                RVbasicblock.addInst(tmp);
            }
            case sge -> {
                RVVirReg tmpReg = new RVVirReg(RVModule.virRegCnt++);
                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.slt, rd, rs1, rs2, null);
                RVbasicblock.addInst(tmp);
                RVBinaryOpInst tmp1 = new RVBinaryOpInst(RVInstruction.RVBinaryType.xor, rd, tmpReg, null, new RVImm(1));
                RVbasicblock.addInst(tmp1);
            }
        }
    }
    @Override
    public void visit(LoadInst it) {
        RVRegister rd = RVmodule.getRVRegister(it.regRet, RVbasicblock);
        RVRegister rs = RVmodule.getRVRegister(it.address, RVbasicblock);
        if (RVfunction.GEPAddrMap.containsKey(rs)) {
            RVAddrImm tmpAddrImm = RVfunction.GEPAddrMap.get(rs);
            RVLInst tmp = new RVLInst(rd, tmpAddrImm.baseReg, tmpAddrImm);
            RVbasicblock.addInst(tmp);
        }
        else if (it.address.needPtr) {
            RVLInst tmp = new RVLInst(rd, rs, new RVAddrImm(0, rs));
            RVbasicblock.addInst(tmp);
        }
        else if (rs instanceof RVGloReg) {
            RVVirReg tmpVirReg = new RVVirReg(RVModule.virRegCnt++);
            RVReloImm tmpImm = new RVReloImm((RVGloReg) rs, RVReloImm.RelocationType.hi);
            RVLuiInst tmp = new RVLuiInst(tmpVirReg, tmpImm);
            RVbasicblock.addInst(tmp);
            RVReloImm tmpImm1 = new RVReloImm((RVGloReg) rs, RVReloImm.RelocationType.lo);
            RVLInst tmp1 = new RVLInst(rd, tmpVirReg, tmpImm1);
            RVbasicblock.addInst(tmp1);
        }
        else {
            RVMoveInst tmp = new RVMoveInst(rd, rs);
            RVbasicblock.addInst(tmp);
        }
    }
    @Override
    public void visit(MoveInst it) {
        RVRegister rd = RVmodule.getRVRegister(it.operandRd, RVbasicblock);
        RVRegister rs = RVmodule.getRVRegister(it.operandRs, RVbasicblock);
        RVMoveInst tmp = new RVMoveInst(rd, rs);
        RVbasicblock.addInst(tmp);
    }
    @Override
    public void visit(PhiInst it) {

    }
    @Override
    public void visit(ReturnInst it) {
        if (it.value != null) {
            RVRegister tmpRetReg = RVmodule.getRVRegister(it.value, RVbasicblock);
            RVMoveInst tmp = new RVMoveInst(RVmodule.getPhyReg("a0"), tmpRetReg);
            RVbasicblock.addInst(tmp);
        }
        RVRetInst tmp1 = new RVRetInst();
        RVbasicblock.addInst(tmp1);
    }
    @Override
    public void visit(StoreInst it) {
        RVRegister value = RVmodule.getRVRegister(it.value, RVbasicblock);
        RVRegister addr = RVmodule.getRVRegister(it.address, RVbasicblock);
        if (RVfunction.GEPAddrMap.containsKey(addr)) {
            RVAddrImm tmpAddrImm = RVfunction.GEPAddrMap.get(addr);
            RVSInst tmp = new RVSInst(value, tmpAddrImm.baseReg, tmpAddrImm);
            RVbasicblock.addInst(tmp);
        }
        else if (it.address.needPtr) {
            RVSInst tmp = new RVSInst(value, addr, new RVAddrImm(0, addr));
            RVbasicblock.addInst(tmp);
        }
        else if (addr instanceof RVGloReg) {
            RVVirReg tmpVirReg = new RVVirReg(RVModule.virRegCnt++);
            RVReloImm tmpImm = new RVReloImm((RVGloReg) addr, RVReloImm.RelocationType.hi);
            RVLuiInst tmp = new RVLuiInst(tmpVirReg, tmpImm);
            RVbasicblock.addInst(tmp);
            RVReloImm tmpImm1 = new RVReloImm((RVGloReg) addr, RVReloImm.RelocationType.lo);
            RVSInst tmp1 = new RVSInst(value, tmpVirReg, tmpImm1);
            RVbasicblock.addInst(tmp1);
        }
        else {
            RVMoveInst tmp = new RVMoveInst(addr, value);
            RVbasicblock.addInst(tmp);
        }
    }
}