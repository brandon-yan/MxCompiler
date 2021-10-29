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

}
