package Util;

import Util.scope.*;

public class Type {
    public enum type{
        INT, BOOL, STRING, VOID, CLASS, NULL
    }
    public type typename;
    public String classname;
    public int dimension;
    public Scope classScope;

    public Type() {
        this.typename = null;
        this.classname = null;
        this.dimension = 0;
        this.classScope = null;
    }

    public Type(String typename, int dimension) {
        this.classname = typename;
        this.dimension = dimension;
        if (typename.equals("int"))
            this.typename = type.INT;
        else if (typename.equals("bool"))
            this.typename = type.BOOL;
        else if (typename.equals("string"))
            this.typename = type.STRING;
        else if (typename.equals("void"))
            this.typename = type.VOID;
        else if (typename.equals(""))
            this.typename = type.NULL;
        else this.typename = type.CLASS;
    }

    public void setClassScope(Scope classScope) {
        this.classScope = classScope;
    }

}
