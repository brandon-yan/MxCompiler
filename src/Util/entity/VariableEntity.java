package Util.entity;

import AST.*;
import Util.Type;

public class VariableEntity extends Entity {
    public enum VarEntityType {
        global, local, parameter, member
    }
    public TypeNode vartype;
    public ExprNode init;
    public VarEntityType varEntityType;


    public VariableEntity (String name, TypeNode vartype, ExprNode init, VarEntityType varEntityType) {
        super(name);
        this.vartype = vartype;
        this.init = init;
        this.varEntityType = varEntityType;
    }

}