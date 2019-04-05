package notype.expression;

import notype.type.FunctionType;
import notype.type.MonoType;
import notype.type.Type;
import notype.type.VariableType;

public class DefunTypeResolver implements TypeResolver {

    @Override
    public Context resolve(Context context, Form form) {
        if (form.size() < 3 || !(form.get(1) instanceof Form))
            throw new ExpressionSyntaxException("syntax: (defun (FUN ARGS...) BODY...)");
        Form formal = (Form)form.get(1);
        if (!(formal.get(0) instanceof Symbol))
            throw new ExpressionSyntaxException("function name must be symol but " + formal.get(0));
        int size = formal.size();
        Type[] types = new Type[size];
        types[0] = new VariableType();
        for (int i = 1; i < size; ++i) {
            Type v = new VariableType();
            types[i] = v;
            context = context.add((Symbol)formal.get(i), v);
        }
        MonoType funType = new FunctionType(types);
        context = context.add((Symbol)formal.get(0), new FunctionTypeResolver(funType));
        for (int i = 2; i < form.size(); ++i) {
            Expression arg = form.get(i);
            context = arg.resolve(context);
            if (context == null)
                return null;
//            form.type = arg.rawType();
        }
        for (int i = 0; i < form.size(); ++i)
            form.get(i).bind = context.bind;
        form.type = MonoType.VOID;
        return context;
    }

}
