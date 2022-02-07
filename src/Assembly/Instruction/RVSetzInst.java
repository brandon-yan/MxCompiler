package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;

import java.util.ArrayList;
import java.util.LinkedHashSet;

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
    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
        if(rd == reg1)
            rd = reg2;
        if(rs1 == reg1)
            rs1 = reg2;
    }

    @Override
    public void replaceUse(RVRegister reg1, RVRegister reg2) {
        if(rd == reg1)
            rd = reg2;
        if(rs1 == reg1)
            rs1 = reg2;
    }

    @Override
    public String toString() {
        return "s" + cmpType.toString() + "z " + rd.toString() + "," + rs1.toString();
    }

    @Override
    public LinkedHashSet<RVRegister> use() {
        LinkedHashSet<RVRegister> use = new LinkedHashSet<>();
        if(!(rs1 instanceof RVGloReg))
            use.add(rs1);
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