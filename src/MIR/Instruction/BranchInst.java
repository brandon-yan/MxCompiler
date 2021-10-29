package MIR.Instruction;

import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class BranchInst extends Instruction{
    public Operand condition;
    public BasicBlock trueblock;
    public BasicBlock falseblock;

    public BranchInst(BasicBlock block, Operand condition, BasicBlock trueblock, BasicBlock falseblock) {
        super(block);
        this.condition = condition;
        this.trueblock = trueblock;
        this.falseblock = falseblock;
    }
}