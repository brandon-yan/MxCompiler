package MIR.Instruction;

import Backend.IRVisitor;
import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class StoreInst extends Instruction{
    public Operand address;
    public Operand value;
    public StoreInst(BasicBlock block, Operand address, Operand value) {
        super(block);
        this.address = address;
        this.value = value;
    }

    public void accept(IRVisitor it){
        it.visit(this);
    }

    public String toString() {
        if (address.IRtype instanceof PointerType) {
            return "store " + ((PointerType)address.IRtype).point.toString() + " " + value.toString() + " " + address.IRtype.toString() + " " + address.toString();
        }
        else
            return "store not ptr " + address.IRtype.toString();
    }
}