package notype.expression;

import notype.type.Type;
import notype.type.VariableType;

public class Local {

    public static final Local ROOT = new Local(null, null, null);

    public final Local next;
    public final Symbol key;
    public final Type type;

    Local(Local next, Symbol key, Type type) {
        this.next = next;
        this.key = key;
        this.type = type;
    }

    public Local add(Symbol key, Type type) {
        return new Local(this, key, type);
    }

    public Local add(Symbol key) {
        return new Local(this, key, new VariableType());
    }

    public Local get(Symbol key) {
        if (this == ROOT)
            return null;
        if (key.equals(this.key))
            return this;
        return next.get(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String sep = "";
        for (Local l = this; l != ROOT; l = l.next) {
            sb.append(sep).append(l.key).append(":").append(l.type);
            sep = ", ";
        }
        sb.append("}");
        return sb.toString();
    }

}
