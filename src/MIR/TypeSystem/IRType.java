package MIR.TypeSystem;

abstract public class IRType {
    public IRType() {}

    public abstract int size();

    public int dimension() {
        return 0;
    };
}