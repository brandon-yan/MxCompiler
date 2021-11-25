package Backend;

import Assembly.RVBasicBlock;
import Assembly.RVFunction;
import Assembly.RVModule;
import MIR.BasicBlock;
import MIR.Function;
import MIR.Instruction.*;
import MIR.Module;

public class InstSelector implements IRVisitor{

    public Module IRmodule;
    public RVModule RVmodule;
    public RVFunction RVfunction;
    public RVBasicBlock RVbasicblock;
    public InstSelector (Module IRmodule) {
        this.IRmodule = IRmodule;
        this.RVmodule = new RVModule();
        this.RVfunction = null;
        this.RVbasicblock = null;
    }

    @Override
    public void visit(Module it) {
    }
    @Override
    public void visit(Function it) {
    }
    @Override
    public void visit(BasicBlock it) {
    }

    @Override
    public void visit(AllocateInst it) {
    }
    @Override
    public void visit(BinaryOpInst it) {

    }
    @Override
    public void visit(BitCastToInst it) {

    }
    @Override
    public void visit(BranchInst it) {

    }
    @Override
    public void visit(CallInst it) {

    }
    public void visit(GetElementPtrInst it) {

    }
    @Override
    public void visit(IcmpInst it) {

    }
    @Override
    public void visit(LoadInst it) {

    }
    @Override
    public void visit(MoveInst it) {

    }
    @Override
    public void visit(PhiInst it) {

    }
    @Override
    public void visit(ReturnInst it) {

    }
    @Override
    public void visit(StoreInst it) {

    }
}