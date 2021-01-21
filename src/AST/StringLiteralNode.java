package AST;

import Util.Position;

public class StringLiteralNode extends LiteralExprNode {
    public String value;
    public StringLiteralNode(String value, Position pos) {
        super(pos);
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
