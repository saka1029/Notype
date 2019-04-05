package notype.type;

public class FunctionType extends MonoType {

    public static String NAME = "function";

    public FunctionType(Type... parameters) {
        super(NAME, parameters);
    }

}
