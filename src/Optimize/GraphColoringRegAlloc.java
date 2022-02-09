package Optimize;

import Assembly.Instruction.*;
import Assembly.Operand.RVImm;
import Assembly.Operand.RVPhyReg;
import Assembly.Operand.RVRegister;
import Assembly.Operand.RVVirReg;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;
import Assembly.RVModule;

import java.util.*;

public class GraphColoringRegAlloc {

    public static class Edge {
        RVRegister u, v;

        public Edge(RVRegister u, RVRegister v) {
            this.u = u;
            this.v = v;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof Edge && ((Edge) other).u.equals(u) && ((Edge) other).v.equals(v);
        }


        @Override
        public int hashCode() {
            return u.toString().hashCode() ^ v.toString().hashCode();
        }

    }

    public RVModule RVmodule;
    public RVFunction RVfunction;
    public int K;
    public RVPhyReg s0, sp, ra;

    public HashSet<RVRegister> preColored;
    public HashSet<RVRegister> initial = new LinkedHashSet<>();
    public HashSet<RVRegister> spillWorkList = new LinkedHashSet<>();
    public HashSet<RVRegister> freezeWorkList = new LinkedHashSet<>();
    public HashSet<RVRegister> simplifyWorkList = new LinkedHashSet<>();
    public HashSet<RVRegister> spilledNodes = new LinkedHashSet<>();
    public HashSet<RVRegister> coloredNodes = new LinkedHashSet<>();
    public HashSet<RVRegister> coalescedNodes = new LinkedHashSet<>();
    public HashSet<RVRegister> spillIntro = new LinkedHashSet<>();

    public HashSet<RVMoveInst> workListMoves = new LinkedHashSet<>();
    public HashSet<RVMoveInst> activeMoves = new LinkedHashSet<>();
    public HashSet<RVMoveInst> coalescedMoves = new LinkedHashSet<>();
    public HashSet<RVMoveInst> constrainedMoves = new LinkedHashSet<>();
    public HashSet<RVMoveInst> frozenMoves = new LinkedHashSet<>();

    public HashSet<Edge> edgeSet = new LinkedHashSet<>();
    public Stack<RVRegister> selectStack = new Stack<>();


    public GraphColoringRegAlloc(RVModule RVmodule) {
        this.RVmodule = RVmodule;
        this.K = RVModule.RVOKPhyRegName.size();
        this.preColored = new LinkedHashSet<>(RVModule.phyRegList);
        s0 = RVModule.getPhyReg("s0");
        sp = RVModule.getPhyReg("sp");
        ra = RVModule.getPhyReg("ra");
    }

    public void run() {
        for (var tmpfunc: RVmodule.RVFuncMap.values()) {
            if (!tmpfunc.builtin) {
                RVfunction = tmpfunc;
                funcColoring(tmpfunc);

                // sw ra,-4(sp)
                // sw s0,-8(sp)
                // mv s0,sp
                // addi sp,sp,-GCSize

                RVInstruction inst = tmpfunc.entry.head;
                RVSInst tmp = new RVSInst(ra, sp, new RVImm(-4));
                if (inst == null)
                    tmpfunc.entry.head = tmp;
                else
                    inst.addPreInst(tmpfunc.entry, tmp);

                inst = tmp;
                for (int i = 0; i < 12; ++i) {
                    if (RVfunction.usedPhyReg.contains(RVModule.getPhyReg("s" + i))) {
                        RVSInst tmp1 = new RVSInst(RVModule.getPhyReg("s" + i), sp, new RVImm(-4 * (i + 2)));
                        inst.addNextInst(RVfunction.entry, tmp1);
                        inst = inst.next;
                    }
                }
                RVMoveInst tmp2 = new RVMoveInst(s0, sp);
                inst.addNextInst(RVfunction.entry, tmp2);
                inst = inst.next;

                RVBinaryOpInst tmp3 = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, sp, sp, null, new RVImm(-RVfunction.graphColorStackSize()));
                inst.addNextInst(RVfunction.entry, tmp3);

                inst = RVfunction.exit.tail;
                RVBinaryOpInst tmp4 = new RVBinaryOpInst(RVInstruction.RVBinaryType.add, sp, sp, null, new RVImm(RVfunction.graphColorStackSize()));
                inst.addPreInst(RVfunction.exit, tmp4);

                for (int i = 0; i < 12; ++i) {
                    if (RVfunction.usedPhyReg.contains(RVModule.getPhyReg("s" + i))) {
                        RVLInst tmp5 = new RVLInst(RVModule.getPhyReg("s" + i), sp, new RVImm(-4 * (i + 2)));
                        inst.addPreInst(RVfunction.exit, tmp5);
                    }
                }
                RVLInst tmp6 = new RVLInst(ra, sp, new RVImm(-4));
                inst.addPreInst(RVfunction.exit, tmp6);

                for (RVBasicBlock block = RVfunction.entry; block != null; block = block.nextBlock) {
                    for (RVInstruction inst1 = block.head; inst1 != null; inst1 = inst1.next) {
                        if (inst1 instanceof RVMoveInst) {
                            RVMoveInst tmpMvInst = (RVMoveInst) inst1;
                            if (tmpMvInst.rd.color == tmpMvInst.rs1.color)
                                inst1.removeInst(block);
                        }

                    }
                }
            }
        }

    }

    public HashSet<RVMoveInst> nodeMoves(RVRegister reg) {
        HashSet<RVMoveInst> ret = new HashSet<>(workListMoves);
        ret.addAll(activeMoves);
        ret.retainAll(reg.movelist);
        return ret;
    }

    public boolean moveRelated(RVRegister reg) {
        return !nodeMoves(reg).isEmpty();
    }

    public void addEdge(RVRegister u, RVRegister v) {
        if (u != v && !edgeSet.contains(new Edge(u, v))) {
            edgeSet.add(new Edge(u, v));
            edgeSet.add(new Edge(v, u));
            if (!preColored.contains(u)) {
                u.adjlist.add(v);
                u.degree++;
            }
            if (!preColored.contains(v)) {
                v.adjlist.add(u);
                v.degree++;
            }
        }
    }

    public void funcColoring(RVFunction func) {
        while (true) {
            init();
            new LivenessAnalysis(func).run();
            build();
            for (var tmp: initial) {
                if (tmp.degree >= K)
                    spillWorkList.add(tmp);
                else if (moveRelated(tmp))
                    freezeWorkList.add(tmp);
                else
                    simplifyWorkList.add(tmp);
            }

            do {
                if(!simplifyWorkList.isEmpty())
                    simplify();
                else if(!workListMoves.isEmpty())
                    coalesce();
                else if(!freezeWorkList.isEmpty())
                    freeze();
                else if(!spillWorkList.isEmpty())
                    selectSpill();
            } while (! (simplifyWorkList.isEmpty() && workListMoves.isEmpty() && freezeWorkList.isEmpty() && spillWorkList.isEmpty()));

            assignColors();
            if (!spilledNodes.isEmpty())
                rewrite();
            else
                break;
        }
    }

    public void init() {
        initial.clear();
        spillWorkList.clear();
        freezeWorkList.clear();
        simplifyWorkList.clear();
        spilledNodes.clear();
        coloredNodes.clear();
        coalescedNodes.clear();

        workListMoves.clear();
        activeMoves.clear();
        coalescedMoves.clear();
        constrainedMoves.clear();
        frozenMoves.clear();

        edgeSet.clear();
        selectStack.clear();

        for (RVBasicBlock block = RVfunction.entry; block != null; block = block.nextBlock) {
            for (RVInstruction inst = block.head; inst != null; inst = inst.next) {
                initial.addAll(inst.def());
                initial.addAll(inst.use());
            }
        }
        initial.removeAll(preColored);
        for (var reg: initial)
            reg.clear();
        for (var reg: preColored)
            reg.preColor();

        for (RVBasicBlock block = RVfunction.entry; block != null; block = block.nextBlock) {
            double w = Math.pow(10.0, Integer.min(block.predecessor.size(), block.successor.size()));
            for (RVInstruction inst = block.head; inst != null; inst = inst.next) {
                for (var tmp : inst.use()) tmp.weight += w;
                for (var tmp : inst.def()) tmp.weight += w;
            }
        }
    }

    public void build() {
        for (RVBasicBlock block = RVfunction.entry; block != null; block = block.nextBlock) {
            HashSet<RVRegister> liveReg = new LinkedHashSet<>(block.liveOut);
            for (RVInstruction inst = block.tail; inst != null; inst = inst.prior) {
                if (inst instanceof RVMoveInst) {
                    liveReg.removeAll(inst.use());
                    for (var tmp: inst.def())
                        tmp.movelist.add((RVMoveInst) inst);
                    for (var tmp: inst.use())
                        tmp.movelist.add((RVMoveInst) inst);
                    workListMoves.add((RVMoveInst) inst);
                }
                HashSet<RVRegister> defReg = inst.def();
                liveReg.addAll(defReg);
                liveReg.add(RVModule.getPhyReg("zero"));
                for (var def: defReg)
                    for (var reg: liveReg) {
                        addEdge(reg, def);
                    }
                liveReg.removeAll(defReg);
                liveReg.addAll(inst.use());
            }
        }
    }

    public void simplify() {
        RVRegister reg = simplifyWorkList.iterator().next();
        simplifyWorkList.remove(reg);
        selectStack.push(reg);
        for (var tmp: adjacent(reg))
            decrementDegree(tmp);
    }

    public HashSet<RVRegister> adjacent(RVRegister reg) {
        HashSet<RVRegister> ret = new LinkedHashSet<>(reg.adjlist);
        ret.removeAll(selectStack);
        ret.removeAll(coalescedNodes);
        return ret;
    }

    public void decrementDegree(RVRegister reg) {
        reg.degree--;
        if (reg.degree == K - 1) {
            HashSet<RVRegister> nodes = new LinkedHashSet<>(adjacent(reg));
            nodes.add(reg);
            enableMoves(nodes);
            spillWorkList.remove(reg);
            if (moveRelated(reg))
                freezeWorkList.add(reg);
            else
                simplifyWorkList.add(reg);
        }
    }

    public void enableMoves(HashSet<RVRegister> regs) {
        for (var reg: regs) {
            for (var tmp: nodeMoves(reg)) {
                if (activeMoves.contains(tmp)) {
                    activeMoves.remove(tmp);
                    workListMoves.add(tmp);
                }
            }
        }
    }

    public void coalesce() {
        RVMoveInst inst = workListMoves.iterator().next();
        RVRegister x = getAlias(inst.rd);
        RVRegister y = getAlias(inst.rs1);
        RVRegister u, v;
        if (preColored.contains(y)) {
            u = y;
            v = x;
        }
        else {
            u = x;
            v = y;
        }
        workListMoves.remove(inst);
        if (u == v) {
            coalescedMoves.add(inst);
            addWorkList(u);
        }
        else if (preColored.contains(v) || edgeSet.contains(new Edge(u, v))) {
            constrainedMoves.add(inst);
            addWorkList(u);
            addWorkList(v);
        }
        else if ((preColored.contains(u) && checkOK(u, v)) || (!preColored.contains(u) && conservative(u, v))) {
            coalescedMoves.add(inst);
            combine(u, v);
            addWorkList(u);
        }
        else
            activeMoves.add(inst);
    }

    public void addWorkList(RVRegister reg) {
        if (!preColored.contains(reg) && !moveRelated(reg) && reg.degree < K) {
            simplifyWorkList.add(reg);
            freezeWorkList.remove(reg);
        }
    }

    public RVRegister getAlias(RVRegister reg) {
        if (coalescedNodes.contains(reg)) {
            reg.alias = getAlias(reg.alias);
            return reg.alias;
        }
        else
            return reg;
    }

    public boolean ifOK(RVRegister t, RVRegister u) {
        return t.degree < K || preColored.contains(t) || edgeSet.contains(new Edge(t, u));
    }

    public boolean checkOK(RVRegister u, RVRegister v) {
        boolean tmp = true;
        for (var t: adjacent(v))
            tmp = tmp & ifOK(t, u);
        return tmp;
    }

    public boolean conservative(RVRegister u, RVRegister v) {
        HashSet<RVRegister> nodes = new LinkedHashSet<>(adjacent(u));
        nodes.addAll(adjacent(v));
        int cnt = 0;
        for (var tmp: nodes)
            if (tmp.degree >= K)
                cnt++;
        return cnt < K;
    }

    public void combine(RVRegister u, RVRegister v) {
        if (freezeWorkList.contains(v))
            freezeWorkList.remove(v);
        else
            spillWorkList.remove(v);
        coalescedNodes.add(v);
        v.alias = u;
        u.movelist.addAll(v.movelist);

        HashSet<RVRegister> t = new LinkedHashSet<>();
        t.add(v);
        enableMoves(t);

        for (var tmp: adjacent(v)) {
            addEdge(tmp, u);
            decrementDegree(tmp);
        }
        if (u.degree >= K && freezeWorkList.contains(u)) {
            freezeWorkList.remove(u);
            spillWorkList.add(u);
        }
    }

    public void freeze() {
        RVRegister u = freezeWorkList.iterator().next();
        freezeWorkList.remove(u);
        simplifyWorkList.add(u);
        freezeMove(u);
    }

    public void freezeMove(RVRegister u) {
        for (var tmp: nodeMoves(u)) {
            RVRegister x = tmp.rd;
            RVRegister y = tmp.rs1;
            RVRegister v;
            if (getAlias(u) == getAlias(y))
                v = getAlias(x);
            else
                v = getAlias(y);
            activeMoves.remove(tmp);
            frozenMoves.add(tmp);
            if(v.degree < K && nodeMoves(v).isEmpty()) {
                freezeWorkList.remove(v);
                simplifyWorkList.add(v);
            }
        }
    }

    public void selectSpill() {
        RVRegister reg = getSpill();
        spillWorkList.remove(reg);
        simplifyWorkList.add(reg);
        freezeMove(reg);
    }

    public RVRegister getSpill() {
        RVRegister min = null;
        double cost = 0.0;
        Iterator<RVRegister> it = spillWorkList.iterator();
        while (it.hasNext()) {
            min = it.next();
            cost = min.weight / min.degree;
            if (!spillIntro.contains(min))
                break;
        }
        while (it.hasNext()) {
            RVRegister reg = it.next();
            if (!spillIntro.contains(reg) && (reg.weight / reg.degree) < cost) {
                min = reg;
                cost = reg.weight / reg.degree;
            }
        }
        return min;
    }

    public void assignColors() {
        while (!selectStack.isEmpty()) {
            RVRegister reg = selectStack.pop();
            ArrayList<RVPhyReg> okColor = new ArrayList<>(RVModule.okPhyRegList);
            for (var tmp: reg.adjlist)
                if (coloredNodes.contains(getAlias(tmp)) || preColored.contains(getAlias(tmp)))
                    okColor.remove(getAlias(tmp).color);
            if (okColor.isEmpty())
                spilledNodes.add(reg);
            else {
                reg.color = okColor.get(0);
                coloredNodes.add(reg);
                RVfunction.usedPhyReg.add(reg.color);
            }
        }
        for (var tmp: coalescedNodes) {
            tmp.color = getAlias(tmp).color;
            RVfunction.usedPhyReg.add(tmp.color);
        }
    }

    public void rewrite() {
        for (var tmp : spilledNodes) {
            tmp.stackoffset = RVfunction.graphColorStackNum;
            RVfunction.graphColorStackNum++;
        }
        for (RVBasicBlock block = RVfunction.entry; block != null; block = block.nextBlock) {
            for (RVInstruction inst = block.head; inst != null; inst = inst.next) {
                if (!inst.def().isEmpty() && inst.def().size() == 1) {
                    RVRegister rd = inst.def().iterator().next();
                    if (rd instanceof RVVirReg)
                        getAlias(rd);
                }
            }
        }

        for (RVBasicBlock block = RVfunction.entry; block != null; block = block.nextBlock) {
            for (RVInstruction inst = block.head; inst != null; inst = inst.next) {
                for (var tmpuse : inst.use()) {
                    if (tmpuse.stackoffset >= 0) {
                        if (!inst.def().isEmpty() && inst.def().contains(tmpuse)) {
                            RVVirReg tmpVirReg = new RVVirReg(RVModule.virRegCnt++);
                            spillIntro.add(tmpVirReg);
                            inst.replaceUse(tmpuse, tmpVirReg);
                            RVLInst tmpload = new RVLInst(tmpVirReg, s0, new RVImm(-4 * (tmpuse.stackoffset + 14)));
                            inst.addPreInst(block, tmpload);
                            RVSInst tmpstore = new RVSInst(tmpVirReg, s0, new RVImm(-4 * (tmpuse.stackoffset + 14)));
                            inst.addNextInst(block, tmpstore);
                        } else if (inst instanceof RVMoveInst) {
                            RVMoveInst tmpMoveInst = (RVMoveInst) inst;
                            if ((tmpMoveInst.rs1 == tmpuse && tmpMoveInst.rd.stackoffset < 0)) {
                                RVLInst tmpInst = new RVLInst(tmpMoveInst.rd, s0, new RVImm(-4 * (tmpuse.stackoffset + 14)));
                                inst.replaceInst(block, tmpInst);
                                inst = tmpInst;
                            }
                        } else {
                            RVVirReg tmpVirReg = new RVVirReg(RVModule.virRegCnt++);
                            spillIntro.add(tmpVirReg);
                            inst.replaceUse(tmpuse, tmpVirReg);
                            RVLInst tmpload = new RVLInst(tmpVirReg, s0, new RVImm(-4 * (tmpuse.stackoffset + 14)));
                            inst.addPreInst(block, tmpload);
                        }
                    }
                }

                for (var tmpdef : inst.def()) {
                    if (tmpdef.stackoffset >= 0) {
                        if (!inst.use().isEmpty() && !inst.use().contains(tmpdef)) {
                            if (inst instanceof RVMoveInst) {
                                RVMoveInst tmpMoveInst = (RVMoveInst) inst;
                                if ((tmpMoveInst.rd == tmpdef && tmpMoveInst.rs1.stackoffset < 0)) {
                                    RVSInst tmpInst = new RVSInst(tmpMoveInst.rs1, RVModule.getPhyReg("s0"), new RVImm(-4 * (tmpdef.stackoffset + 14)));
                                    inst.replaceInst(block, tmpInst);
                                    inst = tmpInst;
                                } else {
                                    RVVirReg tmpVirReg = new RVVirReg(RVModule.virRegCnt++);
                                    spillIntro.add(tmpVirReg);
                                    inst.replaceUse(tmpdef, tmpVirReg);
                                    RVSInst tmpstore = new RVSInst(tmpVirReg, s0, new RVImm(-4 * (tmpdef.stackoffset + 14)));
                                    inst.addNextInst(block, tmpstore);
                                }
                            }
                            else {
                                RVVirReg tmpVirReg = new RVVirReg(RVModule.virRegCnt++);
                                spillIntro.add(tmpVirReg);
                                inst.replaceUse(tmpdef, tmpVirReg);
                                RVSInst tmpstore = new RVSInst(tmpVirReg, s0, new RVImm(-4 * (tmpdef.stackoffset + 14)));
                                inst.addNextInst(block, tmpstore);
                            }
                        }
                    }
                }
            }
        }
    }


}