import java.util.ArrayList;
import java.util.List;

public class AbstractCompoundExpression implements CompoundExpression {
    protected List<Expression> children;
    protected CompoundExpression parent;
    protected String val;

    public AbstractCompoundExpression(String val){
        this.val = val;
        this.children = new ArrayList<>();
        this.parent = null;
    }


    @Override
    public void addSubexpression(Expression subexpression) {
        subexpression.setParent(this);
        this.children.add(subexpression);
    }

    public List<Expression> getSubexpressions(){
        return this.children;
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
        AbstractCompoundExpression e = new AbstractCompoundExpression(val);
        for(Expression c : children){
            e.addSubexpression(c.deepCopy());
        }
        return e;
    }

    //Parsing automatically flattens so no need to implement
    @Override
    public void flatten() {
    }

    @Override
    public void convertToString(StringBuilder stringBuilder, int indentLevel){

    }
}
