package expression;

public interface Expression {
    int evaluate(int x) throws MyOverflowException, DivideByZeroException;
}
