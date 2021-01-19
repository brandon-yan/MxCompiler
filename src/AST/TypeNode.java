package AST;

import Util.Type;
import Util.Position;

public class TypeNode extends ASTNode {
    public String Typename;

    public TypeNode(Position pos, String typename) {
        super(pos);
        this.Typename = typename;
    }

    public String getTypename() {
        return Typename;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}