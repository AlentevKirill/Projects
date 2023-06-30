"use strict";

function Const(c) {
    this.c = c;
}
Const.prototype = {
    evaluate: function(x, y, z) {
        return this.c
    },
    toString: function() {
        return this.c.toString()
    }
}

function AbstractBinaryExpression(f1, f2, operation, operand) {
    this.f1 = f1;
    this.f2 = f2;
    this.operation = operation;
    this.operand = operand;
}
AbstractBinaryExpression.prototype = {
    evaluate: function(x, y, z) {
        return this.operation(this.f1.evaluate(x, y, z), this.f2.evaluate(x, y, z))
    },
    toString: function() {
        return this.f1.toString() + " " + this.f2.toString() + " " + this.operand.toString()
    }
}

function Add(f1, f2) {
    return new AbstractBinaryExpression(f1, f2, (f1, f2) => f1 + f2, "+")
}

function Subtract(f1, f2) {
    return new AbstractBinaryExpression(f1, f2, (f1, f2) => f1 - f2, "-")
}

function Multiply(f1, f2) {
    return new AbstractBinaryExpression(f1, f2, (f1, f2) => f1 * f2, "*")
}

function Divide(f1, f2) {
    return new AbstractBinaryExpression(f1, f2, (f1, f2) => f1 / f2, "/")
}

function Variable(v) {
    this.v = v;
}
Variable.prototype = {
    evaluate: function(x, y, z) {
        if (this.v === 'x') {
            return x;
        } else if (this.v === 'y') {
            return y;
        } else {
            return z;
        }
    },
    toString: function() {
        return this.v.toString()
    }
}

function Negate(f) {
    this.f = f;
}
Negate.prototype = {
    evaluate: function(x, y, z) {
        return -this.f.evaluate(x, y, z)
    },
    toString: function() {
        return this.f.toString() + " negate"
    }
}

function Avg5(f1, f2, f3, f4, f5) {
    this.f1 = f1;
    this.f2 = f2;
    this.f3 = f3;
    this.f4 = f4;
    this.f5 = f5;
}
Avg5.prototype = {
    evaluate: function(x, y, z) {
        return (this.f1.evaluate(x, y, z) + this.f2.evaluate(x, y, z)
        + this.f3.evaluate(x, y, z) + this.f4.evaluate(x, y, z) + this.f5.evaluate(x, y, z)) / 5
    },
    toString: function() {
        return this.f1.toString() + " " + this.f2.toString() + " " + this.f3.toString() + " "
        + this.f4.toString() + " " + this.f5.toString() + " avg5"
    }
}

function Med3(f1, f2, f3) {
    this.f1 = f1;
    this.f2 = f2;
    this.f3 = f3;
}
Med3.prototype = {
    evaluate: function(x, y, z) {
        let arr = [this.f1.evaluate(x, y, z), this.f2.evaluate(x, y, z), this.f3.evaluate(x, y, z)]
        arr.sort((a, b) => a - b);
        return arr[1];
    },
    toString: function() {
        return this.f1.toString() + " " + this.f2.toString() + " " + this.f3.toString() + " med3"
    }
}