package AST;

import Util.Position;

public class IfStmtNode extends StmtNode {
    public ExprNode conditionexpr;
    public StmtNode thenstmt;
    public StmtNode elsestmt;

    public IfStmtNode(ExprNode condition, StmtNode thenstmt, StmtNode elsestmt, Position pos) {
        super(pos);
        this.conditionexpr = condition;
        this.thenstmt = thenstmt;
        this.elsestmt = elsestmt;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
