package Assembly.Instruction;

import Assembly.Operand.RVGloReg;
import Assembly.Operand.RVVirReg;
import Assembly.Operand.RVImm;
import Assembly.Operand.RVRegister;
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



}