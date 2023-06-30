package expression;

public class Multiply extends AbstractExpressions {

    public Multiply(FunctionAndMembers members1, FunctionAndMembers members2) {
        super(members1, members2, "*");
    }

    public int count(int first, int second) {
        return first * second;
    }
}
