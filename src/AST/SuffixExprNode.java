package AST;

import Util.Type;
import Util.Position;

public class SuffixExprNode extends ExprNode {
    public ExprNode expr;
    public enum SuffixOperator {
        suffixadd, suffixsub
    }
    public SuffixOperator opCode;

    public SuffixExprNode(ExprNode expr, SuffixOperator opCode, Position pos) {
        super(pos);
        this.expr = expr;
        this.opCode = opCode;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
