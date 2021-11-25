package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

public class RVMoveInst extends RVInstruction {

    public RVRegister rd, rs1;

    public RVMoveInst(RVRegister rd, RVRegister rs1) {
        super();
        this.rd = rd;
        this.rs1 = rs1;

        if (this.rd instanceof RVGloReg || this.rd instanceof RVVirReg)
            usedVirReg.add(this.rd);
        if (this.rs1 instanceof RVGloReg || this.rs1 instanceof RVVirReg)
            usedVirReg.add(this.rs1);

    }



}