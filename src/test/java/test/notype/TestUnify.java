package test.notype;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import notype.type.MonoType;
import notype.type.Type;
import notype.type.TypeBind;
import notype.type.VariableGroup;
import notype.type.VariableType;

class TestUnify {

    static final MonoType INT = new MonoType("int");
    static final MonoType STRING = new MonoType("string");
    static final MonoType PLUS = new MonoType("function", INT, INT, INT);
    static final VariableType X = new VariableType("X");
    static final VariableType Y = new VariableType("Y");
    static final VariableType Z = new VariableType("Z");

    @Test
    void testIntInt() {
        TypeBind b0 = TypeBind.ROOT;
        TypeBind b1 = INT.unify(INT, b0);
        assertNotNull(b1);
        assertEquals(b1, b0);
        assertEquals("{}", b1.toString());
        assertEquals(INT, INT.resolve(b1));
    }

    @Test
    void testIntInt2() {
        MonoType INT2 = new MonoType("int");
        TypeBind b0 = TypeBind.ROOT;
        TypeBind b1 = INT.unify(INT2, b0);
        assertNotNull(b1);
        assertEquals(b1, b0);
        assertEquals(INT, INT.resolve(b1));
        assertEquals(INT2, INT2.resolve(b1));
    }

    @Test
    void testIntString() {
        TypeBind b0 = TypeBind.ROOT;
        TypeBind b1 = INT.unify(STRING, b0);
        assertNull(b1);
    }

    @Test
    void testVariableInt() {
        TypeBind b0 = TypeBind.ROOT;
        TypeBind b1 = X.unify(INT, b0);
        assertEquals(INT, b1.get(X));
        assertEquals("{X:int}", b1.toString());
        assertEquals(INT, X.resolve(b1));
    }

    @Test
    void testIntVariable() {
        TypeBind b0 = TypeBind.ROOT;
        TypeBind b1 = INT.unify(X, b0);
        assertEquals(INT, b1.get(X));
        assertEquals(INT, X.resolve(b1));
    }

    @Test
    void testVariableMonoType() {
        TypeBind b0 = TypeBind.ROOT;
        TypeBind b1 = X.unify(PLUS, b0);
        assertEquals(PLUS, b1.get(X));
        assertEquals(PLUS, X.resolve(b1));
    }

    @Test
    void testMonoTypeVariable() {
        TypeBind b0 = TypeBind.ROOT;
        TypeBind b1 = PLUS.unify(X, b0);
        assertEquals(PLUS, b1.get(X));
        assertEquals(PLUS, X.resolve(b1));
    }

    @Test
    void testMonoTypeVariable3() {
        MonoType f = new MonoType("function", X, Y);
        TypeBind b0 = TypeBind.ROOT;
        TypeBind b1 = f.unify(Z, b0);
        TypeBind b2 = X.unify(STRING, b1);
        TypeBind b3 = Y.unify(INT, b2);
        assertEquals(f, b3.get(Z));
        assertEquals(STRING, b3.get(X));
        assertEquals(INT, b3.get(Y));
        assertEquals(STRING, X.resolve(b3));
        assertEquals(INT, Y.resolve(b3));
        Type res = f.resolve(b3);
        assertEquals(new MonoType("function", STRING, INT), res);
    }

    @Test
    void test2Variable() {
        TypeBind b0 = TypeBind.ROOT;
        TypeBind b1 = X.unify(Y, b0);
        TypeBind b2 = Y.unify(INT, b1);
        assertEquals(VariableGroup.class, b1.get(X).getClass());
        assertTrue(b1.contains(X));
        assertTrue(b1.contains(Y));
        assertEquals(INT, b2.get(X));
        assertEquals(INT, b2.get(Y));
        assertEquals(INT, X.resolve(b2));
        assertEquals(INT, Y.resolve(b2));
    }


}
