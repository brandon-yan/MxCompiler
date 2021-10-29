package MIR.Instruction;

import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class ReturnInst extends Instruction{
    public IRType type;
    public Operand value;
    public ReturnInst(BasicBlock block, IRType type, Operand value) {
        super(block);
        this.type = type;
        this.value = value;
    }
}