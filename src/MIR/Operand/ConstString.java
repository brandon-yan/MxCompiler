package MIR.Operand;

import MIR.Instruction.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class ConstString extends Operand {
    public String value;

    public ConstString (IRType type, String value) {
        super(type);
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}