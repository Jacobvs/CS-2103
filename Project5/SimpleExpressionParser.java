import java.util.function.BiFunction;

/**
 * Starter code to implement an ExpressionParser. Your parser methods should use the following grammar:
 * E := A | X
 * A := A+M | M
 * M := M*M | X
 * X := (E) | L
 * L := [0-9]+ | [a-z]
 */
public class SimpleExpressionParser implements ExpressionParser {
    /**
     * Attempts to create an expression tree -- flattened as much as possible -- from the specified String.
     * Throws a ExpressionParseException if the specified string cannot be parsed.
     *
     * @param str                the string to parse into an expression tree
     * @param withJavaFXControls you can just ignore this variable for R1
     * @return the Expression object representing the parsed expression tree
     **/
    public Expression parse(String str, boolean withJavaFXControls) throws ExpressionParseException {
        // Remove spaces -- this simplifies the parsing logic
        str = str.replaceAll(" ", "");
        Expression expression = parseExpression(str);
        if (expression == null) {
            // If we couldn't parse the string, then raise an error
            throw new ExpressionParseException("Cannot parse expression: " + str);
        }

        // Flatten the expression before returning
        expression.flatten();
        return expression;
    }

    protected Expression parseExpression(String str) {
        Expression expression;
        parseA(str, null);
        return null;
    }

    //TODO: UNBREAK THIS

    // Create Expression based upon the
    void parseHelper(String str, String regex, int type, CompoundExpression prev, BiFunction<String, Expression, Expression> m) {
        AbstractCompoundExpression e;
        if (str.matches(regex)){
            switch(type){
                case 0:
                    e = new AdditiveExpression(str, prev);
                default:
                    e = new MultiplicativeExpression(str, prev);
            }
            String[] arr = str.split(regex);
            for(String s : arr){
                e.addSubexpression(m.apply(s, e));
            }

        }
        else{
            m.apply(str, prev);
        }
    }

    void parseA(String str, Expression e){
        return parseHelper(str, "[^a-z0-9* ()]+(?![^\\(]*\\))", 0, this::parseM);
    }
    void parseM(String str, Expression e){
        return parseHelper(str, "[^a-z0-9+ ()]+(?![^\\(]*\\))", 1, this::parseP);
    }

    //TODO: implement flattening of parenthesis
    void parseP(String str, Expression e){
        if(str.indexOf('(') > -1) {
            int first = str.indexOf('('), count = 0, last = 0;
            for (int i = first; i < str.length(); i++) {
                if (str.charAt(i) == '(')
                    count++;
                else if (str.charAt(i) == ')')
                    count--;
                if (count == 0) {
                    last = i;
                    break;
                }
            }
            return new ParentheticalExpression(str.substring(first + 1, last - 1));
        }

    }
    void parseL(String str){
        if(str.length() == 0)
             return true;
        return parseHelper(str, "[a-z]|[0-9]+", this::parseL);
    }
}
