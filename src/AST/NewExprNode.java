package AST;

import Util.Position;
import java.util.ArrayList

public class NewExprNode extends StmtNode {
    public Type
    public ArrayList<ExprNode> parameters;

    public IfStmtNode(ExprNode funcname, ArrayList<ExprNode> parameters, Position pos) {
        super(pos);
        this.funcname = funcname;
        this.parameters = new ArrayList()<>;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
