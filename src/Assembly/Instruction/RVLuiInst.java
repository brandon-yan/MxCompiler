package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

public class RVLuiInst extends RVInstruction {

    public RVRegister rd;
    public RVReloImm relocationImm;

    public RVLuiInst(RVRegister rd, RVReloImm relocationImm) {
        super();
        this.rd = rd;
        this.relocationImm = relocationImm;

        if (this.rd instanceof RVGloReg || this.rd instanceof RVVirReg)
            usedVirReg.add(this.rd);

    }
    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
        if(rd != null && rd == reg1)
            rd = reg2;
    }

    @Override
    public String toString() {
        return "lui " + rd.toString() + "," + relocationImm.toString();
    }

}