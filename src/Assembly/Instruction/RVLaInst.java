package Assembly.Instruction;

import Assembly.Operand.*;
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

    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
        if(rd != null && rd == reg1)
            rd = reg2;
        if(addr != null && addr == reg1)
            addr = reg2;
    }

    @Override
    public String toString() {
        return "la " + rd.toString() + "," + addr.toString();
    }

}