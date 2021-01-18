package AST;

public interface ASTVisitor {
    void visit(RootNode it);

    void visit(VarDeclStmtNode it);
    void visit(ReturnStmtNode it);
    void visit(BlockStmtNode it);
    void visit(ExprStmtNode it);
    void visit(IfStmtNode it);

    void visit(AssignExprNode it);
    void visit(BinaryExprNode it);
    void visit(ConstExprNode it);
    void visit(CmpExprNode it);
    void visit(VarExprNode it);
}
