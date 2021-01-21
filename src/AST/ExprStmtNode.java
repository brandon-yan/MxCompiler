package AST;

import Util.Position;
import java.util.ArrayList;

public class ExprStmtNode extends StmtNode{
    public ExprNode expr;

    public ExprStmtNode(ExprNode expr, Position pos) {
        super(pos);
        this.expr = expr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
