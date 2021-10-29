package MIR.Instruction;

import MIR.BasicBlock;
import MIR.Function;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.ArrayList;
import java.util.HashSet;

public class CallInst extends Instruction{
    public Function func;
    public Register retVal;
    public ArrayList<Operand> parameters = new ArrayList<>();

    public CallInst(BasicBlock block, Function func, Register retVal) {
        super(block);
        this.func = func;;
        this.retVal = retVal;
    }
}