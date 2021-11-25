package MIR;

import MIR.Operand.Operand;

import java.util.HashMap;

public class IdExprAddrMap {
    public HashMap<String, Operand> IdAddrMap;
    public IdExprAddrMap parentMap;

    public IdExprAddrMap(IdExprAddrMap parentMap) {
        this.parentMap = parentMap;
        IdAddrMap = new HashMap<>();
    }

    public void addIdAddr(String name, Operand addr) {
        IdAddrMap.put(name, addr);
    }

    public boolean containsIdAddr(String name) {
        if (IdAddrMap.containsKey(name)) return true;
        else if (parentMap != null)
            return parentMap.containsIdAddr(name);
        else return false;
    }

    public Operand getIdAddr(String name) {
        if (IdAddrMap.containsKey(name))
            return IdAddrMap.get(name);
        else if (parentMap != null)
            return parentMap.getIdAddr(name);
        else return null;
    }

}
