package Assembly.Instruction;

import Assembly.Operand.RVGloReg;
import Assembly.Operand.RVVirReg;
import Assembly.Operand.RVImm;
import Assembly.Operand.RVRegister;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

public class RVLaInst extends RVInstruction {

    public RVRegister rd, addr;

    public RVLaInst(RVRegister rd, RVRegister addr) {
        super();
        this.rd = rd;
        this.addr = addr;

        if (this.rd instanceof RVGloReg || this.rd instanceof RVVirReg)
            usedVirReg.add(this.rd);

    }



}