package AST;

public interface ASTVisitor {
    void visit(ProgramNode it);

    void visit(TypeNode it);

    void visit(VarDeclNode it);
    void visit(VarListNode it);
    void visit(VarNode it);
    void visit(FuncDeclNode it);
    void visit(ClassDeclNode it);
    void visit(ConstructorDeclNode it);

    void visit(BlockStmtNode it);
    void visit(ExprStmtNode it);
    void visit(IfStmtNode it);
    void visit(ForStmtNode it);
    void visit(WhileStmtNode it);
    void visit(BreakStmtNode it);
    void visit(ContinueStmtNode it);
    void visit(ReturnStmtNode it);

    void visit(AssignExprNode it);
    void visit(ArrayExprNode it);
    void visit(BinaryExprNode it);
    void visit(FuncCallExprNode it);
    void visit(IdentifierExprNode it);
    void visit(MemberExprNode it);
    void visit(MethodExprNode it);
    void visit(NewExprNode it);
    void visit(PrefixExprNode it);
    void visit(SuffixExprNode it);
    void visit(ThisExprNode it);

    void visit(IntLiteralNode it);
    void visit(BoolLiteralNode it);
    void visit(StringLiteralNode it);
    void visit(NullLiteralNode it);

    void visit(VarDeclStmtNode varDeclStmtNode);
}
