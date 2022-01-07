package MIR.TypeSystem;

public class IntType extends IRType {
    public int siz;
    public IntType(int siz) {
        super();
        this.siz = siz;
    }

    @Override
    public int size() {
        if (siz == 32)
            return 4;
        else
            return 1;
    }

    @Override
    public String toString() {
        return "i" + siz;
    }
}