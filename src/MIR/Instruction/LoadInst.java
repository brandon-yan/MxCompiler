package MIR.Instruction;

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
    }
}