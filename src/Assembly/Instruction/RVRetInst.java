package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class RVRetInst extends RVInstruction {

    public RVRetInst() {
        super();
    }

    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
    }

    @Override
    public void replaceUse(RVRegister reg1, RVRegister reg2) {
    }

    @Override
    public String toString() {
        return "ret";
    }

    @Override
    public LinkedHashSet<RVRegister> use() {
        LinkedHashSet<RVRegister> use = new LinkedHashSet<>();
        return use;
    }

    @Override
    public LinkedHashSet<RVRegister> def() {
        LinkedHashSet<RVRegister> def = new LinkedHashSet<>();
        return def;
    }
}