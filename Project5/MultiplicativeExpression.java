import javafx.scene.Node;

import java.util.ArrayList;

public class MultiplicativeExpression extends AbstractCompoundExpression {

    /**
     * Constructor for multiplicative expression
     * @param val Value of expression, ex: 5*x
     * @param parent Parent of expression, must be compound
     */
    public MultiplicativeExpression(String val, CompoundExpression parent) {
        super(val, parent);
        super.setOperator("*");
    }

    /**
     * Constructor with 1 arg, parent is initially null and does not have to have a parameter
     * @param val
     */
    public MultiplicativeExpression(String val){
        this(val, null);
    }

}
