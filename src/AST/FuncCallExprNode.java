package AST;

import Util.Position;
import java.util.ArrayList;

public class FuncCallExprNode extends ExprNode {
    public ExprNode funcname;
    public ArrayList<ExprNode> parameters;

    public FuncCallExprNode(ExprNode funcname, ArrayList<ExprNode> parameters, Position pos) {
        super(pos);
        this.funcname = funcname;
        this.parameters = new ArrayList<>();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
