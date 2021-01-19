package AST;

import Util.Position;

public class IntLiteralNode extends LiteralNode {
    public int value;
    public IntLiteralNode(int value, Position pos) {
        super(pos);
        this.value = value;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
