package AST;

import Util.Type;
import Util.Position;

public class PrefixExprNode extends ExprNode {
    public ExprNode expr;
    public enum PrefixOperator {
        positive, negative,
        prefixadd, prefixsub,
        logicnot, bitnot
    }
    public PrefixOperator opCode;

    public PrefixExprNode(ExprNode expr, PrefixOperator opCode, Position pos) {
        super(pos);
        this.expr = expr;
        this.opCode = opCode;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
