public class ParentheticalExpression extends AbstractCompoundExpression {

    public ParentheticalExpression(String val, CompoundExpression parent){
        super(val, parent);
        super.setOperator("()");
    }

    public ParentheticalExpression(String val) {
        this(val, null);
    }

    @Override
    public void addSubexpression(Expression subexpression) {
        // Throw error if user tries to add more than one child to parenthetical
        if(super.getSubexpressions().size() == 0) {
            subexpression.setParent(this);
            super.addSubexpression(subexpression);
        }
        else{
            throw new RuntimeException("ParentheticalExpression has a child already.");
        }

    }
}
