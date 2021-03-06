package notype.expression;

import notype.type.MonoType;
import notype.type.Type;

public class FunctionTypeResolver implements TypeResolver {

    public final boolean copy;
    public final MonoType type;

    public FunctionTypeResolver(MonoType type, boolean copy) {
        if (!type.name.equals("function"))
            throw new IllegalArgumentException("function type required but " + type);
        this.copy = copy;
        this.type = type;
    }

    public FunctionTypeResolver(MonoType type) {
        this(type, true);
    }

    @Override
    public Context resolve(Context context, Form form) {
        MonoType type = copy ? (MonoType)this.type.copy() : this.type;
        int size = type.size();
        // form:(+ 1 2) args.size() = 3 (functor + args)
        // type:(function, INT, INT, INT) type.size() = 3 (return type + arg types)
        if (form.size() != size) // args:(+ 1 2) type:(function, INT, INT, INT)
            return null;
        for (int i = 1; i < size; ++i) {
            Expression arg = form.get(i);
            Type argType = type.get(i);
            context = arg.resolve(context);
            if (context == null)
                return null;
            context = context.unify(arg.rawType(), argType);
            if (context == null)
                return null;
        }
        form.type = type.get(0);
        for (int i = 0; i < size; ++i)
            form.get(i).bind = context.bind;
        return context;
    }

}
