package MIR.Instruction;

import Backend.IRVisitor;
import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.ArrayList;
import java.util.HashSet;

public class PhiInst extends Instruction{
    public ArrayList<BasicBlock> blocks;
    public ArrayList<Operand> values;
    public Register regRet;
    public PhiInst(BasicBlock block, ArrayList<BasicBlock> blocks, ArrayList<Operand> values, Register regRet) {
        super(block);
        this.blocks = blocks;
        this.values = values;
        this.regRet = regRet;
        this.regRet.defs.add(this);
    }

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        tmp.append(regRet.toString()).append(" = phi ").append(regRet.IRtype.toString());

        for (int i = 0; i < values.size(); ++i) {
            if (i > 0)
                tmp.append(", ");
            tmp.append("[ ").append(values.get(i).toString()).append(" , ").append(blocks.get(i).toString()).append("]");
        }
        return tmp.toString();
    }
}