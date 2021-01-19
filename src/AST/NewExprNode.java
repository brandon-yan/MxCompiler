package AST;

import Util.Position;
import java.util.ArrayList;

public class NewExprNode extends ExprNode{
    public TypeNode type;
    public ArrayList<ExprNode> arraysize;
    public int dimension;

    public NewExprNode(TypeNode type, ArrayList<ExprNode> arraysize, int dimension, Position pos) {
        super(pos);
        this.type = type;
        this.arraysize = new ArrayList<>();
        this.dimension = dimension;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
