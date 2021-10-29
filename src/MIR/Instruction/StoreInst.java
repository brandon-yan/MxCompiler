package MIR.Instruction;

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
}