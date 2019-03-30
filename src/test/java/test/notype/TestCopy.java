package test.notype;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import notype.type.MonoType;
import notype.type.Type;
import notype.type.VariableType;

class TestCopy {

    static MonoType INT = new MonoType("int");
    static MonoType BOOL = new MonoType("boolean");
    static MonoType PLUS = new MonoType("function", INT, INT, INT);
    static VariableType X = new VariableType("X");

    @Test
    void testMonoType0() {
        assertEquals(INT, INT.copy(new HashMap<>()));
    }

    @Test
    void testMonoType2() {
        Type t = PLUS.copy();
        assertTrue(t instanceof MonoType);
        MonoType m = (MonoType)t;
        assertEquals("function", m.name);
        assertEquals(3, m.size());
        assertEquals(INT, m.get(0));
        assertEquals(INT, m.get(1));
        assertEquals(INT, m.get(2));
    }

    @Test
    void testVariableType() {
        Type t = X.copy();
        assertTrue(t instanceof VariableType);
        assertNotEquals(X, t);
    }

    @Test
    void testFunction() {
        VariableType x = new VariableType("x");
        MonoType cond = new MonoType("function", x, BOOL, x, x);
        Type copy = cond.copy();
        System.out.println(cond + " -> " + copy);
        assertTrue(copy instanceof MonoType);
        MonoType m = (MonoType)copy;
        assertEquals("function", m.name);
        assertEquals(4, m.size());
        assertEquals(BOOL, m.get(1));
        assertEquals(m.get(0), m.get(2));
        assertEquals(m.get(0), m.get(3));
    }

    @Test
    void testNestedFunction() {
        VariableType E = new VariableType("E");
        MonoType list = new MonoType("list", E);
        MonoType predicate = new MonoType("function", BOOL, E);
        MonoType filter = new MonoType("function", list, list, predicate);
        Type copy = filter.copy();
        System.out.println(filter + " -> " + copy);
        assertTrue(copy instanceof MonoType);
        MonoType m = (MonoType)copy;
        assertEquals("function", m.name);
        assertEquals(3, m.size());
        assertTrue(m.get(0) instanceof MonoType);
        MonoType m0 = (MonoType)m.get(0);
        assertEquals("list", m0.name);
        assertTrue(m.get(1) instanceof MonoType);
        MonoType m1 = (MonoType)m.get(1);
        assertEquals("list", m1.name);
        assertTrue(m.get(2) instanceof MonoType);
        MonoType m2 = (MonoType)m.get(2);
        assertEquals("function", m2.name);
        // コピーされた変数は同一であること。
        assertEquals(((MonoType)m0).get(0), ((MonoType)m1).get(0));
        assertEquals(((MonoType)m0).get(0), ((MonoType)m2).get(1));
        // 変数はコピーされていること。
        assertNotEquals(E, ((MonoType)m0).get(0));
    }

}
