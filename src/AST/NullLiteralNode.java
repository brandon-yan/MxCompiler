package AST;

import Util.Position;

public class NullLiteralNode extends LiteralNode {
    public NullLiteralNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
