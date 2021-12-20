package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

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
    public String toString() {
        return "j " + destBlock.toString();
    }

}