package MIR.Operand;

import MIR.Instruction.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class ConstString extends Operand {
    public String value;
    public String name;

    public ConstString (IRType type, String value, String name) {
        super(type);
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}