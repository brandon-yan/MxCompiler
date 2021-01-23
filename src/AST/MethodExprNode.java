package AST;

import Util.Position;
import java.util.ArrayList;

public class MethodExprNode extends ExprNode {
    public ExprNode expr;
    public String methodname;
    public ArrayList<ExprNode> parameters;

    public MethodExprNode(ExprNode expr, String methodname, ArrayList<ExprNode> parameters, Position pos) {
        super(pos);
        this.expr = expr;
        this.methodname = methodname;
        this.parameters = parameters;
    }


    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
