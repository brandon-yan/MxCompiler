package Optimize;

import Assembly.Instruction.RVBranchInst;
import Assembly.Instruction.RVInstruction;
import Assembly.Instruction.RVJumpInst;
import Assembly.Operand.RVRegister;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;
import MIR.Instruction.BranchInst;
import MIR.Operand.Register;

import java.util.*;

public class LivenessAnalysis {
    public RVFunction RVfunction;
    public HashSet<RVBasicBlock> blockVis = new HashSet<>();
    public HashMap<RVBasicBlock, HashSet<RVRegister>> blockUse = new HashMap<>();
    public HashMap<RVBasicBlock, HashSet<RVRegister>> blockDef = new HashMap<>();

    public LivenessAnalysis(RVFunction func) {
        RVfunction = func;
    }

    public void getCFG(RVFunction func) {
        RVBasicBlock block = func.entry;
        while (block != null) {
            block.predecessor = new ArrayList<>();
            block.successor = new ArrayList<>();
            block = block.nextBlock;
        }

        block = func.entry;
        while (block != null) {
            RVInstruction inst = block.head;
            while (inst != null) {
                if (inst instanceof RVBranchInst) {
                    if (((RVBranchInst)inst).trueBlock != null) {
                        RVBasicBlock tmp = ((RVBranchInst)inst).trueBlock;
                        tmp.predecessor.add(block);
                        block.successor.add(tmp);
                    }
                    if (((RVBranchInst)inst).falseBlock != null) {
                        RVBasicBlock tmp = ((RVBranchInst)inst).falseBlock;
                        tmp.predecessor.add(block);
                        block.successor.add(tmp);
                    }
                }
                else if (inst instanceof RVJumpInst) {
                    RVBasicBlock tmp = ((RVJumpInst)inst).destBlock;
                    tmp.predecessor.add(block);
                    block.successor.add(tmp);
                }
                inst = inst.next;
            }
            block = block.nextBlock;
        }
    }

    public void run() {
        getCFG(RVfunction);
        RVBasicBlock block = RVfunction.entry;
        while (block != null) {
            HashSet<RVRegister> use = new LinkedHashSet<>();
            HashSet<RVRegister> def = new LinkedHashSet<>();
            RVInstruction inst = block.head;
            while (inst != null) {
                HashSet<RVRegister> tmp = new LinkedHashSet<>(inst.use());
                tmp.removeAll(def);
                use.addAll(tmp);
                def.addAll(inst.def());
                inst = inst.next;
            }
            blockUse.put(block, use);
            blockDef.put(block, def);
            block.liveIn.clear();
            block.liveOut.clear();
            block = block.nextBlock;
        }

        calculateLiveout1(RVfunction.exit);
    }

    public  void calculateLiveout(RVBasicBlock block) {
        if (blockVis.contains(block))
            return;
        blockVis.add(block);
        HashSet<RVRegister> liveout = new LinkedHashSet<>();
        for (var tmp: block.successor)
            liveout.addAll(tmp.liveIn);
        block.liveOut.addAll(liveout);

        HashSet<RVRegister> livein = new LinkedHashSet<>(liveout);
        livein.removeAll(blockDef.get(block));
        livein.addAll(blockUse.get(block));
        livein.removeAll(block.liveIn);

        if (!livein.isEmpty()) {
            block.liveIn.addAll(livein);
            blockVis.removeAll(block.predecessor);
        }
        for (var tmp: block.predecessor)
            calculateLiveout(tmp);
    }

    public Queue<RVBasicBlock> calculateQueue = new LinkedList<>();

    public void calculateLiveout1(RVBasicBlock block) {
        calculateQueue.clear();
        blockVis.clear();
        calculateQueue.offer(block);
        blockVis.add(block);
        while (!calculateQueue.isEmpty()) {
            RVBasicBlock tmpBlock = calculateQueue.poll();
            HashSet<RVRegister> liveout = new HashSet<>();
            for (var tmp: tmpBlock.successor)
                liveout.addAll(tmp.liveIn);
            tmpBlock.liveOut.addAll(liveout);

            HashSet<RVRegister> livein = new HashSet<>(liveout);
            livein.removeAll(blockDef.get(tmpBlock));
            livein.addAll(blockUse.get(tmpBlock));
            livein.removeAll(tmpBlock.liveIn);

            if (!livein.isEmpty()) {
                tmpBlock.liveIn.addAll(livein);
                blockVis.removeAll(tmpBlock.predecessor);
            }
            for (var preblock: tmpBlock.predecessor)
                if (!blockVis.contains(preblock)) {
                    calculateQueue.offer(preblock);
                    blockVis.add(preblock);
                }


        }
    }
}