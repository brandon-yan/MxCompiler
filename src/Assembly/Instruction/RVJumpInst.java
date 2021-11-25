package Assembly.Instruction;

import Assembly.Operand.RVGloReg;
import Assembly.Operand.RVVirReg;
import Assembly.Operand.RVImm;
import Assembly.Operand.RVRegister;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

public class RVJumpInst extends RVInstruction {

    public RVBasicBlock destBlock;

    public RVJumpInst(RVBasicBlock destBlock) {
        super();
        this.destBlock = destBlock;

    }


}