package AST;

import Util.Type;
import Util.Position;

public class BinaryExprNode extends ExprNode {
    public ExprNode lhs, rhs;
    public enum BinaryOperator {
        add, sub,
        mul, div, mod,
        greater, less, greatequal, lessequal,
        logicand, logicor,
        bitand, bitor,
        rightshift, leftshift
    }
    public BinaryOperator opCode;

    public BinaryExprNode(ExprNode lhs, ExprNode rhs, BinaryOperator opCode, Position pos) {
        super(pos);
        this.lhs = lhs;
        this.rhs = rhs;
        this.opCode = opCode;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
