package MIR;

import MIR.Instruction.*;
import MIR.Operand.*;

import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

public class BasicBlock {

    public String name;
    public boolean terminated = false;
    public int loopcnt = 0;

    public BasicBlock priors = null;
    public BasicBlock nexts = null;
    public Function thisFunction = null;

    public Instruction head = null;
    public Instruction tail = null;

    public HashMap<String, Operand> registers = new HashMap<>();

    public BasicBlock(String name) {
        this.name = name;
    }

    public void addInst(Instruction inst) {
        if (head == null) {
            head = inst;
            tail = inst;
        }
        else {
            inst.prior = tail;
            tail.next = inst;
            tail = inst;
        }
    }


}