package Util.scope;

import AST.TypeNode;
import Util.Position;
import Util.Type;
import Util.entity.FunctionEntity;
import Util.entity.VariableEntity;
import Util.error.SemanticError;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalScope extends Scope {

    public HashMap<String, Type> typeTable;

    public GlobalScope() {
        super(null);
        typeTable = new HashMap<>();
        typeTable.put("int", new Type("int"));
        typeTable.put("bool", new Type("bool"));
        typeTable.put("string", new Type("string"));
        typeTable.put("void", new Type("void"));
        typeTable.put("null", new Type("null"));

        Position pos = new Position();
        ArrayList<VariableEntity> parameters = new ArrayList<>();
        parameters.add(new VariableEntity("str", new TypeNode(pos,"string", 0), null));
        FunctionEntity builtin = new FunctionEntity("print", new TypeNode(pos, "void", 0), parameters, null);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        parameters.add(new VariableEntity("str", new TypeNode(pos,"string", 0), null));
        builtin = new FunctionEntity("println", new TypeNode(pos, "void", 0), parameters, null);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        parameters.add(new VariableEntity("n", new TypeNode(pos,"int", 0), null));
        builtin = new FunctionEntity("printInt", new TypeNode(pos, "void", 0), parameters, null);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        parameters.add(new VariableEntity("n", new TypeNode(pos,"int", 0), null));
        builtin = new FunctionEntity("printlnInt", new TypeNode(pos, "void", 0), parameters, null);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        builtin = new FunctionEntity("getString", new TypeNode(pos, "string", 0), parameters, null);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        builtin = new FunctionEntity("getInt", new TypeNode(pos, "int", 0), parameters, null);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        parameters.add(new VariableEntity("n", new TypeNode(pos,"int", 0), null));
        builtin = new FunctionEntity("toString", new TypeNode(pos, "string", 0), parameters, null);
        defineFunction(builtin, pos);

    }

    public void defineClass (String name, Type type, Position pos) {
        if (typeTable.containsKey(name))
            throw new SemanticError("class redefine", pos);
        else typeTable.put(name, type);
    }

    public boolean containsType(String name) {
        return typeTable.containsKey(name);
    }

    public Type getType (String name) {
        if (containsType(name))
            return typeTable.get(name);
        else return null;
    }
}