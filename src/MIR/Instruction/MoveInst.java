package MIR.Instruction;

import Backend.IRVisitor;
import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class MoveInst extends Instruction{
    public Operand operandRd;
    public Operand operandRs;
    public MoveInst(BasicBlock block, Operand operandRd, Operand operandRs) {
        super(block);
        this.operandRd = operandRd;
        this.operandRs = operandRs;
        this.operandRd.defs.add(this);
    }

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        return operandRd.toString() + " move " + operandRs.toString();
    }
}