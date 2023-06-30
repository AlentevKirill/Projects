package expression;

import java.util.Objects;

public class Variable implements FunctionAndMembers {

    private String string;

    public Variable(String string) {
        this.string = string;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (this.string.equals("x")) {
            return x;
        }
        if (this.string.equals("y")) {
            return y;
        }
        return z;
    }

    @Override
    public int evaluate(int x) {
        return x;
    }

    public String toString() {
        return string;
    }

    @Override
    public boolean equals(Object check) {
        if (check == null || check.getClass() != Variable.class) {
            return false;
        }
        return this.string.equals(check.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(string) * 31;
    }
}

