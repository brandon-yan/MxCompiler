package AST;

import Util.Type;
import Util.Position;

public class TypeNode extends ASTNode {
    public Type type;
    public String typename;
    public int dimension;

    public TypeNode(Position pos, String typename, int dimension) {
        super(pos);
        this.typename = typename;
        this.dimension = dimension;
        this.type = new Type(typename);
    }

    public String getTypename() {
        return typename;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}