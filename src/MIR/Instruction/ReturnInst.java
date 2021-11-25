package MIR.Instruction;

import Backend.IRVisitor;
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

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        if (type instanceof VoidType)
            return "return void";
        else
            return "return" + " " + type.toString() + " " + value.toString();
    }
}