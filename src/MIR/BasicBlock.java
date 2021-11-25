package MIR;

import Backend.IRVisitor;
import MIR.Instruction.*;
import MIR.Operand.*;

import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

public class BasicBlock {

    public String name;

    public BasicBlock priors = null;
    public BasicBlock nexts = null;
    public Function thisFunction = null;

    public Instruction head = null;
    public Instruction tail = null;

    public BasicBlock(String name) {
        this.name = name;
    }

    public void addInst(Instruction inst) {
        if (head == null)
            head = inst;
        else {
            inst.prior = tail;
            tail.next = inst;
        }
        tail = inst;
    }

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        return "%" + name;
    }

}