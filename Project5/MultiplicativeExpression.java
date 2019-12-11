import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class MultiplicativeExpression extends AbstractCompoundExpression {

    /**
     * Constructor for multiplicative expression
     * @param val Value of expression, ex: 5*x
     * @param parent Parent of expression, must be compound
     */
    public MultiplicativeExpression(String val, CompoundExpression parent, Label node) {
        super(val, parent, node);
        super.setOperator("*");
    }

    /**
     * Constructor with 1 arg, parent is initially null and does not have to have a parameter
     * @param val
     */
    public MultiplicativeExpression(String val, Label node){
        this(val, null, node);
    }

}
