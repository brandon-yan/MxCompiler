package MIR.Instruction;

import MIR.BasicBlock;
import MIR.Operand.*;
import MIR.TypeSystem.*;

import java.util.ArrayList;
import java.util.HashSet;

public class GetElementPtrInst extends Instruction{
    public Register ptrRet;
    public Operand pointer;
    public ArrayList<Operand> ptrIndex;
    public GetElementPtrInst(BasicBlock block, Register ptrRet, Operand pointer, ArrayList<Operand> ptrIndex) {
        super(block);
        this.ptrRet = ptrRet;
        this.pointer = pointer;
        this.ptrIndex = ptrIndex;
    }
}