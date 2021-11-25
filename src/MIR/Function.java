package MIR;

import Backend.IRVisitor;
import MIR.Instruction.*;
import MIR.Operand.*;
import MIR.TypeSystem.FunctionType;
import MIR.TypeSystem.IRType;

import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

public class Function {
    public String name;
    public FunctionType retType;
    public Operand retValue;
    public boolean builtin = false;

    public ArrayList<Parameter> parameters = new ArrayList<>();

    public BasicBlock entry;
    public BasicBlock exit;

    public Function(String name) {
        this.name = name;
        entry = new BasicBlock(name + ".entry");
        exit = new BasicBlock(name + ".exit");
        entry.nexts = exit;
        exit.priors = entry;
    }


    public void addParameter(Parameter para) {
        parameters.add(para);
    }

    public void addBasicBlock(BasicBlock bblock) {
        BasicBlock tmpPrior = exit.priors;
        tmpPrior.nexts = bblock;
        bblock.priors = tmpPrior;
        bblock.nexts = exit;
        exit.priors = bblock;
    }

    public void accept(IRVisitor it){
        it.visit(this);
    }


    public String toString() {
        StringBuilder tmp = new StringBuilder("define ");
        tmp.append(retType.toString()).append(" @").append(name).append("(");
        for (int i = 0; i < parameters.size(); ++i) {
            if (i > 0) tmp.append(", ");
            tmp.append(parameters.get(i).IRtype.toString());
        }
        tmp.append(")");
        return tmp.toString();
    }

}