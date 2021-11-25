package Assembly.Operand;

public class RVPhyReg extends RVRegister {

    public RVPhyReg(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return name;
    }

}