package MIR.Instruction;

import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class ParellelCopyInst extends Instruction{
    public Operand oper;
    public ParellelCopyInst(BasicBlock block, Operand oper) {
        super(block);
        this.oper = oper;
    }
}