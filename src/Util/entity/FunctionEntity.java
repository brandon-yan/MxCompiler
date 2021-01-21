package Util.entity;

import AST.*;
import Util.Type;

import java.util.ArrayList;

public class FunctionEntity extends Entity {
    public enum FuncEntityType {
        function, method, constructor
    }
    public TypeNode functype;
    public ArrayList<VariableEntity> parameters;
    public StmtNode functionstmt;
    public FuncEntityType funcEntityType;

    public FunctionEntity (String name, TypeNode functype, ArrayList<VariableEntity> parameters, StmtNode functionstmt, FuncEntityType funcEntityType) {
        super(name);
        this.functype = functype;
        this.parameters = parameters;
        this.functionstmt = functionstmt;
        this.funcEntityType = funcEntityType;
    }
}