package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;
import Assembly.RVModule;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class RVCallInst extends RVInstruction {

    public RVFunction callFunc;

    public RVCallInst(RVFunction callFunc) {
        super();
        this.callFunc = callFunc;

    }

    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
    }

    @Override
    public void replaceUse(RVRegister reg1, RVRegister reg2) {
    }

    @Override
    public String toString() {
        return "call " + callFunc.toString();
    }

    @Override
    public LinkedHashSet<RVRegister> use() {
        LinkedHashSet<RVRegister> use = new LinkedHashSet<>();
        for (int i = 0; i < Integer.min(8, callFunc.IRFunction.parameters.size()); ++i)
            use.add(RVModule.getPhyReg("a" + i));
        return use;
    }

    @Override
    public LinkedHashSet<RVRegister> def() {
        LinkedHashSet<RVRegister> def = new LinkedHashSet<>();
        for(String tmpName : RVModule.RVCallerPhyRegName)
            def.add(RVModule.getPhyReg(tmpName));
        return def;
    }
}