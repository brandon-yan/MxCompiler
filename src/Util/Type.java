package Util;

import Util.entity.FunctionEntity;
import Util.entity.VariableEntity;
import Util.error.SemanticError;
import Util.scope.*;

import java.util.HashMap;

public class Type {
    public enum type{
        INT, BOOL, STRING, VOID, CLASS, NULL
    }
    public type typename;
    public String classname;
    public HashMap<String, VariableEntity> members;
    public HashMap<String, FunctionEntity> methods;

    public Type() {
        this.typename = null;
        this.classname = null;
        this.members = new HashMap<>();
        this.methods = new HashMap<>();
    }

    public Type(String typename) {
        this.classname = typename;
        switch (typename) {
            case "int" -> this.typename = type.INT;
            case "bool" -> this.typename = type.BOOL;
            case "string" -> this.typename = type.STRING;
            case "void" -> this.typename = type.VOID;
            case "" -> this.typename = type.NULL;
            default -> this.typename = type.CLASS;
        }
        this.members = new HashMap<>();
        this.methods = new HashMap<>();
    }

    public void addMembers(VariableEntity member) {
        if (members.containsKey(member.name))
            throw new SemanticError("member redefine", new Position());
        else members.put(member.name, member);
    }

    public void addMethods(FunctionEntity method) {
        if (methods.containsKey(method.name))
            throw new SemanticError("method redefine", new Position());
        else methods.put(method.name, method);
    }

    public boolean containsMembers(String name) {
        return members.containsKey(name);
    }

    public boolean containsMethods(String name) {
        return methods.containsKey(name);
    }

    public VariableEntity getMember(String name) {
        if(containsMembers(name))
            return members.get(name);
        else return null;
    }

    public FunctionEntity getMethod(String name) {
        if(containsMethods(name))
            return methods.get(name);
        else return null;
    }
}
