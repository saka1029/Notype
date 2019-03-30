package notype.expression;

public class ExpressionSyntaxException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExpressionSyntaxException(String message) {
        super(message);
    }

    public ExpressionSyntaxException(String format, Object... args) {
        super(String.format(format, args));
    }

    public ExpressionSyntaxException(Throwable t) {
        super(t);
    }
}
