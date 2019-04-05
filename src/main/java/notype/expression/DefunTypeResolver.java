package notype.expression;

import notype.type.MonoType;
import notype.type.Type;
import notype.type.VariableType;

public class DefunTypeResolver implements TypeResolver {

    @Override
    public Context resolve(Context context, Form form) {
        if (form.size() < 3 || !(form.get(1) instanceof Form))
            throw new ExpressionSyntaxException("syntax: (defun (FUN ARGS...) BODY...)");
        Form formal = (Form)form.get(1);
        if (formal.get(0) instanceof Symbol)
            throw new ExpressionSyntaxException("syntax: (defun (FUN ARGS...) BODY...)");
        Type[] types = new Type[formal.size()];
        for (int i = 0; i < types.length; ++i)
            types[i] = new VariableType();
        MonoType funType = new MonoType("function", types);
        context = context.add((Symbol)formal.get(0), funType);
        return context;
    }

}
