package MIR.Instruction;

import Backend.IRVisitor;
import MIR.BasicBlock;
import MIR.Function;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.ArrayList;
import java.util.HashSet;

public class CallInst extends Instruction{
    public Function func;
    public Register retVal;
    public ArrayList<Operand> parameters;

    public CallInst(BasicBlock block, Function func, Register retVal, ArrayList<Operand> parameters) {
        super(block);
        this.func = func;;
        this.retVal = retVal;
        this.parameters = parameters;
    }

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        if (retVal != null)
            tmp.append(retVal.toString()).append(" = ");
        tmp.append("call ").append(func.toString());
        return tmp.toString();
    }
}