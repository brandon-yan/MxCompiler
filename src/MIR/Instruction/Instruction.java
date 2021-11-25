package MIR.Instruction;

import Backend.IRVisitor;
import MIR.BasicBlock;
import MIR.Operand.*;

abstract public class Instruction {
    public BasicBlock bblock;
    public Instruction prior = null, next = null;
    public boolean hasSideEffect = false;

    public Instruction(BasicBlock bblock) {
        this.bblock = bblock;
    }

    abstract public void accept(IRVisitor it);
}
