package AST;

import Util.Position;

import java.util.ArrayList;

public class LambdaExprNode extends ExprNode{

    public ArrayList<ExprNode> parameters;
    public BlockStmtNode suite;
    public VarListNode varlist;

    public LambdaExprNode(Position pos, ArrayList<ExprNode> parameters, BlockStmtNode suite, VarListNode varlist) {
        super(pos);
        this.parameters = parameters;
        this.suite = suite;
        this.varlist = varlist;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
