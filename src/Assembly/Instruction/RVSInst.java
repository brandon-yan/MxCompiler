package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class RVSInst extends RVInstruction {

    public RVRegister rd, addr;
    public RVImm offset;

    public RVSInst(RVRegister rd, RVRegister addr, RVImm offset) {
        super();
        this.rd = rd;
        this.addr = addr;
        this.offset = offset;

        if (this.rd instanceof RVGloReg || this.rd instanceof RVVirReg)
            usedVirReg.add(this.rd);
        if (this.addr instanceof RVGloReg || this.addr instanceof RVVirReg)
            usedVirReg.add(this.addr);

    }

    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
        if(rd != null && rd == reg1)
            rd = reg2;
        if(addr != null && addr == reg1)
            addr = reg2;
    }

    @Override
    public void replaceUse(RVRegister reg1, RVRegister reg2) {
        if(rd == reg1)
            rd = reg2;
        if(addr == reg1)
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

    @Override
    public LinkedHashSet<RVRegister> use() {
        LinkedHashSet<RVRegister> use = new LinkedHashSet<>();
        if (!(rd instanceof RVGloReg))
            use.add(rd);
        if(!(addr instanceof RVGloReg))
            use.add(addr);
        return use;
    }

    @Override
    public LinkedHashSet<RVRegister> def() {
        LinkedHashSet<RVRegister> def = new LinkedHashSet<>();
        return def;
    }

}