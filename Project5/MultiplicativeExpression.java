import java.util.ArrayList;

public class MultiplicativeExpression extends AbstractCompoundExpression {

    public MultiplicativeExpression(String val, CompoundExpression parent) {
        super(val, parent);
        super.setOperator("*");
    }

    public MultiplicativeExpression(String val){
        this(val, null);
    }
}
