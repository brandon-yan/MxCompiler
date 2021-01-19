package AST;

import Util.Position;
import java.util.ArrayList;


public class ClassDeclNode extends ProgramDeclNode {
    public String identifier;
    public ArrayList<VarDeclNode> Varlist;
    public ArrayList<ConstructorDeclNode> Constructor;
    public ArrayList<FuncDeclNode> Funclist;

    public ClassDeclNode(String identifier, Position pos) {
        super(pos);
        this.Varlist = new ArrayList<>();
        this.identifier = identifier;
        this.Constructor = new ArrayList<>();
        this.Funclist = new ArrayList<>();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}