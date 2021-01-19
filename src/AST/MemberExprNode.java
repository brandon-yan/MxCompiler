package AST;

import Util.Position;
import java.util.ArrayList

public class MemberExprNode extends ExprNode {
    public ExprNode expr;
    public String member;

    public MemberExprNode(ExprNode expr, String member, Position pos) {
        super(pos);
        this.expr = expr;
        this.member = member;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
