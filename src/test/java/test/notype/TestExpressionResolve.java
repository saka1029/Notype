package test.notype;

import static notype.type.MonoType.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import notype.expression.Context;
import notype.expression.DefineTypeResolver;
import notype.expression.Expression;
import notype.expression.ExpressionReader;
import notype.expression.Form;
import notype.expression.FunctionTypeResolver;
import notype.expression.Literal;
import notype.expression.ProgTypeResolver;
import notype.expression.Symbol;
import notype.type.FunctionType;

class TestExpressionResolve {

    static Expression read(String s) {
        return new ExpressionReader(s).read();
    }
    static Form form(Expression... arguments) { return new Form(arguments); }
    static Literal lit(int value) { return new Literal(value, INT); }
    static Literal lit(String value) { return new Literal(value, STRING); }
    static Literal lit(boolean value) { return new Literal(value, BOOLEAN); }
    static Symbol sym(String name) { return new Symbol(name); }

    @Test
    void testFunction() {
        Context c = new Context()
            .add(sym("prog"), new ProgTypeResolver())
            .add(sym("define"), new DefineTypeResolver())
            .add(sym("+"), new FunctionTypeResolver(new FunctionType(INT, INT, INT)))
            ;
        Form f = (Form)read("(prog (define x 1) (+ x 2))");
        Context cc = f.resolve(c);
        System.out.println(f);
        assertNotNull(cc);
        assertEquals(INT, f.type());
        assertEquals(VOID, f.get(1).type());
        assertEquals(INT, f.get(2).type());
    }

}
