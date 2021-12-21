package MIR.Operand;

import MIR.Instruction.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class Register extends Operand {
    public String name;

    public Register (IRType type, String name) {
        super(type);
        this.name = name;
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}