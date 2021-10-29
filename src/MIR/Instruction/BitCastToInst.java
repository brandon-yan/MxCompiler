package MIR.Instruction;

import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class BitCastToInst extends Instruction{
    public Operand oper;

    public BitCastToInst(BasicBlock block, Operand oper) {
        super(block);
        this.oper = oper;
    }
}