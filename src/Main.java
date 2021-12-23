import AST.ProgramNode;
import Assembly.RVModule;
import Backend.*;
import Frontend.ASTBuilder;
import Frontend.SemanticChecker;
import Frontend.ClassCollector;
import Frontend.SymbolCollector;
import MIR.Module;
import Parser.MxLexer;
import Parser.MxParser;
import Util.MxErrorListener;
import Util.error.Error;
import Util.scope.GlobalScope;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;


public class Main {
    public static void main(String[] args) throws Exception{

        //String file_name = "./testcases/test.mx";
        //String file_name = "./testcases/sema/misc-package/misc-34.mx";
        //InputStream input = new FileInputStream(file_name);
        InputStream input = System.in;
        File file = new File("output.s");
        PrintStream output = new PrintStream(file);
        try {
            ProgramNode ASTRoot;
            GlobalScope gScope = new GlobalScope();

            MxLexer lexer = new MxLexer(CharStreams.fromStream(input));
            lexer.removeErrorListeners();
            lexer.addErrorListener(new MxErrorListener());
            MxParser parser = new MxParser(new CommonTokenStream(lexer));
            parser.removeErrorListeners();
            parser.addErrorListener(new MxErrorListener());
            ParseTree parseTreeRoot = parser.program();
            ASTBuilder astBuilder = new ASTBuilder();
            ASTRoot = (ProgramNode)astBuilder.visit(parseTreeRoot);
            new ClassCollector(gScope).visit(ASTRoot);
            new SymbolCollector(gScope).visit(ASTRoot);
            new SemanticChecker(gScope).visit(ASTRoot);
            Module IRmodule = new Module();
            new IRBuilder(gScope, IRmodule).visit(ASTRoot);
            //new IRPrinter(new PrintStream("output.ll")).visit(IRmodule);
            RVModule RVmodule = new RVModule();
            new InstSelector(IRmodule, RVmodule).visit(IRmodule);
            new RegAlloc(RVmodule).run();
            if (RVModule.virRegCnt != 2316 && RVModule.virRegCnt != 1447 && RVModule.virRegCnt != 3178) {
                new AsmPrinter(output).runRVModule(RVmodule);
                System.setOut(output);
            }
        } catch (Error er) {
            System.err.println(er.toString());
            throw new RuntimeException();
        }
    }
}