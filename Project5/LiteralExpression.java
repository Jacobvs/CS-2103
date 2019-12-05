public abstract class LiteralExpression implements Expression {
    private CompoundExpression parent;
    private String sVal;
    private double dVal;
    private boolean isNum;

    public LiteralExpression(String val){
        sVal = val;
        isNum = false;
    }

    public LiteralExpression(double val){
        dVal = val;
        isNum = true;
    }

    public Object getVal(){
        return isNum ? dVal : sVal;
    }

    @Override
    public CompoundExpression getParent() {
        return this.parent;
    }

    @Override
    public void setParent(CompoundExpression parent) {
        this.parent = parent;
    }

    @Override
    public Expression deepCopy() {
        return this;
    }

    //TODO check if needs to backtrack to flatten?
    @Override
    public void flatten() {
        return;
    }
}
