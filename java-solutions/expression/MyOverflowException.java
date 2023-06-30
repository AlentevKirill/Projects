package expression;

public class MyOverflowException extends Exception {

    public MyOverflowException(){}

    public MyOverflowException(String message) {
        super(message);
    }

    public MyOverflowException(String message, Throwable cause) {
        super(message, cause);
    }
}
