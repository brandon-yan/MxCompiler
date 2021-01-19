package AST;

import Util.Position;
import java.util.ArrayList;


public class VarListNode extends ASTNode {
    public ArrayList<VarNode> Varlist;

    public VarListNode(Position pos) {
        super(pos);
        this.Varlist = new ArrayList<>();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}