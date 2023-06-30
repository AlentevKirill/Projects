package expression;

public class Const implements FunctionAndMembers {

    private int variable;

    public Const(int variable) {
        this.variable = variable;
    }

    public String toString() {
        return Integer.toString(this.variable);
    }

    @Override
    public boolean equals(Object check) {
        if (check == null || check.getClass() != Const.class) {
            return false;
        }
        return this.toString().equals(check.toString());
    }

    @Override
    public int evaluate(int x) {
        return this.variable;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return this.variable;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(variable) * 29;
    }
}
