package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

public class RVRetInst extends RVInstruction {

    public RVRetInst() {
        super();
    }

    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
    }

    @Override
    public String toString() {
        return "ret";
    }
}