import java.util.ArrayList;

public class MultiplicativeExpression extends SimpleCompoundExpression {

    public MultiplicativeExpression(){
        super();
        value = "*";
    }

    @Override
    public void flatten() {}

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel) {}
}
