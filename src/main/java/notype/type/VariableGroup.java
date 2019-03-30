package notype.type;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VariableGroup implements BindableType {

    private final Set<VariableType> variables = new HashSet<>();

    private VariableGroup(VariableType v1, VariableType v2) {
        variables.add(v1);
        variables.add(v2);
    }

    private VariableGroup(VariableGroup g, VariableType v) {
        variables.addAll(g.variables);
        variables.add(v);
    }

    private VariableGroup(VariableGroup g1, VariableGroup g2) {
        variables.addAll(g1.variables);
        variables.addAll(g2.variables);
    }

    private TypeBind bind(TypeBind b) {
        for (VariableType v : variables)
            b = b.put(v, this);
        return b;
    }

    public static TypeBind bind(TypeBind b, VariableType v1, VariableType v2) {
        return new VariableGroup(v1, v2).bind(b);
    }

    public static TypeBind bind(TypeBind b, VariableGroup g, VariableType v) {
        return new VariableGroup(g, v).bind(b);
    }

    public static TypeBind bind(TypeBind b, VariableGroup g1, VariableGroup g2) {
        return new VariableGroup(g1, g2).bind(b);
    }

    public TypeBind bind(TypeBind b, BindableType t) {
        for (VariableType e : variables)
            b = b.put(e, t);
        return b;
    }


    public int size() {
        return variables.size();
    }

    public boolean contains(VariableType v) {
        return variables.contains(v);
    }

    @Override
    public String toString() {
        return variables.toString();
    }

    @Override
    public TypeBind unify(Type type, TypeBind bind) {
        if (type instanceof MonoType)
            return bind(bind, (MonoType)type);
        if (type instanceof VariableGroup)
            return bind(bind, this, (VariableGroup)type);
        if (type instanceof VariableType) {
            BindableType x = bind.get((VariableType)type);
            if (x != null)
                return bind(bind, x);
            return bind(bind, this, (VariableType)type);
        }
        return null;
    }

    @Override
    public BindableType resolve(TypeBind bind) throws UnresolvableException {
        throw new UnresolvableException("group", this);
    }

    @Override
    public Type copy(Map<VariableType, VariableType> map) {
        throw new RuntimeException("cannot copy group");
    }

}
