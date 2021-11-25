package Assembly.Instruction;

import Assembly.Operand.RVGloReg;
import Assembly.Operand.RVVirReg;
import Assembly.Operand.RVImm;
import Assembly.Operand.RVRegister;
import Assembly.RVBasicBlock;

import java.util.ArrayList;

public class RVBranchInst extends RVInstruction {
    public RVCmpType cmpType;
    public RVRegister rs1, rs2;
    public RVBasicBlock trueBlock, falseBlock;

    public RVBranchInst(RVCmpType cmpType, RVRegister rs1, RVRegister rs2, RVBasicBlock trueBlock, RVBasicBlock falseBlock) {
        super();
        this.cmpType = cmpType;
        this.rs1 = rs1;
        this.rs2 = rs2;
        this.trueBlock = trueBlock;
        this.falseBlock = falseBlock;

        if (this.rs1 instanceof RVGloReg || this.rs1 instanceof RVVirReg)
            usedVirReg.add(this.rs1);
        if ((this.rs2 != null) && (this.rs2 instanceof RVGloReg || this.rs2 instanceof RVVirReg))
            usedVirReg.add(this.rs2);

    }


}