package notype.type;

import java.util.Map;

public class VariableType implements Type {

    static int sequence = 0;
    public final String name;

    public VariableType(String name) {
        this.name = name;
    }

    public VariableType() {
        this("%" + Integer.toString(sequence++));
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public TypeBind unify(Type type, TypeBind bind) {
        BindableType bound = bind.get(this);
        if (bound != null)
            return bound.unify(type, bind);
        if (type instanceof MonoType)
            return bind.put(this, (MonoType)type);
        if (type instanceof VariableGroup)
            return VariableGroup.bind(bind, (VariableGroup)type, this);
        if (type instanceof VariableType) {
            BindableType y = bind.get((VariableType)type);
            if (y != null)
                return bind.put(this, y);
            return VariableGroup.bind(bind, this, (VariableType)type);
        }
        return null;
    }

    @Override
    public BindableType resolve(TypeBind bind) throws UnresolvableException {
        BindableType type = bind.get(this);
        if (type == null)
            throw new UnresolvableException("undefined", this);
        return type.resolve(bind);
    }

    @Override
    public Type copy(Map<VariableType, VariableType> map) {
        return map.computeIfAbsent(this, v -> new VariableType());
    }

}
