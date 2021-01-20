package Util.scope;

import AST.TypeNode;
import Util.Position;
import Util.Type;
import Util.entity.FunctionEntity;
import Util.entity.VariableEntity;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalScope extends Scope {

    public HashMap<String, Type> typeTable;

    public GlobalScope() {
        super(null);
        typeTable = new HashMap<>();
        typeTable.put("int", new Type("int", 0));
        typeTable.put("bool", new Type("bool", 0));
        typeTable.put("string", new Type("string", 0));
        typeTable.put("void", new Type("void", 0));
        typeTable.put("null", new Type("null", 0));

        Position pos = new Position();
        ArrayList<VariableEntity> parameters = new ArrayList<>();
        parameters.add(new VariableEntity("str", new TypeNode(pos,"string", 0), null, VariableEntity.VarEntityType.parameter));
        FunctionEntity builtin = new FunctionEntity("print", new TypeNode(pos, "void", 0), parameters, null, FunctionEntity.FuncEntityType.function);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        parameters.add(new VariableEntity("str", new TypeNode(new Position(),"string", 0), null, VariableEntity.VarEntityType.parameter));
        builtin = new FunctionEntity("println", new TypeNode(new Position(), "void", 0), parameters, null, FunctionEntity.FuncEntityType.function);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        parameters.add(new VariableEntity("n", new TypeNode(new Position(),"int", 0), null, VariableEntity.VarEntityType.parameter));
        builtin = new FunctionEntity("printInt", new TypeNode(new Position(), "void", 0), parameters, null, FunctionEntity.FuncEntityType.function);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        builtin = new FunctionEntity("getString", new TypeNode(new Position(), "string", 0), parameters, null, FunctionEntity.FuncEntityType.function);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        builtin = new FunctionEntity("getInt", new TypeNode(new Position(), "int", 0), parameters, null, FunctionEntity.FuncEntityType.function);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        parameters.add(new VariableEntity("n", new TypeNode(new Position(),"int", 0), null, VariableEntity.VarEntityType.parameter));
        builtin = new FunctionEntity("toString", new TypeNode(new Position(), "string", 0), parameters, null, FunctionEntity.FuncEntityType.function);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        builtin = new FunctionEntity("length", new TypeNode(new Position(), "int", 0), parameters, null, FunctionEntity.FuncEntityType.method);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        parameters.add(new VariableEntity("left", new TypeNode(new Position(),"int", 0), null, VariableEntity.VarEntityType.parameter));
        parameters.add(new VariableEntity("right", new TypeNode(new Position(),"int", 0), null, VariableEntity.VarEntityType.parameter));
        builtin = new FunctionEntity("substring", new TypeNode(new Position(), "string", 0), parameters, null, FunctionEntity.FuncEntityType.method);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        builtin = new FunctionEntity("parseInt", new TypeNode(new Position(), "int", 0), parameters, null, FunctionEntity.FuncEntityType.method);
        defineFunction(builtin, pos);

        parameters = new ArrayList<>();
        parameters.add(new VariableEntity("pos", new TypeNode(new Position(),"int", 0), null, VariableEntity.VarEntityType.parameter));
        builtin = new FunctionEntity("ord", new TypeNode(new Position(), "pos", 0), parameters, null, FunctionEntity.FuncEntityType.method);
        defineFunction(builtin, pos);
    }

}