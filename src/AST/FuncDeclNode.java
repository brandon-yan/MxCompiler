package AST;

import Util.Position;
import java.util.ArrayList;

public class FuncDeclNode extends ProgramDeclNode {
    public TypeNode type;
    public String identifier;
    public ArrayList<VarDeclNode> parameterlist;
    public BlockStmtNode suite;

    public FuncDeclNode(TypeNode type, String identifier, ArrayList<VarDeclNode> parameterlist, BlockStmtNode suite, Position pos) {
        super(pos);
        this.type = type;
        this.identifier = identifier;
        this.suite = suite;
        this.Varlist = new ArrayList()<>;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}