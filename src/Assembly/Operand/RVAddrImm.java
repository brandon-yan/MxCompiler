package Assembly.Operand;

public class RVAddrImm extends RVImm {

    public RVRegister baseReg;

    public RVAddrImm(int val, RVRegister baseReg) {
        super(val);
        this.baseReg = baseReg;
    }

    @Override
    public String toString() {
        return val + " (" + baseReg.toString() + ")";
    }

}