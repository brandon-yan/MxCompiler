package AST;

import Util.Type;
import Util.Position;

public abstract class ExprNode extends ASTNode {
    public TypeNode type;

    public ExprNode(Position pos) {
        super(pos);
    }

    public boolean isAssignable() {
        return false;
    }
}
