package AST;

import Util.Position;
import java.util.ArrayList;

public class FuncCallExprNode extends ExprNode {
    public String funcname;
    public ArrayList<ExprNode> parameters;

    public FuncCallExprNode(String funcname, ArrayList<ExprNode> parameters, Position pos) {
        super(pos);
        this.funcname = funcname;
        this.parameters = parameters;
    }


    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
