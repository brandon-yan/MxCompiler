package Util.scope;

import Util.Position;
import Util.entity.VariableEntity;
import Util.Type;

import java.util.ArrayList;

public class ClassScope extends Scope {

    public ClassScope(Scope parentScope, String classname) {
        super(parentScope);
        this.classname = classname;
    }


}