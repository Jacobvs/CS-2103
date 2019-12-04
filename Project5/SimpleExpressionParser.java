import java.util.function.Function;

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
        parseA(str);
        return null;
    }

    boolean parseHelper(String str, String regex, Function<String, Boolean> m) {
        if (str.matches(regex)){
            String[] arr = str.split(regex);
            for(String s : arr)
                if(m.apply(s))
                    return true;
        }
        else{
            m.apply(str);
        }
        return false;
    }

    boolean parseA(String str){
        return parseHelper(str, "[^a-z0-9* ()]+(?![^\\(]*\\))", this::parseM);
    }
    boolean parseM(String str){
        return parseHelper(str, "[^a-z0-9+ ()]+(?![^\\(]*\\))", this::parseP);
    }
    boolean parseP(String str){
        return parseHelper(str, "(\\a)", this::parseL);
    }
    boolean parseL(String str){
        if(str.length() == 0)
             return true;
        return parseHelper(str, "[a-z]|[0-9]+", this::parseL);
    }
}
