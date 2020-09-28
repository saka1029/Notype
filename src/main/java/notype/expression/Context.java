package notype.expression;

import notype.type.Type;
import notype.type.TypeBind;

public class Context {

    public final TypeBind bind;
    public final Local local;
    public final Reference reference;


    public Context(TypeBind bind, Local local, Reference reference) {
        if (bind == null) throw new NullPointerException("bind");
        if (local == null) throw new NullPointerException("local");
        if (reference == null) throw new NullPointerException("reference");
        this.bind = bind;
        this.local = local;
        this.reference = reference;
    }

    public Context() {
        this(TypeBind.ROOT, Local.ROOT, Reference.ROOT);
    }

    public Context unify(Type type0, Type type1) {
        TypeBind b = type0.unify(type1, bind);
        if (b == null)
            return null;
        return new Context(b, local, reference);
    }

    public Context add(Symbol key, Type type) {
        return new Context(bind, this.local.add(key, type), reference);
    }

    public Context add(Symbol key) {
        return new Context(bind, local.add(key), reference);
    }

    public Context add(Symbol key, TypeResolver resolver) {
        return new Context(bind, local, reference.add(key, resolver));
    }

    @Override
    public String toString() {
        return String.format("(bind=%s, local=%s, reference=%s)", bind, local, reference);
    }

}
