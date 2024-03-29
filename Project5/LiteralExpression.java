import javafx.scene.Node;
import javafx.scene.control.Label;

public class LiteralExpression implements Expression {

    private CompoundExpression parent;
    private String val;
    private Label node;

    /**
     * Constructor for a literal expression
     * @param val Value of Literal expression -> [a-z], [0-9]+
     * @param parent Parent node of literal expression, must be compound
     */
    public LiteralExpression(String val, CompoundExpression parent){
        this.val = val;
        Label l = new Label(val);
        l.setFont(ExpressionEditor.FONT);
        this.node = l;
        if(parent != null)
            setParent(parent);
        else
            this.parent = null;
    }

    /**
     * Constructor for literal expression, parent is initially null and does not need a param
     * @param val Value of Literal expression -> [a-z], [0-9]+
     */
    public LiteralExpression(String val){
        this(val, null);
    }

    /**
     * Getter for value field
     * @return Value of expression
     */
    public String getVal(){
        return val;
    }

    /**
     * Getter for parent field
     * @return Parent of expression
     */
    @Override
    public CompoundExpression getParent() {
        return this.parent;
    }

    /**
     * Setter for parent field
     * @param parent the CompoundExpression that should be the parent of the target object
     */
    @Override
    public void setParent(CompoundExpression parent) {
        this.parent = parent;
        ((AbstractCompoundExpression) this.parent).updateHbox(node);
    }

    /**
     * Makes a deep copy of the expression, which just evaluates to the same expression (unique for literal expression)
     * @return Literal expression with same value
     */
    @Override
    public Expression deepCopy(CompoundExpression parent) {
        return new LiteralExpression(val, parent);
    }

    @Override
    public Node getNode() {
        return node;
    }

    /**
     * Not necessary, parsing method flattens while parsing
     */
    @Override
    public void flatten() {
    }

    /**
     * Prints a string representation of the expression based off level in tree
     * @param stringBuilder the StringBuilder to use for building the String representation
     * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
     */
    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            stringBuilder.append("\t");
        }
        stringBuilder.append(val + "\n");
    }
}
