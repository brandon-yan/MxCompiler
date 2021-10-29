package MIR.Instruction;

import MIR.BasicBlock;
import MIR.Operand.*;

abstract public class Instruction {
    public BasicBlock bblock;
    public Instruction prior = null, next = null;
    public boolean hasSideEffect = false;

    public Instruction(BasicBlock bblock) {
        this.bblock = bblock;
    }

    public void replaceInst(Instruction inst) {

    }

    public void removeInst(Instruction inst) {

    }
}
