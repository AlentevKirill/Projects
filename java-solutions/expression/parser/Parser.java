package expression.parser;

import expression.EquationException;
import expression.MyOverflowException;
import expression.TripleExpression;

public interface Parser {
    TripleExpression parse(String expression) throws NumberFormatException, MyOverflowException, EquationException;
}
