package AST;
import Util.Type;
import Util.Position;

import java.util.ArrayList;

public class ProgramNode extends ASTNode {
    public ArrayList<ProgramDeclNode> sectionList;

    public ProgramNode(Position pos, Arraylist<ProgramDeclNode> sectionlist) {
        super(pos);
        this.sectionList = new ArrayList<>();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
