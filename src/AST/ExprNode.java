package AST;

import MIR.BasicBlock;
import MIR.Operand.Operand;
import Util.Type;
import Util.Position;

public abstract class ExprNode extends ASTNode {
    public TypeNode type;

    public Operand ExprRet = null;
    public Operand ExprLRet = null;

    public BasicBlock trueBlock = null;
    public BasicBlock falseBlock = null;

    public ExprNode(Position pos) {
        super(pos);
    }

    public boolean isAssignable() {
        return false;
    }

}
