package MIR.Instruction;

import Backend.IRVisitor;
import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class BitCastToInst extends Instruction{
    public Operand oper;
    public Register regRet;
    public IRType bitcastType;

    public BitCastToInst(BasicBlock block, Operand oper, Register regRet, IRType type) {
        super(block);
        this.oper = oper;
        this.regRet = regRet;
        this.bitcastType = type;
    }

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        return regRet.toString() + " = bitcast " + oper.IRtype.toString() + " " + oper.toString() + " to" + bitcastType.toString();
    }
}