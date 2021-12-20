package Assembly;

import Assembly.Instruction.RVInstruction;
import Assembly.Operand.RVRegister;
import MIR.BasicBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RVBasicBlock {

    public String name;
    public BasicBlock IRBBlock;
    public RVBasicBlock nextBlock = null;
    public RVInstruction head = null;
    public RVInstruction tail = null;

    public ArrayList<RVBasicBlock> predecessor = new ArrayList<>();
    public ArrayList<RVBasicBlock> successor = new ArrayList<>();

    public HashSet<RVRegister> liveOut = new HashSet<>();
    public HashSet<RVRegister> liveIn = new HashSet<>();



    public RVBasicBlock(String name, BasicBlock IRBBlock) {
        this.name = name;
        this.IRBBlock = IRBBlock;
    }

    public void addInst(RVInstruction inst) {
        if (head == null)
            head = inst;
        else {
            inst.prior = tail;
            tail.next = inst;
        }
        tail = inst;
    }

    @Override
    public String toString() {
        return name;
    }
}