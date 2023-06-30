package expression;

import expression.parser.ExpressionParser;

public class CheckedAdd implements TripleExpression {
    private FunctionAndMembers funct1;
    private FunctionAndMembers funct2;

    public CheckedAdd(FunctionAndMembers funct1, FunctionAndMembers funct2) {
        this.funct1 = funct1;
        this.funct2 = funct2;
    }

    public boolean testFunct(FunctionAndMembers funct1, FunctionAndMembers funct2) {

        int sum = new Add(funct1, funct2).evaluate();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return 0;
    }
}
