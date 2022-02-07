package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;
import Assembly.RVModule;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class RVJumpInst extends RVInstruction {

    public RVBasicBlock destBlock;

    public RVJumpInst(RVBasicBlock destBlock) {
        super();
        this.destBlock = destBlock;

    }

    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
    }

    @Override
    public void replaceUse(RVRegister reg1, RVRegister reg2) {
    }

    @Override
    public String toString() {
        return "j " + destBlock.toString();
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