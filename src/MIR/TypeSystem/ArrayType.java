package MIR.TypeSystem;

public class ArrayType extends IRType {
    public int arraysiz;
    public IRType type;
    public ArrayType(int arraysiz, IRType type) {
        super();
        this.arraysiz = arraysiz;
        this.type = type;
    }

    @Override
    public int size() {
        return type.size() * arraysiz;
    }

    @Override
    public String toString() {
        return "[" + arraysiz + " x " + type.toString() + "]";
    }

}