package MIR.Instruction;

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
    }
}