package expression;

public class EquationException extends Exception{

    public EquationException() {}

    public EquationException(String message) {
        super(message);
    }

    public EquationException(String message, Throwable cause) {
        super(message, cause);
    }
}
