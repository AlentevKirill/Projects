package expression.parser;

public abstract class AbstractParser implements Parser {
    protected CharSource source;
    protected static final char EOF = 0;
    protected static char sym;

    protected void nextChar() {
        if (source.hasNext()) {
            sym = source.next();
        } else {
            sym = EOF;
        }
    }

    protected boolean test(char expected) {
        if (sym == expected) {
            nextChar();
            return true;
        } else {
            return false;
        }
    }

    protected boolean checkVariable() {
        return Character.isLetter(sym);
    }

    protected boolean checkConst() {
        return Character.isDigit(sym);
    }

}
