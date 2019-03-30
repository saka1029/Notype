package notype.type;

import java.util.Arrays;
import java.util.Map;

public class MonoType implements BindableType {

    public static final MonoType OBJECT = new MonoType("object");
    public static final MonoType VOID = new MonoType("void");
    public static final MonoType CHAR = new MonoType("char");
    public static final MonoType INT = new MonoType("int");
    public static final MonoType LONG = new MonoType("long");
    public static final MonoType DOUBLE = new MonoType("double");
    public static final MonoType FLOAT = new MonoType("float");
    public static final MonoType BIG_INTEGER = new MonoType("biginteger");
    public static final MonoType BOOLEAN = new MonoType("boolean");
    public static final MonoType STRING = new MonoType("string");

    public final String name;
    private final Type[] parameters;

    public MonoType(String name, Type... parameters) {
        this.name = name;
        int size = parameters.length;
        this.parameters = new Type[size];
        for (int i = 0; i < size; ++i)
            this.parameters[i] = parameters[i];
    }

    public int size() {
        return parameters.length;
    }

    public Type get(int index) {
        return parameters[index];
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MonoType))
            return false;
        MonoType o = (MonoType)obj;
        return o.name.equals(name) && Arrays.equals(o.parameters, parameters);
    }

    @Override
    public String toString() {
        return parameters.length == 0 ? name : name + Arrays.toString(parameters);
    }

    @Override
    public TypeBind unify(Type type, TypeBind bind) {
        if (!(type instanceof MonoType))
            return type.unify(this, bind);
       MonoType t = (MonoType)type;
       if (!t.name.equals(name))
           return null;
       int size = parameters.length;
       if (t.parameters.length != size)
           return null;
       for (int i = 0; i < size && bind != null; ++i)
           bind = t.parameters[i].unify(parameters[i], bind);
       return bind;
    }

    private BindableType resolveCopy(TypeBind bind) throws UnresolvableException {
        int size = parameters.length;
        BindableType[] p = new BindableType[size];
        for (int i = 0; i < size; ++i)
            p[i] = parameters[i].resolve(bind);
        return new MonoType(name, p);
    }

    @Override
    public BindableType resolve(TypeBind bind) throws UnresolvableException {
        for (Type type : parameters)
            if (type instanceof VariableType)
                return resolveCopy(bind);
        return this;
    }

    @Override
    public Type copy(Map<VariableType, VariableType> map) {
        int size = parameters.length;
        if (size == 0)
            return this;
        Type[] p = new Type[size];
        for (int i = 0; i < size; ++i)
            p[i] = parameters[i].copy(map);
        return new MonoType(name, p);
    }

}
