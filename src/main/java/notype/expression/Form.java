package notype.expression;

import java.util.Arrays;

import notype.type.Type;

public class Form extends Expression {

    final Expression[] arguments;
    public Type type;

    public Form(Expression... arguments) {
        int size = arguments.length;
        this.arguments = new Expression[size];
        for (int i = 0; i < size; ++i)
            this.arguments[i] = arguments[i];
    }

    public int size() {
        return arguments.length;
    }

    public Expression get(int index) {
        return arguments[index];
    }

    @Override
    public Type type() {
        return type;
    }

    @Override
    public String toString() {
        return Arrays.toString(arguments);
    }

    @Override
    public Context resolve(Context context) {
        if (size() <= 0)
            throw new ExpressionSyntaxException("empty list");
        Expression e = get(0);
        if (!(e instanceof Symbol))
            throw new ExpressionSyntaxException("functor must be symbol but " + e);
        Symbol functor = (Symbol)e;
        for (TypeResolver r : context.reference.get(functor)) {
            Context c = r.resolve(context, this);
            if (c != null) {
                bind = c.bind;
                return c;
            }
        }
        return null;
    }

}
