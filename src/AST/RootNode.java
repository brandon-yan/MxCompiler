package AST;
import Util.Type;
import Util.Position;

import java.util.ArrayList;

public class RootNode extends ASTNode {
    public ArrayList<VarDeclNode> VarList;
    public ArrayList<FuncDeclNode> FuncList;
    public ArrayList<ClassDeclNode> ClassList;

    public RootNode(Position pos, Arraylist<VarDeclNode> varlist, Arraylist<FuncDeclNode> funclist, Arraylist<ClassDeclNode> classlist) {
        super(pos);
        this.VarList = new ArrayList<>();
        this.FuncList = new ArrayList<>();
        this.ClassList = new ArrayList<>();
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
