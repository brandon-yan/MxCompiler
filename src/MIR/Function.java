package MIR;

import MIR.Instruction.*;
import MIR.Operand.*;
import MIR.TypeSystem.IRType;

import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

public class Function {
    public String name;
    public IRType retType;
    public Operand retValue;
    public boolean builtin = false;

    public ArrayList<Parameter> parameters = new ArrayList<>();
    public ArrayList<BasicBlock> blocks = new ArrayList<>();

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

}