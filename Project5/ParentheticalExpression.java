public class ParentheticalExpression extends AbstractCompoundExpression {

    public ParentheticalExpression(String val) {
        super(val);
    }

    public ParentheticalExpression(String val, CompoundExpression parent) {
        super(val, parent);
    }
}