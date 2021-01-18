package AST;

import Util.Position;

abstract public class LiteralExprNode extends ExprNode {
    public LiteralExprNode(Position pos) {
        super(pos);
    }
}