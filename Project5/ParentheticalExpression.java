public class ParentheticalExpression extends AbstractCompoundExpression {

    public ParentheticalExpression(String val) {
        super("()");
    }

    @Override
    public void addSubexpression(Expression subexpression) {
        // Throw error if user tries to add more than one child to parenthetical
        subexpression.setParent(this);
        this.children.add(subexpression);
    }



}