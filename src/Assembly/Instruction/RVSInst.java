package Assembly.Instruction;

import Assembly.Operand.*;
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

    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
        if(rd != null && rd == reg1)
            rd = reg2;
        if(addr != null && rd == reg1)
            addr = reg2;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        if (offset instanceof RVAddrImm)
            tmp.append("sw ").append(rd.toString()).append(",").append(offset.val).append("(").append(addr.toString()).append(")");
        else
            tmp.append("sw ").append(rd.toString()).append(",").append(offset.toString()).append("(").append(addr.toString()).append(")");
        return tmp.toString();
    }

}