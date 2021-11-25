package MIR.TypeSystem;

public class VoidType extends IRType {
    public VoidType() {
        super();
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String toString() {
        return "void";
    }
}