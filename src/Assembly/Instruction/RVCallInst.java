package Assembly.Instruction;

import Assembly.Operand.RVGloReg;
import Assembly.Operand.RVVirReg;
import Assembly.Operand.RVImm;
import Assembly.Operand.RVRegister;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

public class RVCallInst extends RVInstruction {

    public RVFunction callFunc;

    public RVCallInst(RVFunction callFunc) {
        super();
        this.callFunc = callFunc;

    }


}