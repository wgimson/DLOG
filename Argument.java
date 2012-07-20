
public class Argument {
    private boolean isConstant;    // true if constant argument
    private boolean isUnderscore;      // true if _argument
    private String argDataType;     // data type for argument : "NUMBER" or 
                                    // "STRING"
    private String argName;    // name of argument - if variable or if "*" 
                               // or "#"
    private String argValue;    // value of argument in case of constant argument

    public Argument() {
        isConstant = false;
        isUnderscore = false;
        argDataType = null;
        argName = null;
        argValue = null;
    }

    // Setters
    public void setIsConstant(boolean bool) {
        this.isConstant = bool;
    }

    public void setIsUnderscore(boolean bool) {
        this.isUnderscore = bool;
    }

    public void setArgDataType(String str) {
        this.argDataType = str;
    }

    public void setArgName(String str) {
        this.argName = str;
    }
     
    public void setArgValue(String val) {
        this.argValue = val;
    }

    // Getters
    public boolean getIsConstant() {
        return this.isConstant;
    }

    public boolean getIsUnderscore() {
        return this.isUnderscore;
    }

    public String getArgDataType() {
        return this.argDataType;
    }

    public String getArgName() {
        return this.argName;
    }

    public String getArgValue() {
        return this.argValue;
    }

    public String toString() {

        String argStr;

        if (this.isConstant) {
            argStr = this.argValue;
        } else {
            argStr = this.argName;
        }
        return argStr;
    }

    @Override
    public boolean equals(Object arg) {

        if (this.toString().equals(((Argument) arg).toString())) {
            return true;
        } else {
            return false;
        }
    }
}
