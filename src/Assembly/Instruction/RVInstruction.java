package Assembly.Instruction;

import Assembly.Operand.RVRegister;
import java.util.ArrayList;

abstract public class RVInstruction {
    public enum RVBinaryType {
        add, sub, mul, div, rem,
        slt,
        sll, sra,
        and, or, xor
    }

    public enum RVCmpType {
        eq, ne, slt, sle, sgt, sge
    }

    public enum RVWidthType {
        b, w
    }

    public RVInstruction prior = null, next = null;
    public ArrayList<RVRegister> usedVirReg = new ArrayList<>();

    public RVInstruction() { }
}