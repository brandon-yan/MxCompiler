package MIR.Operand;

import MIR.Instruction.*;
import MIR.TypeSystem.*;
import MIR.Function;

import java.util.HashSet;

public class Parameter extends Operand {
    public String name;

    public Parameter (IRType type, String name) {
        super(type);
        this.name = name;
    }

    @Override
    public String toString() {
        return "@ " + name;
    }

}
