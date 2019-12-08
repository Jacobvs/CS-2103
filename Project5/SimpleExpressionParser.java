import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Expression e = parseA(str, null);
        return e;
    }

    // Create Expression based upon the
    Expression parseHelper(String str, String regex, int type, CompoundExpression prev, BiFunction<String, CompoundExpression, Expression> m) {
        AbstractCompoundExpression e;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if(str.length() > 2 && str.charAt(0) == '(' && str.charAt(str.length()-1) == ')')
            return m.apply(str, prev);
        if (matcher.find()){
            if(type == 0)
                e = new AdditiveExpression(str, prev);
            else
                e = new MultiplicativeExpression(str, prev);
            if(type == 2)
                return m.apply(str, e);
            String[] arr = str.split(regex);
            int count = 0;
            for(String s : arr){
                System.out.println(count);
                count++;
                if(s.length() == 0)
                    return null;
                e.addSubexpression(m.apply(s, e));
            }
        }
        else{
            return m.apply(str, prev);
        }
        return e;
    }

    Expression parseA(String str, CompoundExpression e){
        System.out.println("additive");
        return parseHelper(str, "[^a-z0-9* ()]+(?![^\\(]*\\))", 0, e, this::parseM);
    }
    Expression parseM(String str, CompoundExpression e){
        System.out.println("mult");
        return parseHelper(str, "[^a-z0-9+ ()]+(?![^\\(]*\\))", 1, e, this::parseP);
    }

    //TODO: implement flattening of parenthesis
    Expression parseP(String str, CompoundExpression e){
        System.out.println("par");
        ParentheticalExpression pe;
        if(str.indexOf('(') > -1) {
            int first = str.indexOf('('), count = 0, last = 0;
            for (int i = first; i < str.length(); i++) {
                if (str.charAt(i) == '(')
                    count++;
                else if (str.charAt(i) == ')')
                    count--;
                if (count == 0) {
                    last = i;
                    if(last-first >= 1) {
                        pe = new ParentheticalExpression(str.substring(first + 1, last), e);
                        pe.addSubexpression(parseHelper(str.substring(first + 1, last), "[^a-z0-9* ()]+(?![^\\(]*\\))", 0, e, this::parseA));
                        return pe;
                    }
                }
            }
        }
        return parseHelper(str, "[a-z]|[0-9]+", 2, e, this::parseL);
    }

    Expression parseL(String str, CompoundExpression e){
        System.out.println("lit");
        return new LiteralExpression(str, e);
    }
}
