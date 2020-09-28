package test.notype;

import static notype.type.MonoType.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import notype.expression.Context;
import notype.expression.DefineTypeResolver;
import notype.expression.DefunTypeResolver;
import notype.expression.ExpressionReader;
import notype.expression.Form;
import notype.expression.FunctionTypeResolver;
import notype.expression.ProgTypeResolver;
import notype.expression.Symbol;
import notype.type.FunctionType;

class TestDefunTypeResolver {

    @Test
    void test() {
        Form form = (Form)new ExpressionReader(
            "(prog"
            + " (define x 0)"
            + " (defun (plus1 y) (+ y 1))"
            + " (plus1 x))"
            ).read();
        Context context = new Context()
            .add(new Symbol("prog"), new ProgTypeResolver())
            .add(new Symbol("define"), new DefineTypeResolver())
            .add(new Symbol("defun"), new DefunTypeResolver())
            .add(new Symbol("+"), new FunctionTypeResolver(new FunctionType(INT, INT, INT)))
            ;
        Context c = form.resolve(context);
        System.out.println(form);
        assertNotNull(c);
        assertEquals(VOID, form.get(1).rawType());
        assertEquals(VOID, form.get(2).rawType());
        assertEquals(INT, form.get(3).rawType());

    }

}
