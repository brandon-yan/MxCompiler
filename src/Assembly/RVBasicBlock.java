package Assembly;

import Assembly.Instruction.RVInstruction;
import MIR.BasicBlock;

public class RVBasicBlock {

    public String name;
    public BasicBlock IRBBlock;
    public RVBasicBlock nextBlock = null;
    public RVInstruction head = null;
    public RVInstruction tail = null;

    public RVBasicBlock(String name, BasicBlock IRBBlock) {
        this.name = name;
        this.IRBBlock = IRBBlock;
    }
}