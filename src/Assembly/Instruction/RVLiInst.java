package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

public class RVLiInst extends RVInstruction {

    public RVRegister rd;
    public RVImm imm;

    public RVLiInst(RVRegister rd, RVImm imm) {
        super();
        this.rd = rd;
        this.imm = imm;

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
        return "li " + rd.toString() + "," + imm.toString();
    }


}