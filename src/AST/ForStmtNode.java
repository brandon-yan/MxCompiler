package AST;

import Util.Position;

public class ForStmtNode extends StmtNode {
    public ExprNode initexpr;
    public ExprNode conditionexpr;
    public ExprNode increaseexpr;
    public StmtNode forstmt;

    public ForStmtNode(ExprNode initexpr, ExprNode conditionexpr, ExprNode increaseexpr, StmtNode forstmt, Position pos) {
        super(pos);
        this.initexpr = initexpr;
        this.conditionexpr = conditionexpr;
        this.increaseexpr = increaseexpr;
        this.forstmt = forstmt;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
