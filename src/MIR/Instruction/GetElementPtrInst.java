package MIR.Instruction;

import Backend.IRVisitor;
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

    public void accept(IRVisitor it){
        it.visit(this);
    }

    @Override
    public String toString() {
        String tmpBaseType, tmpPtrType;
        if (pointer.IRtype instanceof PointerType) {
            tmpBaseType = ((PointerType)pointer.IRtype).point.toString();
            tmpPtrType = pointer.IRtype.toString();
        }
        else {
            tmpBaseType = pointer.IRtype.toString();
            tmpPtrType = tmpBaseType + "*";
        }
        StringBuilder tmp = new StringBuilder();
        tmp.append(ptrRet.toString()).append(" = GEP ").append(tmpBaseType).append(" ").append(tmpPtrType).append(" ").append(pointer.toString());
        for (Operand index : ptrIndex) {
            tmp.append(", ").append(index.IRtype.toString()).append(" ").append(index.toString());
        }
        return tmp.toString();

    }
}