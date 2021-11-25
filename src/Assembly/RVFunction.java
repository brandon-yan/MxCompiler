package Assembly;

import Assembly.Operand.RVAddrImm;
import Assembly.Operand.RVRegister;
import MIR.Function;

import java.util.HashMap;

public class RVFunction {

    public String name;
    public Function IRFunction;
    public boolean builtin;
    public HashMap<RVRegister, RVAddrImm> GEPAddrMap = new HashMap<>();
    public int manxParaCall;

    public RVBasicBlock entry;
    public RVBasicBlock exit;

    public int stackCnt;
    public int stackCnting;


    public RVFunction(Function IRFunction) {
        this.IRFunction = IRFunction;
        this.name = IRFunction.name;
        this.builtin = IRFunction.builtin;
        manxParaCall = 0;
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
}