package Assembly;

import Assembly.Operand.RVAddrImm;
import Assembly.Operand.RVRegister;
import MIR.Function;

import java.util.ArrayList;
import java.util.HashMap;

public class RVFunction {

    public String name;
    public Function IRFunction;
    public boolean builtin;
    public HashMap<RVRegister, RVAddrImm> GEPAddrMap = new HashMap<>();
    public int maxParaCall;

    public RVBasicBlock entry;
    public RVBasicBlock exit;

    public int stackCnt;
    public int stackCnting;

    public ArrayList<RVBasicBlock> DFSorder = new ArrayList<>();


    public RVFunction(Function IRFunction) {
        this.IRFunction = IRFunction;
        this.name = IRFunction.name;
        this.builtin = IRFunction.builtin;
        maxParaCall = 0;
        stackCnt = 0;
        stackCnting = 0;
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

    public void getDFS() {
        DFSorder.clear();
        runDFS(entry);
    }

    public void runDFS(RVBasicBlock block) {
        if(DFSorder.contains(block))
            return;
        DFSorder.add(block);
        for (int i = block.successor.size() - 1; i >= 0; --i)
            runDFS(block.successor.get(i));
    }

    @Override
    public String toString() {
        return name;
    }
}