package Backend;

import Assembly.Instruction.RVInstruction;
import Assembly.Operand.RVGloReg;
import Assembly.RVBasicBlock;
import Assembly.RVFunction;
import Assembly.RVModule;

import java.io.PrintStream;

public class AsmPrinter {

    public PrintStream out;
    public int curNum;

    public AsmPrinter(PrintStream out) {
        this.out = out;
    }

    public void runRVModule(RVModule module) {
        for (var tmpFunc: module.RVFuncMap.values())
            if (!tmpFunc.builtin)
                runRVFunction(tmpFunc);
        for (var tmpGloVar: module.gloRegMap.values())
            runGloVar(tmpGloVar);
    }

    public void runRVFunction(RVFunction func) {
        out.println("\t.globl\t" + func.name);
        out.println(func.name + ":");
        //func.getDFS();
        for (RVBasicBlock block = func.entry; block != null; block = block.nextBlock)
            runRVBasicBlock(block);
    }

    public void runRVBasicBlock(RVBasicBlock block) {
        out.println(block.name + ":");
        for (RVInstruction inst = block.head; inst != null; inst = inst.next)
            out.println("\t" + inst.toString());
    }


    public void runGloVar(RVGloReg gloVar) {
        if (gloVar.isInt)
            out.println("\t.p2align\t2");
        out.println(gloVar.name + ":");
        if (gloVar.isBool)
            out.println("\t.byte\t" + gloVar.boolVal);
        else if (gloVar.isInt)
            out.println("\t.word.\t" + gloVar.intVal);
        else if (gloVar.isString) {
            String tmp = gloVar.strVal;
            tmp = tmp.replace("\\", "\\\\");
            tmp = tmp.replace("\n", "\\n");
            tmp = tmp.replace("\"", "\\\"");
            out.println("\t.asciz\t\"" + tmp + "\"");
        }
        out.println("");
    }
}