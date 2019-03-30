package notype.type;

import java.util.HashMap;
import java.util.Map;

public interface Type {

    TypeBind unify(Type type, TypeBind bind);
    BindableType resolve(TypeBind bind) throws UnresolvableException;
    Type copy(Map<VariableType, VariableType> map);

    default Type copy() {
        return copy(new HashMap<>());
    }

}
