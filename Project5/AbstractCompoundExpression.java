import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCompoundExpression implements CompoundExpression {
    private List<Expression> children;
    private CompoundExpression parent;
    private String operator;
    private String val;

    public AbstractCompoundExpression(String val, CompoundExpression parent){
        this.val = val;
        this.children = new ArrayList<>();
        this.parent = parent;
    }

    @Override
    public void addSubexpression(Expression subexpression) {
        subexpression.setParent(this);
        this.children.add(subexpression);
    }

    public List<Expression> getSubexpressions(){
        return this.children;
    }

    public String getVal(){
        return val;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public CompoundExpression getParent() {
        return this.parent;
    }

    @Override
    public void setParent(CompoundExpression parent) {
        this.parent = parent;
    }

    // recursively calls itself on child nodes to deep copy tree
    // for each node make a new node with
    @Override
    public Expression deepCopy() {
        Expression e = null;
        for(Expression c : children){
            if(c instanceof  LiteralExpression) {
                e = new LiteralExpression(((LiteralExpression) c).getVal());
            }
            else {
                if (c instanceof AdditiveExpression) {
                    e = new AdditiveExpression(((AdditiveExpression) c).getVal());
                } else if (c instanceof MultiplicativeExpression) {
                    e = new MultiplicativeExpression(((MultiplicativeExpression) c).getVal());
                } else if (c instanceof ParentheticalExpression){
                    e = new ParentheticalExpression(((ParentheticalExpression) c).getVal());
                }
                ((AbstractCompoundExpression) e).addSubexpression(c.deepCopy());
            }
        }
        return e;
    }

    //Parsing automatically flattens so no need to implement
    @Override
    public void flatten() {
    }

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
