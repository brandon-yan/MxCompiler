package Util.scope;

import AST.*;
import Util.entity.FunctionEntity;
import Util.entity.VariableEntity;
import Util.error.SemanticError;
import Util.Position;

import java.util.HashMap;

public class Scope {

    public HashMap<String, VariableEntity> vars;
    public HashMap<String, FunctionEntity> funcs;
    public String classname;
    public Scope parentScope;

    public Scope(Scope parentScope) {
        vars = new HashMap<>();
        funcs = new HashMap<>();
        this.classname = null;
        this.parentScope = parentScope;
    }

    public Scope parentScope() {
        return parentScope;
    }

    public void defineVariable(VariableEntity entity, Position pos) {
        if (vars.containsKey(entity.name))
            throw new SemanticError("variable redefine", pos);
        else vars.put(entity.name, entity);
    }

    public boolean containsVariable(String name, boolean lookUpon) {
        if (vars.containsKey(name)) return true;
        else if (parentScope != null && lookUpon)
            return parentScope.containsVariable(name, true);
        else return false;
    }

    public void defineFunction(FunctionEntity entity, Position pos) {
        if (funcs.containsKey(entity.name))
            throw new SemanticError("function redefine", pos);
        else funcs.put(entity.name, entity);
    }

    public boolean containsFunction(String name, boolean lookUpon) {
        if (funcs.containsKey(name)) return true;
        else if (parentScope != null && lookUpon)
            return parentScope.containsFunction(name, true);
        else return false;
    }
}
