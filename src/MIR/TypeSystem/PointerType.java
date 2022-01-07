package MIR.TypeSystem;

public class PointerType extends IRType {
    public IRType point;
    public int dimension;
    public PointerType(IRType point) {
        super();
        this.point = point;
        this.dimension = point.dimension() + 1;
    }

    @Override
    public int size() {
        return 4;
    }

    @Override
    public String toString() {
        return point.toString() + "*";
    }

    @Override
    public int dimension() {
        return dimension;
    }
}