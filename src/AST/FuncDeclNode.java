package AST;

import Util.Position;
import java.util.ArrayList;

public class FuncDeclNode extends ProgramDeclNode {
    public TypeNode type;
    public String identifier;
    public VarListNode parameterlist;
    public BlockStmtNode suite;

    public FuncDeclNode(TypeNode type, String identifier, VarListNode parameterlist, BlockStmtNode suite, Position pos) {
        super(pos);
        this.type = type;
        this.identifier = identifier;
        this.suite = suite;
        this.parameterlist = parameterlist;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}