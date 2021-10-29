package MIR.Operand;

import MIR.Instruction.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

abstract public class Operand {
    public IRType IRtype;

    public Operand(IRType type) {
        this.IRtype = type;
    }
}

