package Assembly.Operand;

public class RVGloReg extends RVRegister {

    public boolean isString = false, isInt = false, isBool = false;
    public String strVal;
    public int intVal;
    public boolean boolVal;

    public RVGloReg(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return name;
    }

}