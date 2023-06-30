package expression;

import java.util.Objects;

public abstract class AbstractExpressions implements FunctionAndMembers {

    private FunctionAndMembers firstVariable, secondVariable;
    private String functions;

    public AbstractExpressions(FunctionAndMembers firstVariable, FunctionAndMembers secondVariable, String functions) {
        this.firstVariable = firstVariable;
        this.secondVariable = secondVariable;
        this.functions = functions;
    }

    abstract protected int count(int a, int b) throws MyOverflowException, DivideByZeroException;

    public FunctionAndMembers getFirstVariable() {
        return firstVariable;
    }

    public FunctionAndMembers getSecondVariable() {
        return secondVariable;
    }

    @Override
    public int evaluate(int x, int y, int z) throws MyOverflowException, DivideByZeroException {
        return count(firstVariable.evaluate(x, y, z), secondVariable.evaluate(x, y, z));
    }

    @Override
    public int evaluate(int x) throws MyOverflowException, DivideByZeroException {
        return count(firstVariable.evaluate(x), secondVariable.evaluate(x));
    }

    public String toString() {
        return "(" + firstVariable.toString() + " " + functions + " " + secondVariable.toString() + ")";
    }

    public boolean equals(Object check) {
        if (check == null || check.getClass() != this.getClass()) {
            return false;
        }
        AbstractExpressions abstractExpression = (AbstractExpressions) check;
        return this.firstVariable.equals(abstractExpression.firstVariable) && this.secondVariable.equals(abstractExpression.secondVariable);
    }

    @Override
    public int hashCode() {
        int res = Objects.hashCode(getFirstVariable()) * 31;
        res = (res + Objects.hashCode(getSecondVariable())) * 31;
        res = res + Objects.hashCode(getClass()) * 29;
        return res;
    }
}
