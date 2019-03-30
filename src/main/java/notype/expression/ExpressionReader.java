package notype.expression;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import notype.type.MonoType;

public class ExpressionReader {

    static final Symbol EOF_OBJECT = new Symbol("#EOF#");
    static final int EOF = -1;
    int ch;

    static boolean isSpace(int ch) {
        return Character.isWhitespace(ch);
    }

    static boolean isDigit(int ch) {
        return Character.isDigit(ch);
    }

    static boolean isFirstSymbol(int ch) {
        if (Character.isAlphabetic(ch))
            return true;
        if (Character.isDigit(ch))
            return false;
        if (ch <= ' ')
            return false;
        switch (ch) {
        case '?':
        case ';':
        case '"':
        case '\'':
        case '(':
        case ')':
        case '[':
        case ']':
        case '{':
        case '}':
            return false;
        }
        return true;
    }

    static boolean isRestSymbol(int ch) {
        if (isFirstSymbol(ch))
            return true;
        if (isDigit(ch))
            return true;
        switch (ch) {
        case '?':
            return true;
        }
        return false;
    }

    static final Expression[] EXPRESSION_ARRAY = new Expression[0];

    static Literal literal(boolean l) {
        return new Literal(l, MonoType.BOOLEAN);
    }

    static Literal literal(char l) {
        return new Literal(l, MonoType.CHAR);
    }

    static Literal literal(int l) {
        return new Literal(l, MonoType.INT);
    }

    static Literal literal(long l) {
        return new Literal(l, MonoType.LONG);
    }

    static Literal literal(float l) {
        return new Literal(l, MonoType.FLOAT);
    }

    static Literal literal(Double l) {
        return new Literal(l, MonoType.DOUBLE);
    };

    static Literal literal(String l) {
        return new Literal(l, MonoType.STRING);
    };

    static Literal literal(BigInteger l) {
        return new Literal(l, MonoType.BIG_INTEGER);
    };

    final Reader reader;

    public ExpressionReader(Reader reader) {
        try {
            this.reader = reader;
            get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ExpressionReader(String s) {
        this(new StringReader(s));
    }

    int get() throws IOException {
        return ch = reader.read();
    }

    void skipSpace() throws IOException {
        while (isSpace(ch))
            get();
    }

    Form readList() throws IOException {
        get();
        List<Expression> list = new ArrayList<>();
        while (true) {
            skipSpace();
            switch (ch) {
            case ')':
                get();
                return new Form(list.toArray(EXPRESSION_ARRAY));
            case -1:
                throw new ExpressionSyntaxException("')' expected");
            default:
                list.add(read());
                break;
            }
        }
    }

    Literal readEscape() throws IOException {
        char r;
        switch (get()) {
        case 'b': r = '\b'; break;
        case 'f': r = '\f'; break;
        case 't': r = '\t'; break;
        case 'n': r = '\n'; break;
        case 'r': r = '\r'; break;
        case '\"': r = '\"'; break;
        case '\\': r = '\\'; break;
        default: throw new ExpressionSyntaxException("unknown character ?\\%c", ch);
        }
        get();
        return literal(r);
    }

    Expression readString() throws IOException {
        get();
        StringBuilder sb = new StringBuilder();
        while (true) {
            switch (ch) {
            case '"':
                get();
                return literal(sb.toString());
            case '\\':
                sb.append(readEscape());
                break;
            case '\b': case '\f': case '\r': case '\n':
                get();
                throw new ExpressionSyntaxException("unterminated string literal %s", sb);
            default:
                sb.append((char) ch);
                get();
                break;
            }
        }
    }

    Literal readChar() throws IOException {
        switch (get()) {
        case '\\': return readEscape();
        case '\b': case '\f': case '\r': case '\n':
            get();
            throw new ExpressionSyntaxException("unterminated character literal");
        default:
            char r = (char) ch;
            get();
            return literal(r);
        }
    }

    Expression readSymbol() throws IOException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append((char) ch);
            get();
        } while (isRestSymbol(ch));
        String s = sb.toString();
        switch (s) {
        case "true": return literal(Boolean.TRUE);
        case "false": return literal(Boolean.FALSE);
        // case "null": return new Literal(null, MonoType.OBJECT);
        default:
            return new Symbol(s);
        }
    }

    Literal readNumber() throws IOException {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append((char) ch);
            get();
        } while (isDigit(ch));
        switch (Character.toUpperCase(ch)) {
        case 'L': get(); return literal(Long.valueOf(sb.toString()));
        case 'D': get(); return literal(Double.valueOf(sb.toString()));
        case 'I': get(); return literal(new BigInteger(sb.toString()));
        case '.':
            sb.append((char) ch);
            get();
            while (isDigit(ch)) {
                sb.append((char) ch);
                get();
            }
            if (Character.toUpperCase(ch) == 'E') {
                sb.append((char) ch);
                get();
                if (ch == '+' || ch == '-') {
                    sb.append((char) ch);
                    get();
                }
                while (isDigit(ch)) {
                    sb.append((char) ch);
                    get();
                }
            }
            switch (Character.toUpperCase(ch)) {
            case 'F': get(); return literal(Float.valueOf(sb.toString()));
            case 'D': get(); return literal(Double.valueOf(sb.toString()));
            default: return literal(Double.valueOf(sb.toString()));
            }
        default:
            return literal(Integer.valueOf(sb.toString()));
        }
    }

    public Expression read() {
        try {
            skipSpace();
            switch (ch) {
            case '(':
                return readList();
            // case '\'': return readQuote();
            case '"':
                return readString();
            case '?':
                return readChar();
            case EOF:
                return EOF_OBJECT;
            default:
                if (isFirstSymbol(ch))
                    return readSymbol();
                else if (isDigit(ch))
                    return readNumber();
                else {
                    get();
                    throw new ExpressionSyntaxException("unknown charcter '%s'", (char) ch);
                }
            }
        } catch (IOException e) {
            throw new ExpressionSyntaxException(e);
        }
    }
}
