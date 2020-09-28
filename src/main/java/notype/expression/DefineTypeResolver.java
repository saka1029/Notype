package notype.expression;

import notype.type.MonoType;

public class DefineTypeResolver implements TypeResolver {

    Symbol variable(Form form) {
        Expression first = form.get(1);
        if (!(first instanceof Symbol))
            throw new ExpressionSyntaxException("first argument must be symbol but (" + first + ")");
        return (Symbol)first;
    }

    @Override
    public Context resolve(Context context, Form form) {
        Symbol variable;
        switch (form.size()) {
        case 2: // define VARIABLE
            variable = variable(form);
            form.type = MonoType.VOID;
            return context.add(variable);
        case 3: // define VARIABLE EXPRESSION
            variable = variable(form);
            Expression value = form.get(2);
            context = value.resolve(context);
            if (context == null)
                return null;
            form.type = MonoType.VOID;
            context = context.add(variable, value.rawType());
            variable.bind = context.bind;
            variable.definition = context.local;
            return context;
        default:
            throw new ExpressionSyntaxException("syntax: 'define VARIABLE [EXPRESSION]'");
        }
    }

}
