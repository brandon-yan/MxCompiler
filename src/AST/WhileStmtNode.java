package AST;

import Util.Position;

public class WhileStmtNode extends StmtNode {
    public ExprNode conditionexpr;
    public StmtNode whilestmt;

    public WhileStmtNode(ExprNode conditionexpr, StmtNode whilestmt, Position pos) {
        super(pos);
        this.conditionexpr = conditionexpr;
        this.forstmt = forstmt
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
