package AST;

import Util.Position;
import java.util.ArrayList;


public class ClassDeclNode extends ProgramDeclNode {
    public String identifier;
    public ArrayList<VarDeclNode> Varlist;
    public FuncDeclNode Constructor;
    public ArrayList<FUncDeclNode> Funclist;

    public VarDeclNode(String identifier, ArrayList<VarDeclNode> varlist, FuncDeclNode constructor, ArrayList<FUncDeclNode> Funclist, Position pos) {
        super(pos);
        this.Varlist = new ArrayList()<>;
        this.identifier = identifier;
        this.Constructor = constructor;
        this.Funclist = new ArrayList()<>;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}