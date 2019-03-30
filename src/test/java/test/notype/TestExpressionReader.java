package test.notype;

import org.junit.jupiter.api.Test;

import notype.expression.Expression;
import notype.expression.ExpressionReader;

class TestExpressionReader {

    static Expression read(String s) {
        return new ExpressionReader(s).read();
    }

    @Test
    void test() {
        System.out.println(read("(prog (define x 0L) (set x (+ x 1L)))"));
    }

}
