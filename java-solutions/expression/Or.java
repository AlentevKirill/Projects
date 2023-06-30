package expression;

public class Or extends AbstractExpressions {

    public Or(FunctionAndMembers firstMembers, FunctionAndMembers secondMembers) {
        super(firstMembers, secondMembers, "^");
    }

    public int count(int first, int second) {
        return first | second;
    }

}
