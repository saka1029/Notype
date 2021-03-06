package notype.expression;

import notype.type.Type;

public class Symbol extends Expression {

    public final String name;
    Local definition;

    public Symbol(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Symbol))
            return false;
        Symbol o = (Symbol)obj;
        return o.name.equals(name);
    }

    @Override
    public Type rawType() {
        if (definition == null)
            return null;
        return definition.type;
    }

    @Override
    public Context resolve(Context context) {
        definition = context.local.get(this);
        if (definition == null)
            return null;
        return context;
    }

    @Override
    public String toStringSimple() {
        return name;
    }
}
