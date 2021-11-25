package Assembly;

import Assembly.Operand.RVGloReg;
import Assembly.Operand.RVPhyReg;
import Assembly.Operand.RVVirReg;
import MIR.Function;
import MIR.Operand.Operand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class RVModule {

    public static ArrayList<String> RVPhyRegName = new ArrayList<>(Arrays.asList(
            "zero", "ra", "sp", "gp", "tp", "t0", "t1", "t2", "s0", "s1",
            "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
            "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11", "t3", "t4", "t5", "t6"));

    public static ArrayList<String> RVOKPhyRegName = new ArrayList<>(Arrays.asList(
            "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7", "t0", "t1", "t2",
            "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8",
            "s9", "s10", "s11", "t3", "t4", "t5", "t6", "ra"));

    public static ArrayList<String> RVCalleePhyRegName = new ArrayList<>(Arrays.asList(
            "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", "s8", "s9", "s10", "s11"));

    public static ArrayList<String> RVCallerPhyRegName = new ArrayList<>(Arrays.asList(
            "ra", "t0", "t1", "t2", "a0", "a1", "a2", "a3", "a4", "a5", "a6", "a7",
            "t3", "t4", "t5", "t6"));

    public static int max_imm = 2047, min_imm = -2048;
    public static int virRegCnt = 0, RVblockCnt = 0;

    public static ArrayList<RVPhyReg> phyRegList = new ArrayList<>();
    public HashMap<Operand, RVVirReg> virRegMap = new HashMap<>();
    public HashMap<Operand, RVGloReg> gloRegMap = new HashMap<>();
    public HashMap<Function, RVFunction> RVFuncMap = new HashMap<>();

    public RVModule() {
        for (int i = 0; i < 32; ++i)
            phyRegList.add(new RVPhyReg(RVPhyRegName.get(i)));
    }


}