package MIR.Instruction;

import Backend.IRVisitor;
import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class LoadInst extends Instruction{
    public Register regRet;
    public Operand address;
    public LoadInst(BasicBlock block, Register regRet, Operand address) {
        super(block);
        this.regRet = regRet;
        this.address = address;
        this.regRet.defs.add(this);
    }

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        return regRet.toString() + " = load " + regRet.IRtype.toString() + " " + address.IRtype.toString() + " " + address.toString();
    }
}