package MIR.TypeSystem;

import java.util.ArrayList;

public class FunctionType extends IRType {
    public IRType returnType;
    public ArrayList<IRType> parameters;
    public FunctionType(IRType type) {
        super();
        returnType = type;
        parameters = new ArrayList<>();
    }

    @Override
    public int size() {
        return 0;
    }
}