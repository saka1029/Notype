package notype.type;

public class TypeBind {

    public static final TypeBind ROOT = new TypeBind(null, null, null);

    public final TypeBind next;
    public final VariableType variable;
    public final BindableType value;

    TypeBind(TypeBind next, VariableType variable, BindableType value) {
        this.next = next;
        this.variable = variable;
        this.value = value;
    }

    public BindableType get(VariableType variable) {
        if (next == null)
            return null;
        if (variable.equals(this.variable))
            return value;
        return next.get(variable);
    }

    public TypeBind put(VariableType variable, BindableType value) {
        return new TypeBind(this, variable, value);
    }

    public boolean contains(VariableType variable) {
        if (next == null)
            return false;
        if (variable.equals(this.variable))
            return true;
        return next.contains(variable);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (next != null)
            sb.append(variable).append(":").append(value);
        for (TypeBind b = next; b != null && b.next != null; b = b.next)
            sb.append(", ").append(b.variable).append(":").append(b.value);
        sb.append("}");
        return sb.toString();
    }

}
