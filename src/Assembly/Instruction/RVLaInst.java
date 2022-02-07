package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class RVLaInst extends RVInstruction {

    public RVRegister rd, addr;

    public RVLaInst(RVRegister rd, RVRegister addr) {
        super();
        this.rd = rd;
        this.addr = addr;

        if (this.rd instanceof RVGloReg || this.rd instanceof RVVirReg)
            usedVirReg.add(this.rd);

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
        return "la " + rd.toString() + "," + addr.toString();
    }

    @Override
    public LinkedHashSet<RVRegister> use() {
        LinkedHashSet<RVRegister> use = new LinkedHashSet<>();
        if(!(addr instanceof RVGloReg))
            use.add(addr);
        return use;
    }

    @Override
    public LinkedHashSet<RVRegister> def() {
        LinkedHashSet<RVRegister> def = new LinkedHashSet<>();
        if(!(rd instanceof RVGloReg))
            def.add(rd);
        return def;
    }

}