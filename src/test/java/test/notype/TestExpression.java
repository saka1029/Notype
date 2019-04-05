package test.notype;

import static notype.type.MonoType.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import notype.expression.Context;
import notype.expression.DefineTypeResolver;
import notype.expression.Expression;
import notype.expression.Form;
import notype.expression.FunctionTypeResolver;
import notype.expression.Literal;
import notype.expression.ProgTypeResolver;
import notype.expression.Symbol;
import notype.type.FunctionType;
import notype.type.VariableType;

class TestExpression {

    static Form form(Expression... arguments) { return new Form(arguments); }
    static Literal lit(int value) { return new Literal(value, INT); }
    static Literal lit(String value) { return new Literal(value, STRING); }
    static Literal lit(boolean value) { return new Literal(value, BOOLEAN); }
    static Symbol sym(String name) { return new Symbol(name); }

    @Test
    void testPrint() {
        System.out.println(form(sym("+"), sym("x"), lit(1)));
    }

    @Test
    void testResolveVariable() {
        Symbol x = sym("x");
        Symbol y = sym("y");
        Context context = new Context()
            .add(sym("x"))
            .add(sym("y"))
            .add(sym("+"), new FunctionTypeResolver(new FunctionType(INT, INT, INT)));
        Form expression = form(sym("+"), x, y);
        Context result = expression.resolve(context);
        assertNotNull(result);
        System.out.println(expression);
        System.out.println(expression.bind);
        assertEquals(INT, expression.type());
        for (int i = 1; i < expression.size(); ++i)
            assertEquals(INT, expression.get(i).type());
    }

    @Test
    void testResolveIf() {
        VariableType v = new VariableType();
        Context context = new Context()
            .add(sym("x"))
            .add(sym("if"), new FunctionTypeResolver(
                new FunctionType(v, BOOLEAN, v, v)));
        Form expression = form(sym("if"), lit(true), lit(1), sym("x"));
        Context result = expression.resolve(context);
        assertNotNull(result);
        System.out.println(expression);
        System.out.println(expression.bind);
        assertEquals(INT, expression.type());
        assertEquals(BOOLEAN, expression.get(1).type());
        assertEquals(INT, expression.get(2).type());
        assertEquals(INT, context.local.get(sym("x")).type.resolve(expression.bind));
    }

    @Test
    void testResolveProg() {
        Symbol x = sym("x");
        Symbol y = sym("y");
        VariableType X = new VariableType("X");
        Context context = new Context()
            .add(sym("x"))
            .add(sym("y"))
            .add(sym("set"), new FunctionTypeResolver(new FunctionType(X, X, X)))
            .add(sym("+"), new FunctionTypeResolver(new FunctionType(INT, INT, INT)))
            .add(sym("+"), new FunctionTypeResolver(new FunctionType(STRING, STRING, STRING)))
            .add(sym("prog"), new ProgTypeResolver());
        Form expression = new Form(sym("prog"),
            form(sym("set"), sym("x"), form(sym("+"), x, lit(1))),
            form(sym("set"), sym("y"), form(sym("+"), y, lit("abc"))),
            form(sym("+"), lit("a"), y));
        System.out.println(expression);
        Context result = expression.resolve(context);
        assertNotNull(result);
        System.out.println(expression);
        System.out.println(expression.bind);
        assertEquals(STRING, expression.type());
        assertEquals(INT, expression.get(1).type());
        assertEquals(STRING, expression.get(2).type());
        assertEquals(STRING, expression.get(3).type());
    }

    @Test
    void testResolveProgDefine() {
        Symbol x = sym("x");
        Symbol y = sym("y");
        VariableType X = new VariableType("X");
        Context context = new Context()
//            .add(sym("x"))
//            .add(sym("y"))
            .add(sym("set"), new FunctionTypeResolver(new FunctionType(X, X, X)))
            .add(sym("+"), new FunctionTypeResolver(new FunctionType(INT, INT, INT)))
            .add(sym("+"), new FunctionTypeResolver(new FunctionType(STRING, STRING, STRING)))
            .add(sym("define"), new DefineTypeResolver())
            .add(sym("prog"), new ProgTypeResolver());
        Form expression = new Form(sym("prog"),
            form(sym("define"), sym("x")),
            form(sym("define"), sym("y")),
            form(sym("set"), sym("x"), form(sym("+"), x, lit(1))),
            form(sym("set"), sym("y"), form(sym("+"), y, lit("abc"))),
            form(sym("+"), lit("a"), y));
        System.out.println(expression);
        Context result = expression.resolve(context);
        assertNotNull(result);
        System.out.println(expression);
        System.out.println(expression.bind);
        assertEquals(STRING, expression.type());
        assertEquals(VOID, expression.get(1).type());
        assertEquals(VOID, expression.get(2).type());
        assertEquals(INT, expression.get(3).type());
        assertEquals(STRING, expression.get(4).type());
        assertEquals(STRING, expression.get(5).type());
    }

}
