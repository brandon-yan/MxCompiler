package AST;

import Util.Position;

public class ArrayExprNode extends ExprNode{
    public ExprNode name;
    public ExprNode index;

    public ArrayExprNode(ExprNode name, ExprNode index, Position pos) {
        super(pos);
        this.name = name;
        this.index = index;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isAssignable() {
        return true;
    }
}