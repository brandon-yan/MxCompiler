package AST;

import Util.Position;
import java.util.ArrayList;

public class NewExprNode extends ExprNode{
    public ArrayList<ExprNode> arraysize;
    public int dimension;

    public NewExprNode(TypeNode type, int dimension, Position pos) {
        super(pos);
        this.type = new TypeNode(pos, type.typename, dimension);
        this.arraysize = new ArrayList<>();
        this.dimension = dimension;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
