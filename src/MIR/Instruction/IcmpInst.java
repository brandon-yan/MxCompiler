package MIR.Instruction;

import Backend.IRVisitor;
import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class IcmpInst extends Instruction{
    public enum IcmpOp {
        eq, ne, slt, sgt, sle, sge
    }
    public IcmpOp opCode;
    public Operand lhs, rhs;
    public Register regRet;
    public IcmpInst(BasicBlock block, Operand lhs, Operand rhs, IcmpOp op, Register regRet) {
        super(block);
        this.opCode = op;
        this.lhs = lhs;
        this.rhs = rhs;
        this.regRet = regRet;
    }

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        return regRet.toString() + " = icmp " + opCode.toString() + " " + regRet.IRtype.toString() + " " + lhs.toString() + " " + rhs.toString();
    }
}