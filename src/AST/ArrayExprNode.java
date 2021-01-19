package AST;

import Util.Position;

public class ArrayExprNode extends ExprNode{
    public ExprNode name;
    public ExprNode index;
    public int dimension;

    public ArrayExprNode(ExprNode name, ExprNode index, int dimension, Position pos) {
        super(pos);
        this.name = name;
        this.index = index;
        this.dimension = dimension;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}