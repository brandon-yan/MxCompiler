package Assembly.Instruction;

import Assembly.Operand.RVGloReg;
import Assembly.Operand.RVVirReg;
import Assembly.Operand.RVImm;
import Assembly.Operand.RVRegister;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;

public class RVSInst extends RVInstruction {

    public RVRegister rd, addr;
    public RVImm offset;
    public RVWidthType widthType;

    public RVSInst(RVRegister rd, RVRegister addr, RVImm offset, RVWidthType widthType) {
        super();
        this.rd = rd;
        this.addr = addr;
        this.offset = offset;
        this.widthType = widthType;

        if (this.rd instanceof RVGloReg || this.rd instanceof RVVirReg)
            usedVirReg.add(this.rd);
        if (this.addr instanceof RVGloReg || this.addr instanceof RVVirReg)
            usedVirReg.add(this.addr);

    }



}