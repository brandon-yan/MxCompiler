package MIR;

import AST.TypeNode;
import Backend.IRVisitor;
import MIR.Instruction.*;
import MIR.Operand.*;
import MIR.TypeSystem.*;
import Util.Type;

import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

public class Module {
    public static IRType stringT = new PointerType(new IntType(8));
    public static IRType charT = new IntType(8);
    public static IRType i32T = new IntType(32);
    public static IRType boolT = new IntType(1);
    public static IRType voidT = new VoidType();

    public HashMap<String, Function> functions = new HashMap<>();
    public HashMap<String, ClassType> types = new HashMap<>();
    public HashMap<String, ConstString> constStrings = new HashMap<>();
    public HashMap<String, GlobalVariable> global = new HashMap<>();

    public Module() {
        Function printf = new Function("g_print");
        printf.retType = new FunctionType(voidT);
        printf.addParameter(new Parameter(stringT, "str"));
        printf.builtin = true;
        functions.put("g_print", printf);

        Function printlnf = new Function("g_println");
        printlnf.retType = new FunctionType(voidT);
        printlnf.addParameter(new Parameter(stringT, "str"));
        printlnf.builtin = true;
        functions.put("g_println", printlnf);

        Function printIntf = new Function("g_printInt");
        printIntf.retType = new FunctionType(voidT);
        printIntf.addParameter(new Parameter(i32T, "n"));
        printIntf.builtin = true;
        functions.put("g_printInt", printIntf);

        Function printlnIntf = new Function("g_printlnInt");
        printlnIntf.retType = new FunctionType(voidT);
        printlnIntf.addParameter(new Parameter(i32T, "n"));
        printlnIntf.builtin = true;
        functions.put("g_printlnInt", printlnIntf);

        Function getStringf = new Function("g_getString");
        getStringf.retType = new FunctionType(stringT);
        getStringf.builtin = true;
        functions.put("g_getString", getStringf);

        Function getIntf = new Function("g_getInt");
        getIntf.retType = new FunctionType(i32T);
        getIntf.builtin = true;
        functions.put("g_getInt", getIntf);

        Function toStringf = new Function("g_toString");
        toStringf.retType = new FunctionType(stringT);
        toStringf.addParameter(new Parameter(i32T, "i"));
        toStringf.builtin = true;
        functions.put("g_toString", toStringf);

        Function stringAdd = new Function("g_stringAdd");
        stringAdd.retType = new FunctionType(stringT);
        stringAdd.addParameter(new Parameter(stringT, "a"));
        stringAdd.addParameter(new Parameter(stringT, "b"));
        stringAdd.builtin = true;
        functions.put("g_stringAdd", stringAdd);

        Function stringEqual = new Function("g_stringEqual");
        stringEqual.retType = new FunctionType(boolT);
        stringEqual.addParameter(new Parameter(stringT, "a"));
        stringEqual.addParameter(new Parameter(stringT, "b"));
        stringEqual.builtin = true;
        functions.put("g_stringEqual", stringEqual);

        Function stringNotEqual = new Function("g_stringNotEqual");
        stringNotEqual.retType = new FunctionType(boolT);
        stringNotEqual.addParameter(new Parameter(stringT, "a"));
        stringNotEqual.addParameter(new Parameter(stringT, "b"));
        stringNotEqual.builtin = true;
        functions.put("g_stringNotEqual", stringNotEqual);

        Function stringLess = new Function("g_stringLess");
        stringLess.retType = new FunctionType(boolT);
        stringLess.addParameter(new Parameter(stringT, "a"));
        stringLess.addParameter(new Parameter(stringT, "b"));
        stringLess.builtin = true;
        functions.put("g_stringLess", stringLess);

        Function stringLessEqual = new Function("g_stringLessEqual");
        stringLessEqual.retType = new FunctionType(boolT);
        stringLessEqual.addParameter(new Parameter(stringT, "a"));
        stringLessEqual.addParameter(new Parameter(stringT, "b"));
        stringLessEqual.builtin = true;
        functions.put("g_stringLessEqual", stringLessEqual);

        Function stringGreat = new Function("g_stringGreat");
        stringGreat.retType = new FunctionType(boolT);
        stringGreat.addParameter(new Parameter(stringT, "a"));
        stringGreat.addParameter(new Parameter(stringT, "b"));
        stringGreat.builtin = true;
        functions.put("g_stringGreat", stringGreat);

        Function stringGreatEqual = new Function("g_stringGreatEqual");
        stringGreatEqual.retType = new FunctionType(boolT);
        stringGreatEqual.addParameter(new Parameter(stringT, "a"));
        stringGreatEqual.addParameter(new Parameter(stringT, "b"));
        stringGreatEqual.builtin = true;
        functions.put("g_stringGreatEqual", stringGreatEqual);

        Function init = new Function("g_init");
        init.retType = new FunctionType(voidT);
        //init.exit = init.entry;
        functions.put("g_init", init);

        Function malloc = new Function("g_malloc");
        malloc.retType = new FunctionType(stringT);
        malloc.addParameter(new Parameter(i32T, "n"));
        malloc.builtin = true;
        functions.put("g_malloc", malloc);
    }

    public IRType getIRType(TypeNode type) {
        if(type.dimension >= 1) {
            IRType tmpType = getIRType(type.type);
            for (int i = 0; i < type.dimension; ++i)
                tmpType = new PointerType(tmpType);
            return tmpType;
        }
        if(type.type.typename == Type.type.CLASS) {
            String tmpTypeName = type.typename;
            ClassType tmpType = types.get(tmpTypeName);
            return new PointerType(tmpType);
        }
        if (type.type.typename == Type.type.STRING)
            return new PointerType(new IntType(8));
        if (type.type.typename == Type.type.INT)
            return new IntType(32);
        if (type.type.typename == Type.type.BOOL)
            return new IntType(1);
        return new VoidType();
    }

    public IRType getIRType(Type type) {
        if(type.typename == Type.type.CLASS) {
            String tmpTypeName = type.classname;
            ClassType tmpType = types.get(tmpTypeName);
            return new PointerType(tmpType);
        }
        if (type.typename == Type.type.STRING)
            return new PointerType(new IntType(8));
        if (type.typename == Type.type.INT)
            return new IntType(32);
        if (type.typename == Type.type.BOOL)
            return new IntType(1);
        return new VoidType();
    }

    public void accept(IRVisitor it){
        it.visit(this);
    }
}