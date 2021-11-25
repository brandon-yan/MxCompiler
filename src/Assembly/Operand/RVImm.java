package Assembly.Operand;

public class RVImm extends RVOperand {

    public int val;

    public RVImm(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return Integer.toString(val);
    }

}