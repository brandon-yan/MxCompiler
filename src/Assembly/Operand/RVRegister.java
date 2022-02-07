package Assembly.Operand;

import Assembly.Instruction.RVMoveInst;

import java.util.HashSet;

public class RVRegister extends RVOperand {

    public String name;
    public boolean needLoad = true;

    public HashSet<RVRegister> adjlist = new HashSet<>();
    public HashSet<RVMoveInst> movelist = new HashSet<>();
    public double weight = 0.0;
    public int degree = 0;
    public RVRegister alias = null;
    public RVPhyReg color = null;
    public int stackoffset = -1;

    public RVRegister(String name) {
        this.name = name;
        if (this instanceof RVPhyReg)
            color = (RVPhyReg) this;
    }

    public void clear() {
        adjlist.clear();
        movelist.clear();
        weight = 0.0;
        degree = 0;
        alias = null;
        color = null;
    }

    public void preColor() {
        adjlist.clear();
        movelist.clear();
        alias = null;
        color = (RVPhyReg)this;
        degree = 2147483647;
    }

}