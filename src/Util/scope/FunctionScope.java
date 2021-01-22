package Util.scope;

import Util.Position;
import Util.entity.VariableEntity;

import java.util.ArrayList;

public class FunctionScope extends Scope {

    public ArrayList<VariableEntity> parameters;

    public FunctionScope(Scope parentScope) {
        super(parentScope);
        this.parameters = new ArrayList<>();
    }

    public void defineParameter(VariableEntity entity, Position pos) {
        this.parameters.add(entity);
        defineVariable(entity, pos);
    }

}