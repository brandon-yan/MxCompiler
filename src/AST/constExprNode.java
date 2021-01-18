package AST;

import Util.Position;

import Util.Type;

public class ConstExprNode extends ExprNode {
    public int value;

    public ConstExprNode(int value, Type intType, Position pos) {
        super(pos);
        this.value = value;
        type = intType;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
