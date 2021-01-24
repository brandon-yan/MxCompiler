package AST;

import Util.Position;
import java.util.ArrayList;

public class VarDeclNode extends ProgramDeclNode {
    public TypeNode type;
    public VarListNode varlist;

    public VarDeclNode(TypeNode type, VarListNode varlist, Position pos) {
        super(pos);
        this.type = type;
        this.varlist = varlist;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

}
