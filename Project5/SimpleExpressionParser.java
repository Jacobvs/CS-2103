import javafx.scene.control.Label;

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
     * Boolean to store when root node has been found (for parenthetical operations)
     */
    private Boolean firstNode;

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

        //TODO implement javaFXControls Boolean
        // Flatten the expression before returning
        expression.flatten();
        return expression;
    }

    /**
     * Builds an Expression tree from a given string
     * @param str String to parse
     * @return Expression tree of string
     */
    protected Expression parseExpression(String str) {
        firstNode = true;
        return parseA(str, null);
    }

    /**
     * Helper method for CFG parsing, uses regex matching to find parts of equation to store in each Expression object
     * @param str String to parse
     * @param regex regex string to match equation parts to
     * @param type Type of expression being parsed for (0 for AdditiveExpression, 1 for MultiplicativeExpression)
     * @param prev CompoundExpression to hold parent node
     * @param m Bifunction lambda wrapper to call next method from
     * @return Expression of parsed type
     */
    Expression parseHelper(String str, String regex, int type, CompoundExpression prev, BiFunction<String, CompoundExpression, Expression> m) {
        AbstractCompoundExpression e;
        Pattern pattern = Pattern.compile(regex); // Setup regex matcher
        Matcher matcher = pattern.matcher(str);

        if(str.length() > 2 && str.charAt(0) == '(' && str.charAt(str.length()-1) == ')') // Ensure string isn't enveloped in parentheses
            return m.apply(str, prev); // if so, skip to next method

        if (matcher.find() && type != 2){ // if the regex finds a match, continue, If the type is a literal, skip parsing

            e = (type == 0) ? new AdditiveExpression(str, prev) : new MultiplicativeExpression(str, prev); // Setup CompoundExpression node for defined type
            String[] arr = str.split(regex); // Split by regex matches on operator

            if(arr.length == 0) // If both sides of equation are empty, raise error
                return null;

            for(String s : arr){
                if(str.charAt(str.length()-1) == '+' || str.charAt(str.length()-1) == '*'
                   || str.charAt(0) == '+' || str.charAt(0) == '*') // ensure there's no unbalanced operators
                    return null;
                Expression c = m.apply(s, e); // save result of recursive parsing
                if(c == null) // If result is null (error raised) set this node to be null as well
                    return null;
                firstNode = false;
                e.addSubexpression(c); // Add result to children of expression

            }
        }
        else{
            return m.apply(str, prev);
        }
        return e;
    }

    /**
     * Parses string for addition expression components
     * @param str String to parse
     * @param e Parent CompoundExpression
     * @return Expression tree from str
     */
    Expression parseA(String str, CompoundExpression e){
        return parseHelper(str, "[^a-z0-9* ()]+(?![^\\(]*\\))", 0, e, this::parseM); // Regex matches upon all '+' operators not bounded by parenthesis
    }

    /**
     * Parses string for multiplication expression components
     * @param str String to parse
     * @param e Parent CompoundExpression
     * @return Expression tree from str
     */
    Expression parseM(String str, CompoundExpression e){
        return parseHelper(str, "[^a-z0-9+ ()]+(?![^\\(]*\\))", 1, e, this::parseP); // Regex matches upon all '*' operators not bounded by parenthesis
    }

    /**
     * Parses string for parenthetical expression components
     * @param str String to parse
     * @param e Parent CompoundExpression
     * @return Expression tree from str
     */
    Expression parseP(String str, CompoundExpression e){ // Regex can't find matches of balanced parentheses so we do it manually
        if(e == null && !firstNode) // ensure the node isn't null (error) and if so that it's not the first parent node (set to null by default)
            return null;
        ParentheticalExpression pe;
        if(str.indexOf('(') > -1) { // ensure given string contains parentheses
            int first = str.indexOf('('), count = 0, last;
            for (int i = first; i < str.length(); i++) { // keep looping until outermost balanced parentheses set is found
                if (str.charAt(i) == '(')
                    count++;
                else if (str.charAt(i) == ')')
                    count--;
                if (count == 0) { // Balanced set has been found
                    last = i;
                    if(last-first >= 1) { // ensure there's something between the parentheses
                        firstNode = false;

                        pe = new ParentheticalExpression(str, e); // Create new expression
                        str = str.substring(first+1, last); // trim parenthesis off

                        if(str.length() > 0 && (str.charAt(str.length()-1) == '+' || str.charAt(str.length()-1) == '*'
                           || str.charAt(0) == '+' || str.charAt(0) == '*')) // ensure string doesn't have unbalanced operators
                            return null;

                        Expression c = parseHelper(str, "[^a-z0-9* ()]+(?![^\\(]*\\))", 0, e, this::parseM); // Save result of recursing inner string starting back at addition
                        if(c == null) // If c is null cascade null (error) up recursion
                            return null;
                        pe.addSubexpression(c); // add result to node's children
                        return pe;
                    }
                }
            }
        }
        return parseHelper(str, "[a-z]|[0-9]+", 2, e, this::parseL); // If no parenthesis run LiteralExpression parse method
    }

    /**
     * Parses string for literal expression components
     * @param str String to parse
     * @param e Parent CompoundExpression
     * @return Expression tree from str
     */
    Expression parseL(String str, CompoundExpression e){
        if(str.length() == 0 || str.indexOf('(') != -1 || str.indexOf(')') != -1)
            return null;
        firstNode = false;
        return new LiteralExpression(str, e); // Create and return literal expression with str as value
    }
}
