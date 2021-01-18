package AST;

import Util.Position;
import java.util.ArrayList

public class WhileStmtNode extends StmtNode {
    public ExprNode funcname;
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
