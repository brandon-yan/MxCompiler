package Assembly.Instruction;

import Assembly.Operand.RVGloReg;
import Assembly.Operand.RVVirReg;
import Assembly.Operand.RVImm;
import Assembly.Operand.RVRegister;

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


}