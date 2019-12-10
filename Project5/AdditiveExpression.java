import javafx.scene.Node;

public class AdditiveExpression extends AbstractCompoundExpression {

    /**
     * Constructor for additive expression
     * @param val Value of expression, example: x+4
     * @param parent Parent node of expression, must be compound
     */
    public AdditiveExpression(String val, CompoundExpression parent){
        super(val, parent);
        super.setOperator("+");
    }

    /**
     * 1 Args constructor for additive expression, parent is initially null and does not need a param
     * @param val Value of expression (see above)
     */
    public AdditiveExpression(String val) {
        this(val, null);
    }

    @Override
    public Node getNode() {
        return null;
    }
}
