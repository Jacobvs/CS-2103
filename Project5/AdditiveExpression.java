import java.util.ArrayList;

public class AdditiveExpression extends AbstractCompoundExpression {

    public AdditiveExpression(String val) {
        super("+");
    }

    /**
     * Do not need, tree is parsed in a way that makes this obsolete
     */
    @Override
    public void flatten() {

    }

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {

    }
}
