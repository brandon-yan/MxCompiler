package Backend;


import Assembly.Instruction.*;
import Assembly.Operand.*;
import Assembly.RVBasicBlock;
import Assembly.RVModule;

import java.util.HashMap;
import java.util.HashSet;

public class RegAlloc {
    public RVModule RVmodule;
    public HashSet<RVRegister> regSet = new HashSet<>();
    public HashMap<RVRegister, RVRegister> regMap = new HashMap<>();

    public RegAlloc(RVModule RVmodule) {
        this.RVmodule = RVmodule;
    }

    public void run() {
        for (var tmpFunc: RVmodule.RVFuncMap.values())
            if (!tmpFunc.builtin) {
                for (RVBasicBlock block = tmpFunc.entry; block != null; block = block.nextBlock)
                    for (RVInstruction inst = block.head; inst != null; inst = inst.next)
                        for (int i = 0; i < inst.usedVirReg.size(); ++i) {
                            RVRegister tmpReg = inst.usedVirReg.get(i);
                            if (!regSet.contains(tmpReg)) {
                                regSet.add(tmpReg);
                                tmpFunc.stackCnting++;
                            }
                        }

                int stackSize = tmpFunc.stackSize();
                int segNum = stackSize / 2048 + 1;
                for (RVBasicBlock block = tmpFunc.entry; block != null; block = block.nextBlock)
                    for (RVInstruction inst = block.head; inst != null; inst = inst.next) {
                        int phyRegCnt = 0;
                        for (int i = 0; i < inst.usedVirReg.size(); ++i) {
                            RVRegister tmpReg = inst.usedVirReg.get(i);
                            RVRegister tmpStoreReg;
                            if (!regMap.containsKey(tmpReg)) {
                                int offsetSp = 4 * (segNum + 1 + tmpFunc.stackCnt + 1);
                                int segNumThis = offsetSp / 2048;
                                tmpStoreReg = new RVStackReg(tmpFunc, RVmodule.getPhyReg("s" + segNumThis), new RVImm(1024 - offsetSp % 2048), tmpFunc.stackCnt);
                                regMap.put(tmpReg,tmpStoreReg);
                            }
                            else
                                tmpStoreReg = regMap.get(tmpReg);

                            if (tmpStoreReg instanceof RVStackReg) {
                                if (tmpReg instanceof RVVirReg) {
                                    if (tmpReg.needLoad || i != 0) {
                                        RVLInst tmp = new RVLInst(RVmodule.getPhyReg("t" + phyRegCnt), ((RVStackReg)tmpStoreReg).baseReg, ((RVStackReg)tmpStoreReg).offset, RVInstruction.RVWidthType.w);
                                        inst.addPreInst(block, tmp);
                                    }
                                    inst.replaceReg(tmpReg, RVmodule.getPhyReg("t" + phyRegCnt));
                                    phyRegCnt++;
                                }
                                else if (tmpReg instanceof RVGloReg) {
                                    RVReloImm tmpImm = new RVReloImm((RVGloReg) tmpReg, RVReloImm.RelocationType.hi);
                                    RVLuiInst tmp = new RVLuiInst(RVmodule.getPhyReg("t" + phyRegCnt), tmpImm);
                                    inst.addPreInst(block, tmp);
                                    phyRegCnt++;
                                    RVReloImm tmpImm1 = new RVReloImm((RVGloReg) tmpReg, RVReloImm.RelocationType.lo);
                                    RVLInst tmp1 = new RVLInst(RVmodule.getPhyReg("t" + phyRegCnt), RVmodule.getPhyReg("t" + (phyRegCnt - 1)), tmpImm1, RVInstruction.RVWidthType.w);
                                    inst.addPreInst(block, tmp1);
                                    inst.replaceReg(tmpReg, RVmodule.getPhyReg("t" + phyRegCnt));
                                    phyRegCnt++;
                                }
                            }
                            else
                                inst.replaceReg(tmpReg, (RVPhyReg) tmpStoreReg);
                        }

                        phyRegCnt = 0;
                        int tmpSiz = Integer.min(1, inst.usedVirReg.size());
                        if (inst instanceof RVJumpInst)
                            break;
                        if (inst instanceof RVSInst)
                            tmpSiz = 0;
                        for (int i = 0; i < tmpSiz; ++i) {
                            RVRegister tmpReg = inst.usedVirReg.get(i);
                            RVRegister tmpStoreReg = regMap.get(tmpReg);
                            if (tmpStoreReg instanceof  RVStackReg) {
                                if (tmpReg instanceof RVVirReg) {
                                    RVSInst tmp = new RVSInst(RVmodule.getPhyReg("t" + phyRegCnt), ((RVStackReg)tmpStoreReg).baseReg, ((RVStackReg)tmpStoreReg).offset, RVInstruction.RVWidthType.w);
                                    inst.addNextInst(block, tmp);
                                    inst = inst.next;
                                    phyRegCnt++;
                                }
                                else if (tmpReg instanceof RVGloReg) {
                                    RVReloImm tmpImm = new RVReloImm((RVGloReg) tmpReg, RVReloImm.RelocationType.hi);
                                    RVLuiInst tmp = new RVLuiInst(RVmodule.getPhyReg("t" + phyRegCnt), tmpImm);
                                    inst.addNextInst(block, tmp);
                                    inst = inst.next;
                                    phyRegCnt++;
                                    RVReloImm tmpImm1 = new RVReloImm((RVGloReg) tmpReg, RVReloImm.RelocationType.lo);
                                    RVSInst tmp1 = new RVSInst(RVmodule.getPhyReg("t" + phyRegCnt), RVmodule.getPhyReg("t" + (phyRegCnt - 1)), tmpImm1, RVInstruction.RVWidthType.w);
                                    inst.addPreInst(block, tmp1);
                                    inst = inst.next;
                                    phyRegCnt++;
                                }
                            }
                        }
                    }


                // sw ra,-4(sp)
                // sw s0,-8(sp)
                // addi sp,sp,-UpperSize
                // mv s0,sp
                // addi sp,sp,-DownSize
                assert tmpFunc.entry != null;
                RVInstruction tmp = tmpFunc.entry.head;
                RVInstruction tmp1 = new RVSInst(RVmodule.getPhyReg("ra"), RVmodule.getPhyReg("sp"), new RVImm(-4), RVInstruction.RVWidthType.w);
                if (tmp == null)
                    tmpFunc.entry.head = tmp1;
                else
                    tmp.addPreInst(tmpFunc.entry, tmp1);
                tmp = tmp1;
                for (int i = 0; i < segNum; ++i) {
                    RVSInst tmp2 = new RVSInst(RVmodule.getPhyReg("s" + i), RVmodule.getPhyReg("sp"), new RVImm(-8 - 4 * i), RVInstruction.RVWidthType.w);
                    tmp.addNextInst(tmpFunc.entry, tmp2);
                    tmp = tmp.next;
                }

                for (int i = 0; i < segNum; ++i) {
                    RVBinaryOpInst tmp3 = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, RVmodule.getPhyReg("sp"), RVmodule.getPhyReg("sp"), null, new RVImm(-1024));
                    tmp.addNextInst(tmpFunc.entry, tmp3);
                    tmp = tmp.next;
                    RVMoveInst tmp4 = new RVMoveInst(RVmodule.getPhyReg("s" + i), RVmodule.getPhyReg("sp"));
                    tmp.addNextInst(tmpFunc.entry, tmp4);
                    tmp = tmp.next;
                    RVBinaryOpInst tmp5 = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, RVmodule.getPhyReg("sp"), RVmodule.getPhyReg("sp"), null, new RVImm(-1024));
                    tmp.addNextInst(tmpFunc.entry, tmp5);
                    tmp = tmp.next;
                }


                assert tmpFunc.exit != null;
                tmp = tmpFunc.exit.tail;

                for (int i = 0; i < segNum; ++i) {
                    RVBinaryOpInst tmp6 = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, RVmodule.getPhyReg("sp"), RVmodule.getPhyReg("sp"), null, new RVImm(1024));
                    tmp.addPreInst(tmpFunc.exit, tmp6);
                    RVBinaryOpInst tmp7 = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, RVmodule.getPhyReg("sp"), RVmodule.getPhyReg("sp"), null, new RVImm(1024));
                    tmp.addPreInst(tmpFunc.exit, tmp7);
                }

                for (int i = 0; i < segNum; ++i) {
                    RVLInst tmp8 = new RVLInst(RVmodule.getPhyReg("s" + i), RVmodule.getPhyReg("sp"), new RVImm(-8 - 4 * i), RVInstruction.RVWidthType.w);
                    tmp.addPreInst(tmpFunc.exit, tmp8);
                }

                RVLInst tmp9 = new RVLInst(RVmodule.getPhyReg("ra"), RVmodule.getPhyReg("sp"), new RVImm(-4), RVInstruction.RVWidthType.w);
                tmp.addPreInst(tmpFunc.exit, tmp9);

            }
    }
}