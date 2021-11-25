package Assembly.Operand;

public class RVRegister extends RVOperand {

    public String name;
    public boolean needLoad = true;

    public RVRegister(String name) {
        this.name = name;
    }

}