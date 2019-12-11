import javafx.scene.Node;
import javafx.scene.control.Label;

public class AdditiveExpression extends AbstractCompoundExpression {

    /**
     * Constructor for additive expression
     * @param val Value of expression, example: x+4
     * @param parent Parent node of expression, must be compound
     */
    public AdditiveExpression(String val, CompoundExpression parent, Label node){
        super(val, parent, node);
        super.setOperator("+");
    }

    /**
     * 1 Args constructor for additive expression, parent is initially null and does not need a param
     * @param val Value of expression (see above)
     */
    public AdditiveExpression(String val, Label node) {
        this(val, null, node);
    }

}
