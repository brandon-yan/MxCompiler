package Assembly;

import Assembly.Instruction.RVBinaryOpInst;
import Assembly.Instruction.RVInstruction;
import Assembly.Instruction.RVLiInst;
import Assembly.Operand.*;
import MIR.BasicBlock;
import MIR.Function;
import MIR.Operand.*;

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
    public HashMap<BasicBlock, RVBasicBlock> RVBlockMap = new HashMap<>();

    public RVModule() {
        for (int i = 0; i < 32; ++i)
            phyRegList.add(new RVPhyReg(RVPhyRegName.get(i)));
    }

    public void addString(ConstString tmpString) {
        RVGloReg tmpGloReg = new RVGloReg(tmpString.name);
        tmpGloReg.isString = true;
        tmpGloReg.strVal = tmpString.value;
        gloRegMap.put(tmpString, tmpGloReg);
    }

    public void addGloReg(GlobalVariable tmpVar) {
        RVGloReg tmpGloReg = new RVGloReg(tmpVar.name);
        Operand init = tmpVar.init;
        if (init instanceof ConstInt) {
            tmpGloReg.isInt = true;
            tmpGloReg.intVal = ((ConstInt)init).value;
        }
        else if (init instanceof ConstBool) {
            tmpGloReg.isBool = true;
            tmpGloReg.boolVal = ((ConstBool)init).value;
        }
        else if (init instanceof ConstString) {
            tmpGloReg.isString = true;
            tmpGloReg.strVal = ((ConstString)init).value;
        }
        else {
            tmpGloReg.isInt = true;
            tmpGloReg.intVal = 0;
        }
        gloRegMap.put(tmpVar, tmpGloReg);
    }

    public void addFunc(Function tmpFunc) {
        RVFunction tmpRVFunc = new RVFunction(tmpFunc);
        RVFuncMap.put(tmpFunc, tmpRVFunc);
    }

    public RVRegister getRVRegister(Operand tmpOper, RVBasicBlock tmpBlock) {
        if (tmpOper instanceof ConstInt) {
            int val = ((ConstInt)tmpOper).value;
            RVVirReg tmpRVReg = new RVVirReg(virRegCnt++);
//            if (val < 0) {
//                RVBinaryOpInst tmp = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, tmpRVReg, phyRegList.get(0), null, new RVImm(val));
//                tmpBlock.addInst(tmp);
//                return tmpRVReg;
//            }
//            else {
//                RVLiInst tmp = new RVLiInst(tmpRVReg, new RVImm(val));
//                tmpBlock.addInst(tmp);
//                return tmpRVReg;
//            }
            RVLiInst tmp = new RVLiInst(tmpRVReg, new RVImm(val));
            tmpBlock.addInst(tmp);
            return tmpRVReg;
        }
        else if (tmpOper instanceof ConstBool) {
            int val = 0;
            boolean tmpval = ((ConstBool)tmpOper).value;
            if (tmpval)
                val = 1;
            RVVirReg tmpRVReg = new RVVirReg(virRegCnt++);
            RVLiInst tmp = new RVLiInst(tmpRVReg, new RVImm(val));
            tmpBlock.addInst(tmp);
            return tmpRVReg;
        }
        else if (tmpOper instanceof ConstString) {
            throw new RuntimeException();
        }
        else if (tmpOper instanceof ConstNull) {
            int val = 0;
            RVVirReg tmpRVReg = new RVVirReg(virRegCnt++);
            RVLiInst tmp = new RVLiInst(tmpRVReg, new RVImm(val));
            tmpBlock.addInst(tmp);
            return tmpRVReg;
        }
        else if (tmpOper instanceof GlobalVariable) {
            if (gloRegMap.containsKey(tmpOper))
                return gloRegMap.get(tmpOper);
            RVGloReg tmpRVReg = new RVGloReg(((GlobalVariable)tmpOper).name);
            gloRegMap.put(tmpOper, tmpRVReg);
            return tmpRVReg;
        }
        else if (tmpOper instanceof Parameter || tmpOper instanceof Register) {
            if (virRegMap.containsKey(tmpOper))
                return virRegMap.get(tmpOper);
            RVVirReg tmpRVReg = new RVVirReg(virRegCnt++);
            virRegMap.put(tmpOper, tmpRVReg);
            return tmpRVReg;
        }
        else
            return phyRegList.get(0);

    }

    public RVBasicBlock getRVBasicBlock(BasicBlock tmpBlock) {
        RVBasicBlock RVBblock;
        if (tmpBlock != null) {
            if (RVBlockMap.containsKey(tmpBlock))
                return RVBlockMap.get(tmpBlock);
            RVBblock = new RVBasicBlock("LBB" + (RVblockCnt++), tmpBlock);
            RVBlockMap.put(tmpBlock, RVBblock);
        }
        else
            RVBblock = new RVBasicBlock("LBB" + (RVblockCnt++), tmpBlock);
        return RVBblock;
    }

    public RVPhyReg getPhyReg(String name) {
        for (int i = 0; i < 32; ++i) {
            if (RVPhyRegName.get(i).equals(name))
                return phyRegList.get(i);
        }
        throw new RuntimeException();
    }

}