public class IDBNode {

    private String var, argDataType;

    public IDBNode(String variable, String argDataType) {

        this.var = variable;
        this.argDataType = argDataType;
    }

    public IDBNode(String variable) {

        this.var = variable;
    }

    public String getName() {
        return this.var;
    }

    public String getArgDataType() {
        return this.argDataType;
    }

    @Override
    public boolean equals(Object idbNode) {

        if (this.getName().equals(((IDBNode) idbNode).getName())) {

            return true;
        } else {

            return false;
        }
    }
}
