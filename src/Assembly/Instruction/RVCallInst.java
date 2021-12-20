package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

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
    public String toString() {
        return "call " + callFunc.toString();
    }

}