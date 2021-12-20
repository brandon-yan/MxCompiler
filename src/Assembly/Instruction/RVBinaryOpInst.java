package Assembly.Instruction;

import Assembly.Operand.*;

import java.util.ArrayList;

public class RVBinaryOpInst extends RVInstruction {
    public RVBinaryType binaryType;
    public RVRegister rd, rs1, rs2;
    public RVImm imm;

    public RVBinaryOpInst(RVBinaryType binaryType, RVRegister rd, RVRegister rs1, RVRegister rs2, RVImm imm) {
        super();
        this.binaryType = binaryType;
        this.rd = rd;
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.imm = imm;

        if (this.rd instanceof RVGloReg || this.rd instanceof RVVirReg)
            usedVirReg.add(this.rd);
        if (this.rs1 instanceof RVGloReg || this.rs1 instanceof RVVirReg)
            usedVirReg.add(this.rs1);
        if (this.rs2 instanceof RVGloReg || this.rs2 instanceof RVVirReg)
            usedVirReg.add(this.rs2);

    }

    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
        if(rd == reg1)
            rd = reg2;
        if(rs1 == reg1)
            rs1 = reg2;
        if(rs2 == reg1)
            rs2 = reg2;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder(binaryType.toString());
        if (imm == null)
            tmp.append(" ").append(rd.toString()).append(",").append(rs1.toString()).append(",").append(rs2.toString());
        else
            tmp.append("i ").append(rd.toString()).append(",").append(rs1.toString()).append(",").append(imm.toString());
        return tmp.toString();
    }

}