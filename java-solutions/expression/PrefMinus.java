package expression;

public class PrefMinus implements FunctionAndMembers {
    FunctionAndMembers members;

    public PrefMinus(FunctionAndMembers members) {
        this.members = members;
    }

    @Override
    public int evaluate(int x) {
        return - members.evaluate(x);
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return - members.evaluate(x, y, z);
    }

    public String toString() {
        return "-" + members.toString();
    }

}
