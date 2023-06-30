package expression;

public class And extends AbstractExpressions {

    public And(FunctionAndMembers firstMembers, FunctionAndMembers secondMembers) {
        super(firstMembers, secondMembers, "&");
    }

    public int count(int first, int second) {
        return first & second;
    }
}
