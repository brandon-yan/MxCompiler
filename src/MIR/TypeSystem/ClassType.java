package MIR.TypeSystem;

import java.util.ArrayList;

public class ClassType extends IRType {
    public int siz = 0;
    public String name;
    public ArrayList<IRType> memberType;
    public ArrayList<String> memberName;
    public ClassType(String name) {
        super();
        this.name = name;
        memberType = new ArrayList<>();
        memberName = new ArrayList<>();
    }

    public void addMember(IRType membertype, String membername) {
        memberType.add(membertype);
        memberName.add(membername);
        siz += membertype.size();
    }

    @Override
    public int size() {
        return siz;
    }
}