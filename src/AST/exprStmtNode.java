package AST;

import Util.Position;
import java.util.ArrayList;

public class ExprStmtNode extends StmtNode{
    public TypeNode type;
    public ArrayList<ExprNode> arraysize;
    public int dimension;

    public ExprStmtNode(TypeNode type, ArrayList<ExprNode> arraysize, int dimension, Position pos) {
        super(pos);
        this.type = type;
        this.arraysize = new ArrayList()<>;
        this.dimension = dimension;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
