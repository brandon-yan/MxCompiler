package MIR.Instruction;

import Backend.IRVisitor;
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

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        if (condition == null)
            return "jump " + trueblock.toString();
        else {
            return "branch " + condition.toString() + " true: " + trueblock.toString() + " false: " + falseblock.toString();
        }
    }
}