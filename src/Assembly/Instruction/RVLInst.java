package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

public class RVLInst extends RVInstruction {

    public RVRegister rd, rs1;
    public RVImm offset;

    public RVLInst(RVRegister rd, RVRegister rs1, RVImm offset) {
        super();
        this.rd = rd;
        this.rs1 = rs1;
        this.offset = offset;

        if (this.rd instanceof RVGloReg || this.rd instanceof RVVirReg)
            usedVirReg.add(this.rd);
        if (this.rs1 instanceof RVGloReg || this.rs1 instanceof RVVirReg)
            usedVirReg.add(this.rs1);

    }

    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
        if(rd != null && rd == reg1)
            rd = reg2;
        if(rs1 != null && rs1 == reg1)
            rs1 = reg2;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        if (offset instanceof RVAddrImm)
            tmp.append("lw ").append(rd.toString()).append(",").append(offset.val).append("(").append(rs1.toString()).append(")");
        else
            tmp.append("lw ").append(rd.toString()).append(",").append(offset.toString()).append("(").append(rs1.toString()).append(")");
        return tmp.toString();
    }
}