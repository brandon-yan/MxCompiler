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
        if (this.retVal != null)
            this.retVal.defs.add(this);
    }

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        if (retVal != null)
            tmp.append(retVal.toString()).append(" = ");
        tmp.append("call ").append(func.retType.returnType.toString()).append(" ");
        tmp.append("@").append(func.name).append("(");
        for (int i = 0; i < parameters.size(); ++i) {
            if (i > 0) tmp.append(", ");
            tmp.append(parameters.get(i).IRtype.toString()).append(" ").append(parameters.get(i).toString());
        }
        tmp.append(")");
        return tmp.toString();
    }
}