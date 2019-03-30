package notype.expression;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Reference {

    public static final Reference ROOT = new Reference(null, null, null);

    public final Reference next;
    public final Symbol key;
    public final TypeResolver resolver;

    Reference(Reference next, Symbol key, TypeResolver resolver) {
        this.next = next;
        this.key = key;
        this.resolver = resolver;
    }

    public Reference add(Symbol key, TypeResolver resolver) {
        return new Reference(this, key, resolver);
    }

    public Iterable<TypeResolver> get(final Symbol key) {
        return () -> new Iterator<TypeResolver>() {

            Reference reference = Reference.this;
            {
                advance();
            }

            private void advance() {
                for (; reference != ROOT; reference = reference.next)
                    if (reference.key.equals(key))
                        break;
            }

            @Override
            public boolean hasNext() {
                return reference != ROOT;
            }

            @Override
            public TypeResolver next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                Reference r = reference;
                reference = reference.next;
                advance();
                return r.resolver;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String sep = "";
        for (Reference l = this; l != ROOT; l = l.next) {
            sb.append(sep).append(l.key).append(":").append(l.resolver);
            sep = ", ";
        }
        sb.append("}");
        return sb.toString();
    }

}
