package expression;

public class Add extends AbstractExpressions {

    public Add(FunctionAndMembers members1, FunctionAndMembers members2) {
        super(members1, members2, "+");
    }

    public int count(int first, int second) throws MyOverflowException {
        try {
            if (Integer.MAX_VALUE - first < second) {
                throw new MyOverflowException("overflow");
            } else {
                return first + second;
            }
        } catch (MyOverflowException e) { ;
            return first + second;
        }
    }
}
