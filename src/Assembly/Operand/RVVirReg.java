package Assembly.Operand;

public class RVVirReg extends RVRegister {

    public int index;

    public RVVirReg(int index) {
        super("%" + index);
        this.index = index;
    }

    @Override
    public String toString() {
        return name;
    }

}