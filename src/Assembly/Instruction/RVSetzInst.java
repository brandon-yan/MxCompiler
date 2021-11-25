package Assembly.Instruction;

import Assembly.Operand.RVGloReg;
import Assembly.Operand.RVVirReg;
import Assembly.Operand.RVImm;
import Assembly.Operand.RVRegister;
import Assembly.RVBasicBlock;

import java.util.ArrayList;

public class RVSetzInst extends RVInstruction {
    public RVCmpType cmpType;
    public RVRegister rd, rs1;

    public RVSetzInst(RVCmpType cmpType, RVRegister rd, RVRegister rs1) {
        super();
        this.cmpType = cmpType;
        this.rd = rd;
        this.rs1 = rs1;


        if (this.rd instanceof RVGloReg || this.rd instanceof RVVirReg)
            usedVirReg.add(this.rd);
        if (this.rs1 instanceof RVGloReg || this.rs1 instanceof RVVirReg)
            usedVirReg.add(this.rs1);

    }


}