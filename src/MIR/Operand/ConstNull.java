package MIR.Operand;

import MIR.Instruction.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class ConstNull extends Operand {

    public ConstNull (IRType type) {
        super(type);
    }

    @Override
    public String toString() {
        return "null";
    }

}