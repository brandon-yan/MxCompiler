package Assembly.Operand;

public class RVVirReg extends RVRegister {

    public int index;

    public RVVirReg(int index) {
        super("%" + index);
        this.index = index;
    }

    @Override
    public String toString() {
        if (color == null)
            return name;
        else return color.name;
    }

}