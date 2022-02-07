package Assembly.Instruction;

import Assembly.Operand.*;
import Assembly.RVBasicBlock;

import java.util.ArrayList;
import java.util.LinkedHashSet;

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

    @Override
    public void replaceReg(RVRegister reg1, RVPhyReg reg2) {
        if(rs1 == reg1)
            rs1 = reg2;
        if(rs2 == reg1)
            rs2 = reg2;
    }

    @Override
    public void replaceUse(RVRegister reg1, RVRegister reg2) {
        if(rs1 == reg1)
            rs1 = reg2;
        if(rs2 == reg1)
            rs2 = reg2;
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder("b");
        if (rs2 == null)
            tmp.append(cmpType.toString()).append("z").append(" ").append(rs1.toString()).append(",").append(trueBlock.toString()).append("\n\tj ").append(falseBlock.toString());
        else
            tmp.append(cmpType.toString()).append(" ").append(rs1.toString()).append(",").append(rs2.toString()).append(",").append(trueBlock.toString()).append("\n\tj ").append(falseBlock.toString());
        return tmp.toString();
    }

    @Override
    public LinkedHashSet<RVRegister> use() {
        LinkedHashSet<RVRegister> use = new LinkedHashSet<>();
        if(!(rs1 instanceof RVGloReg))
            use.add(rs1);
        if(rs2 != null && !(rs2 instanceof RVGloReg))
            use.add(rs2);
        return use;
    }

    @Override
    public LinkedHashSet<RVRegister> def() {
        LinkedHashSet<RVRegister> def = new LinkedHashSet<>();
        return def;
    }

}