package MIR.TypeSystem;

public class IntType extends IRType {
    public int siz;
    public IntType(int siz) {
        super();
        this.siz = siz;
    }

    @Override
    public int size() {
        return siz;
    }

    @Override
    public String toString() {
        return "i" + siz;
    }
}