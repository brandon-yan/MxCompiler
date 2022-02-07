package Assembly;

import Assembly.Operand.RVAddrImm;
import Assembly.Operand.RVPhyReg;
import Assembly.Operand.RVRegister;
import MIR.Function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class RVFunction {

    public String name;
    public Function IRFunction;
    public boolean builtin;
    public HashMap<RVRegister, RVAddrImm> GEPAddrMap = new HashMap<>();

    public RVBasicBlock entry;
    public RVBasicBlock exit;

    public int stackCnt;
    public int stackCnting;

    public int graphColorStackNum;
    public int maxParaCall;
    public HashSet<RVPhyReg> usedPhyReg = new HashSet<>();


    public RVFunction(Function IRFunction) {
        this.IRFunction = IRFunction;
        this.name = IRFunction.name;
        this.builtin = IRFunction.builtin;
        stackCnt = 0;
        stackCnting = 0;
        graphColorStackNum = 0;
        maxParaCall = 0;
    }

    public void  addBlock (RVBasicBlock block) {
        if (entry == null)
            entry = block;
        else
            exit.nextBlock = block;
        exit = block;
    }

    public int stackSize() {
        return 4 * stackCnting + (16 - (4 * stackCnting % 16) + 4 * 16);
    }

    public int graphColorStackSize() {
        return 4 * graphColorStackNum + (16 - (4 * graphColorStackNum % 16)) + 4 * 16 + 4 * maxParaCall;
    }

    @Override
    public String toString() {
        return name;
    }
}