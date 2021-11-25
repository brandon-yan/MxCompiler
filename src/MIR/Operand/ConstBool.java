package MIR.Operand;

import MIR.Instruction.*;
import MIR.TypeSystem.*;

import java.util.HashSet;

public class ConstBool extends Operand {
    public boolean value;

    public ConstBool (IRType type, boolean value) {
        super(type);
        this.value = value;
    }

    @Override
    public String toString(){
        if (value)
            return "true";
        else
            return "false";
    }

}