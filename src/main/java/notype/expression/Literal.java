package notype.expression;

import notype.type.MonoType;
import notype.type.Type;

public class Literal extends Expression {

    public final Object value;
    public final Type type;

    public Literal(Object value, Type type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public Context resolve(Context context) {
        return context;
    }

    @Override
    public Type rawType() {
        return type;
    }

    @Override
    public String toString() {
        return value.toString() +
            (type == MonoType.LONG ? "L" :
            type == MonoType.BIG_INTEGER ? "I" :
            type == MonoType.FLOAT ? "F" :
            type == MonoType.DOUBLE ? "D" :
            "");
    }

}
