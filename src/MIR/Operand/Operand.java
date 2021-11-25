package MIR.Operand;

import MIR.Instruction.*;
import MIR.TypeSystem.*;


abstract public class Operand {
    public IRType IRtype;

    public Operand(IRType type) {
        this.IRtype = type;
    }
}

