import java.util.Vector;

public class Predicate {
    private Vector<Argument> arguments;
    private boolean isLastPredicate;
    private boolean isHeadPredicate;
    private boolean isNegated;
    private boolean isComparison;
    private String comparisonOperator;
    private String predName;
    private Argument rightOperand;
    private Argument leftOperand;

    public Predicate() {
        isLastPredicate = false;
        isHeadPredicate = false;
        isNegated = false;
        isComparison = false;
        predName = null;
        comparisonOperator = null;
    }

    // Setters
    public void setIsLastPredicate(boolean bool) {
        this.isLastPredicate = bool;
    }

    public void setIsHeadPredicate(boolean bool) {
        this.isHeadPredicate = bool;
    }

    public void setIsNegated(boolean bool) {
        this.isNegated = bool;
    }

    public void setIsComparison(boolean bool) {
        this.isComparison = bool;
    }

    public void setComparisonOperator(String str) {
        this.comparisonOperator = str;
    }

    public void setPredName(String str) {
        this.predName = str;
    }

    public void setArguments(Vector<Argument> args) {
        this.arguments = args;
    }

    public void setRightOperand(Argument rOp) {
        this.rightOperand = rOp;
    }

    public void  setLeftOperand(Argument lOp) {
        this.leftOperand = lOp;
    }

    // Getters
    public boolean getIsLastPredicate() {
        return this.isLastPredicate;
    }

    public boolean getIsHeadPredicate() {
        return this.isHeadPredicate;
    }

    public Vector<Argument> getArguments() {
        return this.arguments;
    }

    public Argument getRightOperand() {
        return this.rightOperand;
    }

    public Argument getLeftOperand() {
        return this.leftOperand;
    }

    public boolean getIsNegated() {
        return this.isNegated;
    }

    public boolean getIsComparison() {
        return this.isComparison;
    }

    public String getComparisonOperator() {
        return this.comparisonOperator;
    }

    public String getPredName() {
        return this.predName;
    }

    public String toString() {

        String predStr = null;

        if (this.isComparison) {
            if (this.isNegated) {
                predStr = "not" + this.leftOperand.toString() + " " + 
                    this.comparisonOperator + " " + this.rightOperand.toString()
                    + ")";
            } else {
                predStr = this.leftOperand.toString() + " " +
                    this.comparisonOperator + " " + 
                    this.rightOperand.toString();
            }
        } else {
            if (this.isNegated) {
                predStr = "not" + this.predName + "("; 
                for (int i =0; i < this.arguments.size(); i++) {
                    predStr += this.arguments.get(i).toString();
                    
                    if (i != (this.arguments.size()-1)) {
                        predStr += ",";
                    }
                }
                predStr += ")";
            } else {
                predStr = this.predName + "(";
                for (int i = 0; i < this.arguments.size(); i++) {
                    predStr += this.arguments.get(i).toString();

                    if (i != (this.arguments.size()-1)) {
                        predStr += ",";
                    }
                }
                predStr += ")";
            }
        }
        return predStr;
    }
}
