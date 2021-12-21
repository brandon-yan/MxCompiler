package MIR.Operand;

import MIR.Instruction.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class GlobalVariable extends Operand {
    public String name;
    public Operand init;

    public GlobalVariable (IRType type, String name, Operand init) {
        super(type);
        this.name = name;
        this.init = init;
    }

    @Override
    public String toString() {
        return "@" + name;
    }

}
