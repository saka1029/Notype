package notype.expression;

public class ProgTypeResolver implements TypeResolver {

    @Override
    public Context resolve(Context context, Form form) {
        for (int i = 1; i < form.size(); ++i) {
            Expression arg = form.get(i);
            context = arg.resolve(context);
            if (context == null)
                return null;
            form.type = arg.type();
        }
        for (int i = 0; i < form.size(); ++i)
            form.get(i).bind = context.bind;
        return context;
    }

}
