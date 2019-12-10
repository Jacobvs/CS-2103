import javafx.scene.Node;

public class ParentheticalExpression extends AbstractCompoundExpression {

    /**
     * Constructor for parenthetical expression
     * @param val Value of expression, ex: (5+x)
     * @param parent Parent of expression
     */
    public ParentheticalExpression(String val, CompoundExpression parent){
        super(val, parent);
        super.setOperator("()");
    }

    /**
     * Constructor with 1 argument
     * @param val Value of expression (see above)
     */
    public ParentheticalExpression(String val) {
        this(val, null);
    }

    /**
     * Adds subexpression to list of children, parenthetical expressions can only have 1 child
     * @param subexpression the child expression to add
     */
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
