package AST;
import Util.Type;
import Util.Position;

import java.util.ArrayList;

public class ProgramDeclNode extends ASTNode {

    public ProgramDeclNode(Position pos) {
        super(pos);
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
