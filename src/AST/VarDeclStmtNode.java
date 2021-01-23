package AST;

import Util.Position;
import java.util.ArrayList;

public class VarDeclStmtNode extends StmtNode {
    public TypeNode type;
    public VarListNode varlist;

    public VarDeclStmtNode(TypeNode type, VarListNode varlist, Position pos) {
        super(pos);
        this.type = type;
        this.varlist = varlist;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

}
