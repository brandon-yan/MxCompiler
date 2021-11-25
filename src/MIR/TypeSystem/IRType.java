package MIR.TypeSystem;

abstract public class IRType {
    public IRType() {}

    public abstract int size();

    public abstract String toString();

    public int dimension() {
        return 0;
    };
}