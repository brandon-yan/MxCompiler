package Assembly.Instruction;

import Assembly.Operand.RVPhyReg;
import Assembly.Operand.RVRegister;
import Assembly.RVBasicBlock;

import java.awt.event.WindowStateListener;
import java.util.ArrayList;
import java.util.LinkedHashSet;

abstract public class RVInstruction {
    public enum RVBinaryType {
        add, sub, mul, div, rem,
        slt,
        sll, sra,
        and, or, xor
    }

    public enum RVCmpType {
        eq, ne, lt, le, gt, ge
    }

    public RVInstruction prior = null, next = null;
    public ArrayList<RVRegister> usedVirReg = new ArrayList<>();

    public RVInstruction() { }

    public void addPreInst(RVBasicBlock block, RVInstruction inst) {
        if (this.prior == null) {
            inst.prior = null;
            inst.next = this;
            this.prior = inst;
            block.head = inst;
        }
        else {
            inst.prior = this.prior;
            inst.next = this;
            this.prior.next = inst;
            this.prior = inst;
        }
    }

    public void addNextInst(RVBasicBlock block, RVInstruction inst) {
        if (this.next == null) {
            inst.prior = this;
            inst.next = null;
            this.next = inst;
            block.tail = inst;
        }
        else {
            inst.prior = this;
            inst.next = this.next;
            this.next.prior = inst;
            this.next = inst;
        }
    }

    abstract public void replaceReg(RVRegister reg1, RVPhyReg reg2);

    abstract public void replaceUse(RVRegister reg1, RVRegister reg2);

    abstract public LinkedHashSet<RVRegister> use();

    abstract public LinkedHashSet<RVRegister> def();

    public void replaceInst(RVBasicBlock block, RVInstruction inst) {
        inst.prior = this.prior;
        inst.next = this.next;
        if (this.prior == null)
            block.head = inst;
        else
            this.prior.next = inst;
        if (this.next == null)
            block.tail = inst;
        else
            this.next.prior = inst;
    }

    public void removeInst(RVBasicBlock block){
        if (this.prior == null)
            block.head = this.next;
        else
            this.prior.next = this.next;
        if(this.next == null)
            block.tail = this.prior;
        else
            this.next.prior = this.prior;
    }
}