package AST;

import Util.Position;

public class IfStmtNode extends StmtNode {
    public ExprNode conditionexpr;
    public StmtNode thenstmt;
    public StmtNode elseStmt;

    public IfStmtNode(ExprNode condition, StmtNode thenstmt, StmtNode elsestmt, Position pos) {
        super(pos);
        this.condition = condition;
        this.thenstmt = thenstmt;
        this.elsestmt = elsestmt;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
