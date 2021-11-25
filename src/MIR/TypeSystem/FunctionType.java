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

    @Override
    public String toString() {
        StringBuilder tmp = new StringBuilder();
        tmp.append("retType: ").append(returnType.toString()).append("\n" + "parameters: ");
        for (int i = 0; i < parameters.size(); ++i) {
            if (i > 0) tmp.append(", ");
            tmp.append(parameters.get(i).toString());
        }
        return tmp.toString();
    }
}