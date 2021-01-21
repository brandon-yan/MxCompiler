package AST;

import Util.Position;

public class BoolLiteralNode extends LiteralExprNode {
    public boolean value;
    public BoolLiteralNode(boolean value, Position pos) {
        super(pos);
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
