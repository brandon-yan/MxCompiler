package AST;

import Util.Position;

public class IdentifierExprNode extends ExprNode {
    public String identifier;
    public IdentifierExprNode(String identifier, Position pos) {
        super(pos);
        this.identifier = identifier;
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
