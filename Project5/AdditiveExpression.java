public class AdditiveExpression extends AbstractCompoundExpression {

    public AdditiveExpression(String val, CompoundExpression parent){
        super(val, parent);
        super.setOperator("+");
    }

    public AdditiveExpression(String val) {
        this(val, null);
    }

}
