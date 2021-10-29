package MIR.Instruction;

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
}