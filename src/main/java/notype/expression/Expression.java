package notype.expression;

import notype.type.Type;
import notype.type.TypeBind;

public abstract class Expression {

    public TypeBind bind;
    public abstract Type rawType();
    public abstract Context resolve(Context context);

    public Type type() {
        return rawType().resolve(bind);
    }

}
