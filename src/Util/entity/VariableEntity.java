package Util.entity;

import AST.*;
import Util.Type;

public class VariableEntity extends Entity {
    public TypeNode vartype;
    public ExprNode init;


    public VariableEntity (String name, TypeNode vartype, ExprNode init) {
        super(name);
        this.vartype = vartype;
        this.init = init;
    }

}