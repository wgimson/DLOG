import java.util.Vector;
import java.util.HashMap;
import java.util.*;
import edu.gsu.cs.dbengine.*;

public class Program {

    static String unmatchedVariable;
    private Vector<Rule> rules;
    private Vector<String> idbPredicateNamesOrderedByStrata;
    private HashMap<String, IDBPredicate> idbPredicateData;

    public Program() {
    }

    // Getters
    public Vector<Rule> getRules() {
        return this.rules;
    }

    public HashMap<String, IDBPredicate> getIdbPredicateData() {
        return this.idbPredicateData;
    }

    public Vector<String> getIdbPredicateNamesOrderedByStrata() {
        return this.idbPredicateNamesOrderedByStrata;
    }

    // Setters
    public void setRules(Vector<Rule> rules) {
        this.rules = rules;
    }

    public String toString() {
        try {
            String str = "\nThe Program contains " + rules.size() + " rules.\n";
            return str;
        } catch (Exception e) {
            String str = "\nThe program contains no rules.\n";
            return str;
        }
    }

    public boolean safetyCheck() {

        Vector<Rule> rules = this.getRules();

        for (int i = 0; i < rules.size(); i++) {

            Rule rule = rules.get(i);

            Predicate headPred = rule.getHeadPredicate();

            Vector<Predicate> regularBodyPredicates = 
                rule.getRegularBodyPredicates();
    
            Vector<Argument> headPredArgs = headPred.getArguments();
            Vector<Argument> headPredVariableArgs = new Vector<Argument>();

            for (int j = 0; j < headPredArgs.size(); j++) {
                if (headPredArgs.get(j).toString().matches("^[_A-Z].*")) {
                    headPredVariableArgs.add(headPredArgs.get(j));
                }
                if (headPredArgs.get(j).getIsUnderscore()) {
                    System.out.println("Error: anonymous variable(" +
                            headPredArgs.get(j) + 
                            ") found in head Predicate.\n");
                    return false;
                }
            }

            Vector<Argument> regularBodyPredicateVariableArgs = 
                new Vector<Argument>();
            for (int j = 0; j < regularBodyPredicates.size(); j++) {
                Predicate regularBodyPredicate = regularBodyPredicates.get(j);

                Vector<Argument> regularBodyPredicateArgs = 
                    regularBodyPredicate.getArguments();

                for (int k = 0; k < regularBodyPredicateArgs.size(); k++) {
                    if (regularBodyPredicateArgs.get(k).
                            toString().matches("^[_A-Z].*")) {
                                regularBodyPredicateVariableArgs.
                        add(regularBodyPredicateArgs.get(k));
                    }
                }
            }

            boolean variablesAreMatched = matchVariables(headPredVariableArgs, 
                regularBodyPredicateVariableArgs);

            if (!variablesAreMatched) {
                System.out.println("Error: variable(" + unmatchedVariable +
                    ") found in head Predicate does not\n" +
                    "       appear in regular body Predicates.\n");
                return false;
            }

            boolean negatedCheck = checkNegatedBodyPredicates(rule);
            if (!negatedCheck) {
                return false;
            }
        }    
        return true;
    }

    private static boolean matchVariables(Vector<Argument> headPredArgs, 
        Vector<Argument> bodyPredArgs) {

        boolean match = false;

        for (int i = 0; i < headPredArgs.size(); i++) {
            for (int j = 0; j < bodyPredArgs.size(); j++) {
                if (headPredArgs.get(i).toString()
                    .equals(bodyPredArgs.get(j).toString())) {
                    match = true;
                }

            }
            if (!match) {
                unmatchedVariable = headPredArgs.get(i).toString();
                return false;
            }
            match = false;
        }
        return true;
    }

    private boolean checkNegatedBodyPredicates(Rule rule) {

        Vector<Predicate> negatedBodyPredicates = new Vector<Predicate>();
        Vector<Predicate> positiveRegularBodyPredicates = 
            new Vector<Predicate>(); 

        Vector<Predicate> bodyPredicates = rule.getBodyPredicates();
        for (int i = 0; i < bodyPredicates.size(); i++) {
            Predicate bodyPred = bodyPredicates.get(i);
            if (bodyPred.getIsNegated()) {
                negatedBodyPredicates.add(bodyPredicates.get(i));
            }
        }

        Vector<Predicate> regularBodyPredicates = 
            rule.getRegularBodyPredicates();
        for (int i = 0; i < regularBodyPredicates.size(); i++) {
            Predicate regularBodyPredicate = regularBodyPredicates.get(i);
            if (!regularBodyPredicate.getIsNegated()) {
                positiveRegularBodyPredicates.
                    add(regularBodyPredicate);
            }
        }

        Vector<Argument> negatedBodyPredicateArguments = 
            new Vector<Argument>();
        Vector<Argument> negatedPredicateVariables = new Vector<Argument>();
        for (int i = 0; i < negatedBodyPredicates.size(); i++) {
            Predicate negatedBodyPredicate = negatedBodyPredicates.get(i);
            if (negatedBodyPredicate.getIsComparison()) {
                negatedBodyPredicateArguments.add(negatedBodyPredicate.
                        getLeftOperand());
                negatedBodyPredicateArguments.add(negatedBodyPredicate.
                        getRightOperand());
            } else {
                negatedBodyPredicateArguments = negatedBodyPredicate.
                        getArguments();
            }
            for (int j = 0; j < negatedBodyPredicateArguments.size(); j++) {
               
                Argument negatedBodyPredicateArgument = 
                    negatedBodyPredicateArguments.get(j);
                if (negatedBodyPredicateArgument.toString().matches
                        ("^[_A-Z].*")) {
                            negatedPredicateVariables.add(
                                negatedBodyPredicateArgument);
                }
            }
        }

        for (int i = 0; i < negatedPredicateVariables.size(); i++) {
            Argument negatedPredicateVariable = negatedPredicateVariables.get(i);
            if (negatedPredicateVariable.toString().matches("^[_].*")) {
                System.out.println("Error: anonymous variable(" +
                    negatedPredicateVariable + ") found in negated " +
                    "body Predicate.\n");
                return false;
            }
        }


        Vector<Argument> positiveRegularBodyPredicateVariables = 
            new Vector<Argument>();
        for (int i = 0; i < positiveRegularBodyPredicates.size(); i++) {
            Predicate positiveRegularBodyPredicate = 
                positiveRegularBodyPredicates.get(i);
            Vector<Argument> positiveRegularBodyPredicateArguments = 
                positiveRegularBodyPredicate.getArguments();
            for (int j = 0; j < positiveRegularBodyPredicateArguments.size();
                j++) {
                Argument positiveRegularBodyPredicateArgument = 
                positiveRegularBodyPredicateArguments.get(j);
                
                if (positiveRegularBodyPredicateArgument.toString().matches
                    ("^[_A-Z].*")) {
                    positiveRegularBodyPredicateVariables.add
                        (positiveRegularBodyPredicateArgument);
                }
            }
        }        

        boolean matchCount = false;
        for (int i = 0; i < negatedPredicateVariables.size(); i++) {
            String testVariable1 = negatedPredicateVariables.get(i).toString();
            for (int j = 0; j < positiveRegularBodyPredicateVariables.size();
                    j++) {
                String testVariable2 =
                    positiveRegularBodyPredicateVariables.get(j).toString();
                if (testVariable1.equals(testVariable2)) {
                    matchCount = true;
                }
            }

            if (!matchCount) {
                System.out.println("Error: variable(" + testVariable1 + 
                        ") found in negated body Predicate which\n" +
                        "       does not exist in positive regular body" +
                        " Predicates.\n");
                return false;
            }
            matchCount = false;
        }


        return true;
    }

          
    public boolean answerPredicateCheck() {
        // We must ensure that 'answer' appears in at least one head of a Rule
        // and does not appear in any body of a Rule, so the first step is
        // to retrieve all the Rules for the Program
        Vector<Rule> rules = this.getRules();

        // These will keep track of how many 'answer' Predicates are encountered
        // both in the body Predicates and in the head Predicate
        int answerHeadCount = 0, answerBodyCount = 0;

        // Now first we will concern ourselves with the Rule heads - the 
        // simplest thing to do seems to be to loop through each Rule in rules,
        // get it's head Predicate, and put all of these into a Vector of 
        // head Predicates - then we can simply loop through these and make
        // sure that 'answer' appears at least once
        Vector<Predicate> ruleHeads = new Vector<Predicate>();

        for (int i = 0; i < rules.size(); i++) {
            Predicate ruleHead = rules.get(i).getHeadPredicate();
            ruleHeads.add(ruleHead);
        }

        for (int i = 0; i < ruleHeads.size(); i++) {
            String ruleHeadName = ruleHeads.get(i).getPredName().
                toUpperCase().trim();
            if (ruleHeadName.equals("ANSWER")) {
                answerHeadCount++;
            }
        }

        // If answerHeadCount <= 0, that means that there is no answer Predicate
        // in any Rule head Predicate in the Program, and this is a semantic
        // error
        if (answerHeadCount <= 0) {
            System.out.println("Error: 'answer' does not appear in any head "
                + "Predicate.\n");
            return false;
        }
        
        // Now we'll check to make sure that no 'answer' predicate is present 
        // in the body of any Rule in the Program - the best way to do this is
        // to use the rules Vector we already made, loop through it and pull
        // out all the body Predicates, putting them in a Vector - then we'll
        // loop through this and make sure that none use 'answer' for their
        // name
        Vector<Predicate> ruleBodies = new Vector<Predicate>();

        // Since each call to getBodyPredicates() on a Rule will return a 
        // Vector<Predicate>, and we're calling this on each Rule in the 
        // program, we must use the Vector class' addAll() method each time
        // we put the elements of a body Predicate Vector into ruleBodies
        for (int i = 0; i < rules.size(); i++) {
            ruleBodies.addAll(rules.get(i).getBodyPredicates());
        }

        for (int i = 0; i < ruleBodies.size(); i++) {
            if (!ruleBodies.get(i).getIsComparison()) {
                String ruleBodyName = ruleBodies.get(i).getPredName()
                    .toUpperCase().trim();
                if (ruleBodyName.equals("ANSWER")) {
                    answerBodyCount++;
                }
            }
        }

        // If answerBodyCount > 0, that means we have an answer Predicate in a
        // Rule body, which is a semantic error
        if (answerBodyCount > 0) {
            System.out.println("Error: 'answer' appears in body Predicate.\n");
            return false;
        }

        return true;
    }

    public boolean regularBodyPredicateCheck() {

        // Like so much of this program, the process begins with getting all the
        // rules out of the Program
        Vector<Rule> rules = this.getRules();

        // Now we need to get all head and regular body Predicates into 
        // respective Vectors
        Vector<Predicate> ruleHeadPredicates = new Vector<Predicate>();
        Vector<Predicate> ruleRegularBodyPredicates = new Vector<Predicate>();
        for (int i = 0; i < rules.size(); i++) {
            ruleHeadPredicates.add(rules.get(i).getHeadPredicate());
            ruleRegularBodyPredicates.addAll(rules.get(i)
                    .getRegularBodyPredicates());
        }

        // Now we must go through and check each regular body Predicate name 
        // against those of the head Predicates - if there is no match, we 
        // move on to the database
        Vector<String> headPredicateNames = new Vector<String>();
        Vector<String> regularBodyPredicateNames = new Vector<String>();

        // Both head Predicate names and regular body Predicate names must be 
        // stored in all upper case, because this is how Relation names are 
        // stored in the database
        for (int i = 0; i < ruleHeadPredicates.size(); i++) {
            String headPredicateName = ruleHeadPredicates.get(i).
                getPredName().toUpperCase();
            headPredicateNames.add(headPredicateName);
        }

        // Again, all regular body Predicate names must be stored in all upper
        // case form
        for (int i = 0; i < ruleRegularBodyPredicates.size(); i++) {
            String regularBodyPredicateName = ruleRegularBodyPredicates.get(i)
                .getPredName().toUpperCase();
            regularBodyPredicateNames.add(regularBodyPredicateName);
        }
        
        // Now we have a Vector of head Predicate names and a Vector of regular
        // body Predicate names, ready to be compared to eachother in a nested
        // for-loop
        for (int i = 0; i < regularBodyPredicateNames.size(); i++) {
            int nameMatchCount = 0;
            for (int j = 0; j < headPredicateNames.size(); j++) {
                if (regularBodyPredicateNames.get(i).equals(headPredicateNames
                        .get(j))) {
                    nameMatchCount++;
                }
            }
            
            // If no matches occurred above, the next step is to check the offeding
            // regular body Predicate name against the Relations in the database
            if (nameMatchCount <= 0) {
                String unmatchedPredName = regularBodyPredicateNames.get(i);

                // Now we take the passed Relation which was null before and 
                // instantiate it to be equivalent to the criteria for which we 
                // are checking - then we can test whether such a Relation exists
                // in the database - a Relation is equivalent to a Predicate
                if (!Relation.relationExists(unmatchedPredName.toUpperCase())) {
                    System.out.println("\nError: regular body Predicate" +
                            " '" + unmatchedPredName.toLowerCase() + 
                            "' not found in head Predicates or database.\n");
                    return false;
                }

            }
        }

        return true;
    }

    // Here we're checking that no head Predicate appears in the database, 
    // and that arity for all head Predicates is the same
    public boolean headPredicateCheck() {
        // First we'll get all the Rules out of the Program object and into 
        // a Vector
        Vector<Rule> rules = this.getRules();

        // Now we'll get only the head Predicates and put them in a Vector
        Vector<Predicate> headPredicates = new Vector<Predicate>();
        for (int i = 0; i < rules.size(); i++) {
            headPredicates.add(rules.get(i).getHeadPredicate());
        }

        // Relations exist in the database as Predicate names in capital letters
        //  so we must extract these and check them against what is in the 
        //  database
        Vector<String> headPredicateNames = new Vector<String>();
        for (int i = 0; i < headPredicates.size(); i++) {
            headPredicateNames.add(headPredicates.get(i).getPredName().
                    toUpperCase());
        }

        // Now, for each head Predicate name in headPredicateNames, we must 
        // ensure that it does not exist in the database - if it does, this
        // is a syntax error
        for (int i = 0; i < headPredicateNames.size(); i++) {
            if (Relation.relationExists(headPredicateNames.get(i))) {
                System.out.println("Error: Relation '" + 
                        headPredicateNames.get(i) + "' already exists in "
                        + "database.\n");
                return false;
            }
        }

        return true;
    }

    public boolean arityCheck() {

        Vector<Rule> rules = this.getRules();

        Vector<Predicate> headAndRegularBodyPredicates = new Vector<Predicate>();

        for (int i = 0; i < rules.size(); i++) {

            Rule rule = rules.get(i);

            Predicate headPred = rule.getHeadPredicate();

            Vector<Predicate> regularBodyPredicates = rule.getRegularBodyPredicates();

            headAndRegularBodyPredicates.add(headPred);

            headAndRegularBodyPredicates.addAll(regularBodyPredicates);
        }

        boolean aritiesChecked = this.checkIDBArities(headAndRegularBodyPredicates);

        if (!aritiesChecked) {

            return false;
        } else {

            return true;
        }
    }

    private boolean checkIDBArities(Vector<Predicate> headAndRegularBodyPredicates) {

        Vector<Rule> rules = this.getRules();

        Vector<Predicate> headPreds = new Vector<Predicate>();

        for (Rule rule : rules) {

            Predicate headPred = rule.getHeadPredicate();

            headPreds.add(headPred);
        }

        for (Predicate pred : headAndRegularBodyPredicates) {

            boolean match = false;

            for (Predicate headPred : headPreds) {

                if (pred.getPredName().equals(headPred.getPredName())) {

                    match = true;

                    Vector<Argument> predArgs = pred.getArguments();

                    Vector<Argument> headPredArgs = headPred.getArguments();

                    if ((predArgs.size()) != (headPredArgs.size())) {

                        System.out.println("Error: arity mismatch for IDB Predicate: "
                            + pred.getPredName());

                        return false;
                    }
                }
            }

            if (!match) {

                Vector<Argument> predArgs = pred.getArguments();

                Relation r = Relation.getRelation(pred.getPredName().toUpperCase());

                Vector<String> attributeLength = r.getAttributes();

                if ((attributeLength.size()) != (predArgs.size())) {

                    System.out.println("Error: arity mismatch for EDB Predicate: " 
                        + pred.getPredName());

                    return false;
                }
            }
        }
        return true;
    }







    public boolean recursiveQueryCheck() {

        this.idbPredicateData = 
            new HashMap<String, IDBPredicate>();
        this.idbPredicateNamesOrderedByStrata = new Vector<String>();
        //Vector<IDBPredicate> idbPredicatesOrderedByStrata = null;

        // Here we will populate the HashMap constructed above - first we go 
        // through every Rule and pull out the head Predicate; next we use the 
        // name of the head Predicate to construct an IDBPredicate object, since
        // that's what IDBPredicates are, and map it to it's name in the HashMap - 
        // the name is the key
        Vector<Rule> rules = this.getRules();
        for (int i = 0; i < rules.size(); i++) {
            Predicate headPred = rules.get(i).getHeadPredicate();
            String idbPredName = headPred.getPredName();
            IDBPredicate idbPred = new IDBPredicate(idbPredName, rules);


            // And put the newly constructed IDBPredicate into the HashMap
            idbPredicateData.put(idbPredName, idbPred);
        }


        // This is the algorithm to determine stratification, and whether or not
        // recursion is present - we begin by establishing a number, max, which no
        // IDBPredicate's stratum can exceed without indicating recursion - this number
        // is the size of the HashMap - in other words, if a head Predicate attains a
        // stratificatoin greater than the number of head Predicates, or Rules, in the 
        // Program, the we have a recursive Program
        int stratCount, max = idbPredicateData.size();
        do {
            stratCount = 0;

            // For each Rule we get the name of the head Predicate and use it to fetch
            // the analogous IDBPredicate from the HashMap - then we get the Vector of
            // regular body Predicates against which to calculate the head Predicate's 
            // stratum
            for (int i = 0; i < rules.size(); i++) {
                Rule rule = rules.get(i);
                Predicate headPred = rule.getHeadPredicate();
                String headPredName = headPred.getPredName();
                IDBPredicate headIDB = idbPredicateData.get(headPredName);
                Vector<Predicate> regularBodyPredicates = rule.getRegularBodyPredicates();

                // Then, for each regular body Predicate, if the HashMap contains a key to
                // it, i.e. if it is also a head Predicate elsewhere, then we use it's name
                // to fetch the analogous IDBPredicate object - since IDBPredicate implements
                // Comparable, we can use compareTo to check if one's stratum is larger than
                // the other - if compareTo returns an integer <= 1, then we must increment
                // the head Predicate's stratum
                for (int j = 0; j < regularBodyPredicates.size(); j++) {
                    Predicate regularBodyPredicate = regularBodyPredicates.get(j);
                    String regularBodyPredicateName = regularBodyPredicate.getPredName();
                    if (idbPredicateData.containsKey(regularBodyPredicateName)) {
                        IDBPredicate regularBodyIDB = 
                        idbPredicateData.get(regularBodyPredicateName);
                        if (headIDB.compareTo(regularBodyIDB) <= 1) {
                            headIDB.incrementStratum();
                            stratCount++;
                        }

                        if (headIDB.getStratum() > max) {
                            System.out.println("\nError: recursive query.\n");
                            return false;
                        }
                    }
                }
            }
        } while (stratCount != 0);
        
        Vector<IDBPredicate> idbPredicatesOrderedByStrata = new Vector(idbPredicateData.values());
        Collections.sort(idbPredicatesOrderedByStrata);
        for (int i = 0; i < idbPredicatesOrderedByStrata.size(); i++) {
            String idbName = idbPredicatesOrderedByStrata.get(i).getPredicateName();
            idbPredicateNamesOrderedByStrata.add(idbName);
        }
         
        return true;
    }

    public boolean getStratifiedIDBPredicate() {

        for (int i = 0; i < this.idbPredicateNamesOrderedByStrata.size(); i++) {
             
            String idbPredName = this.idbPredicateNamesOrderedByStrata.get(i);

            getStratifiedIDBPredicateMatch(idbPredName);

            IDBPredicate idb = idbPredicateData.get(idbPredName);
        }

        return true;
    }

    private void getStratifiedIDBPredicateMatch(String idbStratName) {

        IDBPredicate stratifiedIDBPredicate = idbPredicateData.get(idbStratName);

        Vector<Rule> rules = stratifiedIDBPredicate.getRules();

        for (int i = 0; i < rules.size(); i++) {

            Predicate ruleHead = rules.get(i).getHeadPredicate();

            if (ruleHead.getPredName().
                    equals(stratifiedIDBPredicate.getPredicateName())) {
                 
                getArgumentTypes(stratifiedIDBPredicate, ruleHead);
            }
        }
    } 
     
    private void getArgumentTypes(IDBPredicate idb, Predicate ruleHead) {

        Vector<Argument> ruleHeadArgs = ruleHead.getArguments();

        Vector<String> idbArgTypes = idb.getArgDataType();

        for (int i = 0; i < ruleHeadArgs.size(); i++) {

            Argument arg = ruleHeadArgs.get(i);

            if (arg.getIsConstant()) {

                idbArgTypes.add(arg.getArgDataType());

            } else { 

                String variableName = arg.toString();

                getIDBVariable(idb, variableName);
            }
        }
    }

    private void getIDBVariable(IDBPredicate idb, String variable) {

        Vector<Rule> rules = idb.getRules();
        Rule rule = null;

        for (int i = 0; i < rules.size(); i++) {

            Predicate headPred = rules.get(i).getHeadPredicate();

            if (headPred.getPredName().equals(idb.getPredicateName())) {

                rule = rules.get(i);
                break;
            }
        }

        if (rule != null) {

            Vector<Predicate> regularBodyPredicates = rule.getRegularBodyPredicates();

            for (int i = 0; i < regularBodyPredicates.size(); i++) {

                Predicate regularBodyPredicate = regularBodyPredicates.get(i);

                Vector<Argument> args = regularBodyPredicate.getArguments();

                for (int j = 0; j < args.size(); j++) {

                    Argument arg = args.get(j);

                    if (arg.toString().equals(variable)) {

                        if (idbPredicateData.containsKey(regularBodyPredicate.getPredName())) {

                            IDBPredicate idb2 = idbPredicateData.get(regularBodyPredicate.getPredName());
                            Vector<String> argTypes = idb2.getArgDataType();
                            String argType = argTypes.get(j);
                            idb.getArgDataType().add(argType);

                            return;
                        } else {

                            Relation r =
                                Relation.getRelation
                                (regularBodyPredicate.getPredName().toUpperCase());


                            Vector<String> relAttributes = r.getAttributes();

                            String aName = relAttributes.get(j);

                            String aType = 
                                Relation.attributeType
                                (regularBodyPredicate.getPredName().toUpperCase(), aName);

                            if (aType.equals("INTEGER") || aType.equals("DECIMAL")) {
                                idb.getArgDataType().add("NUMBER");
                            } else {
                                idb.getArgDataType().add("STRING");
                            }

                            return;
                        }
                    }
                }
            }
        }
    }

    public boolean checkAllConstantsInProgram() {

        Vector<Rule> rules = this.getRules();

        Vector<Predicate> headAndRegularBodyPredicates = new Vector<Predicate>();

        for (int i = 0; i < rules.size(); i++) {

            Rule rule = rules.get(i);

            headAndRegularBodyPredicates.add(rule.getHeadPredicate());

            headAndRegularBodyPredicates.addAll(rule.getRegularBodyPredicates());
        }

        for (int i = 0; i < headAndRegularBodyPredicates.size(); i++) {

            Predicate checkPred = headAndRegularBodyPredicates.get(i);
            
            boolean predCheckedAgainstAllOthers = 
                this.checkPredAgainstAllOthers(checkPred, headAndRegularBodyPredicates);

            if (!predCheckedAgainstAllOthers) {
                return false;
            }
        }

        return true;
    }

    private boolean checkPredAgainstAllOthers(Predicate checkPred, 
            Vector<Predicate> headAndRegularBodyPredicates) {

        Vector<Argument> args = checkPred.getArguments();

        for (int i = 0; i < args.size(); i++) {

            Argument arg = args.get(i);

            if (arg.getIsConstant()) {


                boolean argCheckedAgainstAllOthers = 
                    this.checkArgAgainstAllOthers(arg, checkPred,
                            headAndRegularBodyPredicates);

                if (!argCheckedAgainstAllOthers) {

                    return false;
                }
            }
        }

        return true;
    }

    private boolean checkArgAgainstAllOthers(Argument arg, Predicate checkPred,
            Vector<Predicate> headAndRegularBodyPredicates) {

        String argType = arg.getArgDataType();

        for (int i = 0; i < headAndRegularBodyPredicates.size(); i++) {

            Predicate pred = headAndRegularBodyPredicates.get(i);

            HashMap<String, IDBPredicate> idbPredicateData = this.getIdbPredicateData();

            if (idbPredicateData.containsKey(pred.getPredName())) {

                if (pred.getPredName().equals(checkPred.getPredName())) {

                   
                    Vector<Argument> predArguments = pred.getArguments();
                    Vector<Argument> checkPredArguments = checkPred.getArguments();

                    for (int j = 0; j < predArguments.size(); j++) {

                        Argument notArg = predArguments.get(j);
                        Argument checkArg = checkPredArguments.get(j);

                        if (checkArg.getIsConstant() && notArg.getIsConstant()) {

                            String notArgType = notArg.getArgDataType();

                            if (!notArgType.equals(checkArg.getArgDataType())) {

                                System.out.println("Error: constant " + checkArg + " in Predicate "
                                    + pred.getPredName() + " has incorrect\n       type; expected: "
                                    + notArgType + "; found: " + checkArg.getArgDataType());

                                return false;
                            } 
                        }
                    }
                }
            } else {

                Vector<Argument> arguments = pred.getArguments();

                for (int j = 0; j < arguments.size(); j++) {

                    Argument checkArg = arguments.get(j);

                    String checkArgType;

                    if (checkArg.getIsConstant() &&  
                            (arg.toString().equals(checkArg.toString()))) {

                        Relation r = Relation.getRelation(pred.getPredName().toUpperCase());

                        Vector<String> attrs = r.getAttributes();

                        if (Relation.attributeType(pred.getPredName().toUpperCase(),
                                    attrs.get(j)).equals("INTEGER") || 
                                Relation.attributeType(pred.getPredName().toUpperCase(),
                                    attrs.get(j)).equals("DECIMAL")) {
                            checkArgType = "NUMBER";
                        } else {
                            checkArgType = "STRING";
                        }

                        if (!argType.equals(checkArgType)) {

                            System.out.println("Error: constant " + checkArg + " in Predicate "
                                + pred.getPredName() + " has incorrect\n       type; expected: " + 
                                checkArgType + "; found: " + argType);

                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    public boolean checkAllVariablesInProgram() {

        Vector<Rule> rules = this.getRules();

        for (Rule rule : rules) {

            boolean allVariablesInRuleWereChecked = 
                checkAllVariablesInRule(rule);

            if (!allVariablesInRuleWereChecked){
                return false;
            }
        }

        return true;
    }

    private boolean checkAllVariablesInRule(Rule rule) {

        Vector<Predicate> headAndRegularBodyPredicates = 
            new Vector<Predicate>();

        HashMap idbPredicateData = this.getIdbPredicateData();

        LinkedList<IDBNode> idbNodes = new LinkedList<IDBNode>();

        headAndRegularBodyPredicates.add(rule.getHeadPredicate());
        headAndRegularBodyPredicates.addAll(rule.getRegularBodyPredicates());

        String nodeArgDataType;

        for (Predicate pred : headAndRegularBodyPredicates) {

            Vector<Argument> predArgs = pred.getArguments();

            for (int i = 0; i < predArgs.size(); i++) {

                Argument arg = predArgs.get(i);

                if (!arg.getIsConstant()) {

                IDBNode dummyIdb = new IDBNode(arg.getArgName());

                if (idbNodes.contains(dummyIdb)) {

                    if (idbPredicateData.containsKey(pred.getPredName())) {

                        IDBPredicate idbPred = 
                            (IDBPredicate) idbPredicateData.get(pred.getPredName());

                        Vector<String> argDataTypes = idbPred.getArgDataType();

                        String argDataType = argDataTypes.get(i);

                        if (!(argDataType.equals("NUMBER")) &&
                                !(argDataType.equals("STRING"))) {

                            if (argDataType.matches(".*DECIMAL.*") || 
                                argDataType.equals(".*INTEGER.*")) {

                                argDataType = "NUMBER";
                            } else {

                                argDataType = "STRING";
                            }
                        }

                        IDBNode idbNode = null;

                        for (int j = 0; j < idbNodes.size(); j++) {

                            idbNode = idbNodes.get(j);

                            if (idbNode.getName().equals(arg.getArgName())) {
                                break;
                            }
                        }

                        nodeArgDataType = idbNode.getArgDataType(); 

                        if (!nodeArgDataType.equals(argDataType)) {
                            System.out.println("Error: incorrect type for variable " + arg +
                                " in Predicate\n       " + pred + "; expected: "
                                + argDataType + "; found: " + nodeArgDataType);

                            return false;
                        }
                    } else {

                        Relation r = Relation.getRelation(pred.getPredName().toUpperCase());

                        Vector<String> attributes = r.getAttributes();

                        String attribute = attributes.get(i);

                        String attributeType = 
                            Relation.attributeType(pred.getPredName().toUpperCase(), attribute);

                        if (attributeType.matches(".*DECIMAL.*") ||
                                attributeType.matches(".*INTEGER.*")) {

                            attributeType = "NUMBER";
                        } else {

                            attributeType = "STRING";
                        }

                        IDBNode idbNode = null;

                        for (int j = 0; j < idbNodes.size(); j++) {

                            idbNode = idbNodes.get(j);

                            if (idbNode.getName().equals(arg.getArgName())) {
                                break;
                            }
                        }

                        nodeArgDataType = idbNode.getArgDataType(); 

                        if (!nodeArgDataType.equals(attributeType)) {

                            System.out.println("Error: incorrect type for variable: " + arg + 
                                    " in Predicate\n       " + pred + "; expected: "
                                    + attributeType + "; found: " + nodeArgDataType);

                            return false;
                        }
                    } 
                } else {

                    if (!arg.getIsConstant()) {

                        if (idbPredicateData.containsKey(pred.getPredName())) {

                            String argName = arg.toString();

                            IDBPredicate idb = 
                                (IDBPredicate) idbPredicateData.get(pred.getPredName());

                            String attributeType = idb.getArgDataType().get(i);

                            IDBNode idbNode = new IDBNode(argName, attributeType); 

                            idbNodes.add(idbNode);

                    } else {

                        String argName = arg.toString();

                        Relation r = Relation.getRelation(pred.getPredName().toUpperCase());

                        Vector<String> attributes = r.getAttributes();

                        String attribute = attributes.get(i);

                        String attributeType = 
                            Relation.attributeType(pred.getPredName().toUpperCase(),
                                attribute);

                        if (!attributeType.equals("NUMBER") && !attributeType.equals("STRING")) {

                            if (attributeType.matches(".*DECIMAL.*") || 
                                    attributeType.matches(".*INTEGER.*")) {

                                attributeType = "NUMBER";
                            } else { 

                                attributeType = "STRING";
                            }
                        }

                        IDBNode idbNode = new IDBNode(argName, attributeType);

                        idbNodes.add(idbNode);
                    } 
                    } 
                }
                } 
            } 
        } 

        return true;
    }
}
