package notype.type;

public class UnresolvableException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    public final String cause;
    public final Type type;

    public UnresolvableException(String cause, Type type) {
        super(cause + ": " + type);
        this.cause = cause;
        this.type = type;
    }

}
