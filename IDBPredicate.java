// Phase 2 Class
// comparison is done on stratum value
import java.util.Vector;

public class IDBPredicate implements Comparable {

    private String predicateName;
    private Vector<Rule> rules;
    private Vector<String> argDataType;
    private int stratum;

    public IDBPredicate(String predName, Vector<Rule> rules) {
        this.predicateName = predName;
        this.rules = rules;
        this.stratum = 0;
        this.argDataType = new Vector<String>();
    }

    // Getters
    public String getPredicateName() {
        return this.predicateName;
    }

    public Vector<Rule> getRules() {
        return this.rules;
    }

    public String getArgDataType(int i) {
        return this.argDataType.get(i);
    }

    public Vector<String> getArgDataType() {
        return this.argDataType;
    }

    public int getStratum() {
        return this.stratum;
    }

    // Setters
    public void setPredicateName(String predName) {
        this.predicateName = predName;
    }

    public void setRules(Vector<Rule> rules) {
        this.rules = rules;
    }

    public void setArgDataType(String argType) {
        this.argDataType = new Vector<String>();
        this.argDataType.add(argType);
    }

    public void setStratum(int stm) {
        this.stratum = stm;
    }

    public void incrementStratum() {
        this.stratum += 1;
    }

    // Implement Comparable
    public int compareTo(Object idbPred) {
        return (this.getStratum() - ((IDBPredicate) idbPred).getStratum());
    }
}
