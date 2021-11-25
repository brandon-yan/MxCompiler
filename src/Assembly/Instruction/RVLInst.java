package Assembly.Instruction;

import Assembly.Operand.RVGloReg;
import Assembly.Operand.RVVirReg;
import Assembly.Operand.RVImm;
import Assembly.Operand.RVRegister;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

public class RVLInst extends RVInstruction {

    public RVRegister rd, rs1;
    public RVImm offset;
    public RVWidthType widthType;

    public RVLInst(RVRegister rd, RVRegister rs1, RVImm offset, RVWidthType widthType) {
        super();
        this.rd = rd;
        this.rs1 = rs1;
        this.offset = offset;
        this.widthType = widthType;

        if (this.rd instanceof RVGloReg || this.rd instanceof RVVirReg)
            usedVirReg.add(this.rd);
        if (this.rs1 instanceof RVGloReg || this.rs1 instanceof RVVirReg)
            usedVirReg.add(this.rs1);

    }



}