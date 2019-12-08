public class LiteralExpression implements Expression {
    private CompoundExpression parent;
    private String val;

    public LiteralExpression(String val, CompoundExpression parent){
        this.parent = parent;
        this.val = val;
    }

    public LiteralExpression(String val){
        this(val, null);
    }

    public String getVal(){
        return val;
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
        return new LiteralExpression(val);
    }

    @Override
    public void flatten() {
    }

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            stringBuilder.append("\t");
        }
        stringBuilder.append(val + "\n");
    }
}
