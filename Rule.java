import java.util.Vector;
import java.util.HashMap;
import edu.gsu.cs.dbengine.*;

public class Rule {
    private Predicate headPredicate;
    private Vector<Predicate> bodyPredicates;
    private Vector<Predicate> regularBodyPredicates;
    private Vector<Predicate> comparisonBodyPredicates;

    public Rule() {
    }

    // Setters
    public void setHeadPredicate(Predicate p) {
        this.headPredicate = p;
    }

    public void setBodyPredicates(Vector<Predicate> bodyPreds) {
        this.bodyPredicates = bodyPreds; 
        this.regularBodyPredicates = new Vector<Predicate>();
        this.comparisonBodyPredicates = new Vector<Predicate>();

        for (int i = 0; i < bodyPreds.size(); i++) {
            if (bodyPreds.get(i).getIsComparison()) {
                this.comparisonBodyPredicates.add(bodyPreds.get(i));
            } else {
                this.regularBodyPredicates.add(bodyPreds.get(i));
            }
        }
    }

    // Getters
    public Predicate getHeadPredicate() {
        return this.headPredicate;
    }

    public Vector<Predicate> getBodyPredicates() {
        return this.bodyPredicates;
    }

    public Vector<Predicate> getRegularBodyPredicates() {
        return this.regularBodyPredicates;
    }

    public Vector<Predicate> getComparisonBodyPredicates() {
        return this.comparisonBodyPredicates;
    }

    public String toString() {

        String ruleStr;

        ruleStr = this.headPredicate.toString(); 
        for (int i = 0; i < bodyPredicates.size(); i++) {
            ruleStr += bodyPredicates.get(i).toString();
        }
        return ruleStr;
    }

    public boolean checkRepeatedVariablesInRule(HashMap<String, 
            IDBPredicate> idbPredicateData) {

        Predicate headPred = this.getHeadPredicate();
         
        IDBPredicate idbPred = idbPredicateData.get(headPred.getPredName());

        Vector<Predicate> regularBodyPredicates = this.getRegularBodyPredicates();

        Vector<String> argDataType = idbPred.getArgDataType();

        Vector<Argument> headPredArguments = headPred.getArguments();

        for (int i = 0; i < headPredArguments.size(); i++) {

            Argument headPredArg = headPredArguments.get(i);

            String argType = argDataType.get(i);

            // Degbugging
            System.out.println("\n\nArgument: " + headPredArg.toString() +
                    " is of type: " + argType + "\n\n");

            if (!headPredArg.getIsConstant()) {

                boolean headPredVarsAgainstRegBodyVarsCheckOut = 
                    headPredVarsAgainstRegBodyVars(argType, headPredArg,
                            idbPredicateData, regularBodyPredicates);

                if (!headPredVarsAgainstRegBodyVarsCheckOut) {

                    return false;
                } else {
                    
                    return true;
                }
            }
        }

        return true;
    }

    private boolean headPredVarsAgainstRegBodyVars(String argType, Argument headPredArg, 
            HashMap<String, IDBPredicate> idbPredicateData, Vector<Predicate>
            regularBodyPredicates) {

        for (int i = 0; i < regularBodyPredicates.size(); i++) {

            Predicate regularBodyPredicate = regularBodyPredicates.get(i);

            Vector<Argument> regularBodyArgs = regularBodyPredicate.getArguments();

            for (int j = 0; j < regularBodyArgs.size(); j++) {

                Argument regularBodyArg = regularBodyArgs.get(j);

                if (!(regularBodyArg.getIsConstant()) && 
                        (headPredArg.toString().equals(regularBodyArg.toString()))) {

                    // Debugging
                    System.out.println("\n\n" + headPredArg.toString() + " equals: " 
                            + regularBodyArg.toString());

                    if (idbPredicateData.containsKey(regularBodyPredicate.getPredName())) {

                        // Debugging
                        System.out.println("\n\n" + regularBodyPredicate.getPredName() +
                            " is a regular body Predicate.\n\n");

                        IDBPredicate regularBodyIDB = idbPredicateData.get(
                            regularBodyPredicate.getPredName());

                        Vector<String> regularBodyArgTypes = regularBodyIDB.getArgDataType();

                        String regularArgDataType = regularBodyArgTypes.get(j);

                        if (!argType.toUpperCase().equals(regularArgDataType.toUpperCase())) {
                            
                            System.out.println("\nError: Variable: " + headPredArg + " in head " 
                                + "Predicate does not equal variable: " + regularBodyArg +
                                "; found: " + regularArgDataType + "; expected: " + argType);

                            return false;
                        }

                    } else {

                        Relation r = Relation.getRelation(
                            regularBodyPredicate.getPredName().toUpperCase());

                        Vector<String> attrs = r.getAttributes();

                        String regularArgDataType = attrs.get(j);


                        if (!argType.toUpperCase().equals(regularArgDataType.toUpperCase())) {

                            System.out.println("\nError: Variable: " + headPredArg + " in head " 
                                + "Predicate does not equal variable: " + regularBodyArg +
                                "; found: " + regularArgDataType + "; expected: " + argType);

                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }














        
}
