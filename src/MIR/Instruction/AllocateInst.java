package MIR.Instruction;

import Backend.IRVisitor;
import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class AllocateInst extends Instruction{

    public IRType type;
    public Operand value;

    public AllocateInst(BasicBlock block, IRType type, Operand value) {
        super(block);
        this.type = type;
        this.value = value;

    }

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        return value.toString() + " " + "allocate" + " " + type.toString();
    }
}