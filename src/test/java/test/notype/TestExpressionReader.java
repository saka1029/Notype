package test.notype;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import notype.expression.Expression;
import notype.expression.ExpressionReader;
import notype.expression.Form;
import notype.expression.Literal;
import notype.expression.Symbol;
import notype.type.MonoType;

class TestExpressionReader {

    static Expression read(String s) {
        return new ExpressionReader(s).read();
    }

    @Test
    void test() {
        Form e = (Form)read("(prog (define x 0L) (set x (+ x 1L)))");
        System.out.println(e);
        assertEquals(new Symbol("prog"), e.get(0));
        assertTrue(e.get(1) instanceof Form);
        assertEquals(new Symbol("define"), ((Form)e.get(1)).get(0));
        assertEquals(MonoType.LONG, ((Literal)((Form)e.get(1)).get(2)).type);
        assertEquals(0L, ((Literal)((Form)e.get(1)).get(2)).value);
    }

}
