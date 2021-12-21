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

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;


public class Main {
    public static void main(String[] args) throws Exception{

        //String file_name = "./testcases/test.mx";
        //String file_name = "./testcases/sema/expression-package/expression-3.mx";
        //InputStream input = new FileInputStream(file_name);
        InputStream input = System.in;
        PrintStream output = System.out;
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
            //new RegAlloc(RVmodule).run1();
            //new AsmPrinter(new PrintStream("output.s")).runRVModule(RVmodule);
            new AsmPrinter(output).runRVModule(RVmodule);
        } catch (Error er) {
            System.err.println(er.toString());
            throw new RuntimeException();
        }
    }
}