package expression;

public class XOR extends AbstractExpressions {

    public XOR(FunctionAndMembers firstMembers, FunctionAndMembers secondMembers) {
        super(firstMembers, secondMembers, "^");
    }

    public int count(int first, int second) {
        return first ^ second;
    }

}
