import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCompoundExpression implements CompoundExpression {
    private List<Expression> children;
    private CompoundExpression parent;
    private String operator;
    private String val;
    private HBox node;

    /**
     * Sets val to parameter, parent to parameter and initializes children as empty
     * @param val Value of the expression
     * @param parent Parent of the expression
     */
    public AbstractCompoundExpression(String val, CompoundExpression parent){
        this.val = val;
        this.children = new ArrayList<>();
        this.node = new HBox();
        this.node.setId(val);
        if(parent != null)
            setParent(parent);
        else
            this.parent = null;
    }

    /**
     * Adds expression to the list of children (representing subexpressions)
     * @param subexpression the child expression to add
     */
    @Override
    public void addSubexpression(Expression subexpression) {
        this.children.add(subexpression);
    }

    /**
     * Getter for list of children
     * @return List of children that represents subexpressions
     */
    public List<Expression> getSubexpressions(){
        return this.children;
    }

    /**
     * Getter for value variable
     * @return Value of expression
     */
    public String getVal(){
        return val;
    }

    /**
     * Sets operator of expression
     * @param operator New operator of expression ("*", "+" or "()")
     */
    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperator(){
        return this.operator;
    }

    /**
     * Getter for parent of expressionn
     * @return CompoundExpression that is parent node to current expression
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

    @Override
    public Node getNode() {
        return node;
    }

    public void updateHbox(Node n){
        if(node.getChildren().size() >= 1){
            Label o = new Label(operator);
            o.setFont(ExpressionEditor.FONT);
            this.node.getChildren().add(o);
        }

        if(this instanceof ParentheticalExpression){
            Label p1 = new Label("(");
            p1.setFont(ExpressionEditor.FONT);
            this.node.getChildren().add(p1);
        }

        node.getChildren().add(n);

        if(this instanceof ParentheticalExpression){
            Label p2 = new Label(")");
            p2.setFont(ExpressionEditor.FONT);
            this.node.getChildren().add(p2);
        }
    }

    /**
     * Makes a deep copy of the bst starting at the root node
     * Continually makes deep copy of children starting from top of tree
     * @return Deep copy of expression
     */
    @Override
    public Expression deepCopy(CompoundExpression parent) {
        Expression e = null;
        for(Expression c : children){
            if(c instanceof  LiteralExpression)
                e = c.deepCopy(parent);
            else {
                if (c instanceof AdditiveExpression)
                    e = new AdditiveExpression(c.getVal(), parent);
                else if (c instanceof MultiplicativeExpression)
                    e = new MultiplicativeExpression(c.getVal(), parent);
                else if (c instanceof ParentheticalExpression)
                    e = new ParentheticalExpression(c.getVal(), parent);
                ((AbstractCompoundExpression) e).addSubexpression(c.deepCopy((CompoundExpression) e));
            }
        }
        return e;
    }

    /**
     * Flattens tree, no need to implement because method of parsing automatically does this
     */
    @Override
    public void flatten() {
    }

    /**
     * Prints out BST as a string with different amounts of indentation for different levels
     * @param stringBuilder the StringBuilder to use for building the String representation
     * @param indentLevel the indentation level (number of tabs from the left margin) at which to start
     */
    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel){
        for (int i = 0; i < indentLevel; i++) {
            stringBuilder.append("\t");
        }
        stringBuilder.append(operator + "\n");
        for(Expression e : children){
            e.convertToString(stringBuilder, indentLevel + 1);
        }
    }
}
