package expression.parser;

import expression.*;

public class ExpressionParser extends AbstractParser implements Parser{

    public static final String max = Integer.toString(Integer.MAX_VALUE);

    @Override
    public FunctionAndMembers parse(String expression) throws NumberFormatException, MyOverflowException, EquationException {
        this.source = new StringSource(expression);
        nextChar();
        return parseExpression();
    }

    private FunctionAndMembers parseExpression() throws NumberFormatException, MyOverflowException, EquationException {
        return parseOr();
    }

    private FunctionAndMembers parseOr() throws NumberFormatException, MyOverflowException, EquationException {
        try {
            FunctionAndMembers func = parseTwo('|');
            return func;
        } catch (NumberFormatException | MyOverflowException | EquationException e) {
            return parseExpression();
        }
    }

    private FunctionAndMembers parseAnd() throws NumberFormatException, MyOverflowException, EquationException {
        try {
            FunctionAndMembers func = parseTwo('&');
            return func;
        } catch (NumberFormatException | MyOverflowException | EquationException e) {
            return parseExpression();
        }
    }

    private FunctionAndMembers parseXOR() throws NumberFormatException, MyOverflowException, EquationException {
        try {
            FunctionAndMembers func = parseTwo('^');
            return func;
        } catch (NumberFormatException | MyOverflowException | EquationException e) {
            return parseExpression();
        }
    }

    private FunctionAndMembers parseLowPrior() throws NumberFormatException, MyOverflowException, EquationException {
        try {
            FunctionAndMembers func = parseTwo('+', '-');
            return func;
        } catch (NumberFormatException | MyOverflowException | EquationException e) {
            return parseExpression();
        }
    }

    private FunctionAndMembers parseMidPrior() throws NumberFormatException, MyOverflowException, EquationException {
        try {
            FunctionAndMembers func = parseTwo('*', '/');
            return func;
        } catch (NumberFormatException | MyOverflowException | EquationException e) {
            return parseExpression();
        }
    }

    private FunctionAndMembers parseTopPrior() throws NumberFormatException, MyOverflowException, EquationException {
        skipWhitespace();
        if (checkConst()) {
            try {
                FunctionAndMembers constant = parseConst();
                return constant;
            } catch (NumberFormatException e) {
                return parseExpression();
            }
        }
        if (sym == '-') {
            return parseOneElem();
        }
        if (test('(')) {
            FunctionAndMembers low = parseOr();
            while (!test(')'));
            return low;
        }
        if (test(')')) {
            try {
                throw new EquationException();
            } catch (EquationException e) {
                System.err.println("Invalid expression");
                return parseExpression();
            }
        }
        Variable variable = new Variable(Character.toString(sym));
        nextChar();
        return variable;
    }

    private FunctionAndMembers parseOneElem() throws NumberFormatException, MyOverflowException, EquationException {
        FunctionAndMembers save;
        if (test('-')) {
            skipWhitespace();
            if (checkConst()) {
                try {
                    FunctionAndMembers constant = parseMinusConst();
                    return constant;
                } catch (NumberFormatException e) {
                    return parseExpression();
                }
            }
            if (test('(')) {
                FunctionAndMembers low = parseOr();
                while (!test(')'));
                return new PrefMinus(low);
            }
            if (test(')')) {
                try {
                    throw new EquationException();
                } catch (EquationException e) {
                    System.err.println("Invalid expression");
                    return parseExpression();
                }
            }
            if (checkVariable()) {
                PrefMinus prefMinus = new PrefMinus(new Variable(Character.toString(sym)));
                nextChar();
                return prefMinus;
            }
            save = parseOneElem();
            return new PrefMinus(save);
        }
        try {
            FunctionAndMembers constant = parseMinusConst();
            return constant;
        } catch (NumberFormatException e) {
            return parseExpression();
        }
    }

    private FunctionAndMembers parseTwo(char firstOperation, char secondOperation) throws NumberFormatException, MyOverflowException, EquationException {
        FunctionAndMembers first;
        if (firstOperation == '+') {
            try {
                first = parseMidPrior();
            } catch (NumberFormatException | MyOverflowException | EquationException e) {
                return parseExpression();
            }
        } else {
            try {
                first = parseTopPrior();
            } catch (NumberFormatException | MyOverflowException | EquationException e) {
                return parseExpression();
            }
        }
        while (!test(EOF)) {
            skipWhitespace();
            char operator = sym;
            if (operator != firstOperation && operator != secondOperation) {
                break;
            }
            nextChar();
            FunctionAndMembers second;
            if (firstOperation == '+') {
                second = parseMidPrior();
            } else {
                second = parseTopPrior();
            }
            if (operator == '+') {
                first = new Add(first, second);
            } else if (operator == '-') {
                first = new Subtract(first, second);
            } else if (operator == '*') {
                first = new Multiply(first, second);
            } else if (operator == '/') {
                first = new Divide(first, second);
            }
        }
        return first;
    }

    private FunctionAndMembers parseTwo(char operation) throws NumberFormatException, MyOverflowException, EquationException {
        FunctionAndMembers left;
        try {
            left = getLogic(operation);
        } catch (NumberFormatException | MyOverflowException | EquationException e) {
            return parseExpression();
        }
        while (!test(EOF)) {
            skipWhitespace();
            char op = sym;
            if (op != operation) {
                break;
            }
            nextChar();
            FunctionAndMembers right;
            try {
                right = getLogic(operation);
            } catch (NumberFormatException | MyOverflowException | EquationException e) {
                return parseExpression();
            }
            if (op == '|') {
                left = new Or(left, right);
            } else if (op == '^') {
                left = new XOR(left, right);
            } else if (op == '&') {
                left = new And(left, right);
            }
        }
        return left;
    }

    private FunctionAndMembers getLogic(char operation) throws NumberFormatException, MyOverflowException, EquationException{
        if (operation == '|') {
            return parseXOR();
        }
        if(operation == '^') {
            return parseAnd();
        }
        try {
            FunctionAndMembers funct = parseLowPrior();
            return funct;
        } catch (NumberFormatException | MyOverflowException | EquationException e) {
            return parseExpression();
        }
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(sym)) {
            nextChar();
        }
    }

    private FunctionAndMembers parseConst() throws NumberFormatException, MyOverflowException {
        skipWhitespace();
        StringBuilder value = new StringBuilder();
        while (checkConst()) {
            value.append(sym);
            nextChar();
        }
        if (value.indexOf("0") == 0) {
            throw new NumberFormatException("Invalid number format");
        }
        if (value.toString().compareTo(max) > 0) {
            throw new MyOverflowException("overflow");
        }
        return new Const(Integer.parseInt(value.toString()));
    }

    private FunctionAndMembers parseMinusConst() throws NumberFormatException, MyOverflowException {
        skipWhitespace();
        StringBuilder value = new StringBuilder();
        while (checkConst()) {
            value.append(sym);
            nextChar();
        }
        if (value.indexOf("0") == 0) {
            throw new NumberFormatException("Invalid number format");
        }
        if (value.toString().compareTo(max) > 0) {
            throw new MyOverflowException("overflow");
        }
        return new Const(Integer.parseInt(value.insert(0, '-').toString()));
    }
}