package MIR.Instruction;

import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.HashSet;
import java.util.Spliterator;

public class BinaryOpInst extends Instruction{
    public enum BinaryOp {
        add, sub, mul, sdiv, srem,     //+, -, *, /, %
        shl, ashr, and, or, xor        //<<, >>, &, |, ^
    }

    public BinaryOp opCode;
    public Operand lhs, rhs;
    public Register regRet;
    public BinaryOpInst(BasicBlock block, Operand lhs, Operand rhs, BinaryOp op, Register regRet) {
        super(block);
        this.lhs = lhs;
        this.rhs = rhs;
        this.opCode = op;
        this.regRet = regRet;
    }
}