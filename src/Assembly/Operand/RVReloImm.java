package Assembly.Operand;

public class RVReloImm extends RVImm {

    public enum RelocationType {
        hi, lo
    }
    public RelocationType reloType;
    public RVGloReg reloReg;

    public RVReloImm(RVGloReg reloReg, RelocationType reloType) {
        super(0);
        this.reloReg = reloReg;
        this.reloType = reloType;
    }

    @Override
    public String toString() {
        return "%" + reloType.toString() + "(" + reloReg.toString() + ")";
    }

}