package MIR.Instruction;

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
    }
}