package MIR.Operand;

import MIR.Instruction.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class ConstInt extends Operand {
    public int value;

    public ConstInt (IRType type, int value) {
        super(type);
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

}