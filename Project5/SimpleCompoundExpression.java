import java.util.ArrayList;

public class SimpleCompoundExpression implements CompoundExpression{

    ArrayList<Expression> subExpressions;
    CompoundExpression parent;
    String value;

    public SimpleCompoundExpression(){
        this.subExpressions = new ArrayList<Expression>();
        parent = null;
    }

    @Override
    public void addSubexpression(Expression subexpression) {
        subexpression.setParent(this);
        subExpressions.add(subexpression);
    }

    @Override
    public CompoundExpression getParent() {
        return parent;
    }

    @Override
    public void setParent(CompoundExpression parent) {
        this.parent = parent;
    }

    @Override
    public Expression deepCopy() {
        SimpleCompoundExpression copy = new SimpleCompoundExpression();
        for(Expression child: subExpressions){
            copy.addSubexpression(child.deepCopy());
        }
        return copy;
    }

    @Override
    public void flatten() {

    }

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {

    }
}
