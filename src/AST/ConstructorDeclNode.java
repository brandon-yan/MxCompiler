package AST;

import Util.Position;
import java.util.ArrayList;

public class ConstructorDeclNode extends ProgramDeclNode {
    public String identifier;
    public VarListNode parameterlist;
    public BlockStmtNode suite;

    public ConstructorDeclNode(String identifier, VarListNode parameterlist, BlockStmtNode suite, Position pos) {
        super(pos);
        this.identifier = identifier;
        this.suite = suite;
        this.parameterlist = parameterlist;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}