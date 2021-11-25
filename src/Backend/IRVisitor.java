package Backend;

import AST.*;
import MIR.*;
import MIR.Instruction.*;
import MIR.Module;
import MIR.Operand.*;
import MIR.TypeSystem.*;
import Parser.MxParser;
import Util.*;
import Util.scope.GlobalScope;

import java.util.ArrayList;
import java.util.Stack;


public interface IRVisitor {
    void visit(Module it);
    void visit(Function it);
    void visit(BasicBlock it);

    void visit(AllocateInst it);
    void visit(BinaryOpInst it);
    void visit(BitCastToInst it);
    void visit(BranchInst it);
    void visit(CallInst it);
    void visit(GetElementPtrInst it);
    void visit(IcmpInst it);
    void visit(LoadInst it);
    void visit(MoveInst it);
    void visit(PhiInst it);
    void visit(ReturnInst it);
    void visit(StoreInst it);
}
