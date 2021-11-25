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

import java.io.*;


public class IRPrinter implements IRVisitor{

    public PrintStream out;

    public IRPrinter(PrintStream out) {
        this.out = out;
    }
    @Override
    public void visit(Module it) {
        for (var tmpClass : it.types.keySet()) {
            out.println(it.types.get(tmpClass).toString());
        }
        out.println();
        for (var tmpGlobal : it.global.keySet()) {
            out.println(it.global.get(tmpGlobal).toString());
        }
        out.println();
        for (var tmpFunc : it.functions.keySet()) {
            if (!it.functions.get(tmpFunc).builtin)
                out.println(it.functions.get(tmpFunc).toString());
        }
        out.println();
        for (var tmpFunc : it.functions.values()) {
            if (!tmpFunc.builtin) {
                tmpFunc.accept(this);
                out.println();
            }
        }
    }
    @Override
    public void visit(Function it) {
        out.print(it.toString());
        out.println("{");
        BasicBlock tmpBBlock = it.entry;
        while (tmpBBlock != null) {
            tmpBBlock.accept(this);
            out.println();
            tmpBBlock = tmpBBlock.nexts;
        }
        out.println("}");
    }
    @Override
    public void visit(BasicBlock it) {
        out.println(it.toString());
        Instruction tmpInst = it.head;
        while (tmpInst != null) {
            tmpInst.accept(this);
            out.println();
            tmpInst = tmpInst.next;
        }
    }

    @Override
    public void visit(AllocateInst it) {
        out.println("    " + it.toString());
    }
    @Override
    public void visit(BinaryOpInst it) {
        out.println("    " + it.toString());
    }
    @Override
    public void visit(BitCastToInst it) {
        out.println("    " + it.toString());
    }
    @Override
    public void visit(BranchInst it) {
        out.println("    " + it.toString());
    }
    @Override
    public void visit(CallInst it) {
        out.println("    " + it.toString());
    }
    public void visit(GetElementPtrInst it) {
        out.println("    " + it.toString());
    }
    @Override
    public void visit(IcmpInst it) {
        out.println("    " + it.toString());
    }
    @Override
    public void visit(LoadInst it) {
        out.println("    " + it.toString());
    }
    @Override
    public void visit(MoveInst it) {
        out.println("    " + it.toString());
    }
    @Override
    public void visit(PhiInst it) {
        out.println("    " + it.toString());
    }
    @Override
    public void visit(ReturnInst it) {
        out.println("    " + it.toString());
    }
    @Override
    public void visit(StoreInst it) {
        out.println("    " + it.toString());
    }

}