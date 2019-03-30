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
import notype.type.MonoType;
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
            .add(sym("+"), new FunctionTypeResolver(new MonoType("function", INT, INT, INT)));
        Form expression = form(sym("+"), x, y);
        Context result = expression.resolve(context);
        assertNotNull(result);
        System.out.println(expression);
        System.out.println(expression.bind);
        assertEquals(INT, expression.resolvedType());
        for (int i = 1; i < expression.size(); ++i)
            assertEquals(INT, expression.get(i).resolvedType());
    }

    @Test
    void testResolveIf() {
        VariableType v = new VariableType();
        Context context = new Context()
            .add(sym("x"))
            .add(sym("if"), new FunctionTypeResolver(
                new MonoType("function", v, BOOLEAN, v, v)));
        Form expression = form(sym("if"), lit(true), lit(1), sym("x"));
        Context result = expression.resolve(context);
        assertNotNull(result);
        System.out.println(expression);
        System.out.println(expression.bind);
        assertEquals(INT, expression.resolvedType());
        assertEquals(BOOLEAN, expression.get(1).resolvedType());
        assertEquals(INT, expression.get(2).resolvedType());
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
            .add(sym("set"), new FunctionTypeResolver(new MonoType("function", X, X, X)))
            .add(sym("+"), new FunctionTypeResolver(new MonoType("function", INT, INT, INT)))
            .add(sym("+"), new FunctionTypeResolver(new MonoType("function", STRING, STRING, STRING)))
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
        assertEquals(STRING, expression.resolvedType());
        assertEquals(INT, expression.get(1).resolvedType());
        assertEquals(STRING, expression.get(2).resolvedType());
        assertEquals(STRING, expression.get(3).resolvedType());
    }

    @Test
    void testResolveProgDefine() {
        Symbol x = sym("x");
        Symbol y = sym("y");
        VariableType X = new VariableType("X");
        Context context = new Context()
//            .add(sym("x"))
//            .add(sym("y"))
            .add(sym("set"), new FunctionTypeResolver(new MonoType("function", X, X, X)))
            .add(sym("+"), new FunctionTypeResolver(new MonoType("function", INT, INT, INT)))
            .add(sym("+"), new FunctionTypeResolver(new MonoType("function", STRING, STRING, STRING)))
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
        assertEquals(STRING, expression.resolvedType());
        assertEquals(VOID, expression.get(1).resolvedType());
        assertEquals(VOID, expression.get(2).resolvedType());
        assertEquals(INT, expression.get(3).resolvedType());
        assertEquals(STRING, expression.get(4).resolvedType());
        assertEquals(STRING, expression.get(5).resolvedType());
    }

}
