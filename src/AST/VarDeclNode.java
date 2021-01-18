package AST;

import Util.Position;
import java.util.ArrayList;

public class VarDeclNode extends ASTNode {
    public String name;
    public TypeNode type;
    public ExprNode init;

    public VarDeclNode(String name, TypeNode type, ExprNode init, Position pos) {
        super(pos);
        this.name = name;
        this.type = type;
        this.init = init;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean isAssignable() {return true;}
}
