package Assembly.Operand;

import Assembly.RVFunction;

public class RVStackReg extends RVRegister {

    public int index;
    public RVImm offset;
    public RVRegister baseReg;
    public RVFunction func;

    public RVStackReg(RVFunction func, RVRegister baseReg, RVImm offset, int index) {
        super("stack_" +func.stackCnt);
        this.func = func;
        this.baseReg = baseReg;
        this.offset = offset;
        this.index = index;
        func.stackCnt++;
    }

    @Override
    public String toString() {
        return offset.toString() + " (" + baseReg.toString() + ")";
    }

}