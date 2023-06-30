"use strict";

const cnst = value => (x, y, z) => value;

let abstractBinaryFunc = (operation, func1, func2) => (x, y, z) => operation(func1(x, y, z), func2(x, y, z));

let add = (func1, func2) => abstractBinaryFunc((f1, f2) => f1 + f2, func1, func2);

let subtract = (func1, func2) => abstractBinaryFunc((f1, f2) => f1 - f2, func1, func2);

let divide = (func1, func2) => abstractBinaryFunc((f1, f2) => f1 / f2, func1, func2);

let multiply = (func1, func2) => abstractBinaryFunc((f1, f2) => f1 * f2, func1, func2);

let negate = (func) => (x, y, z) => -func(x, y, z);

const variable = check => (x, y, z) => {
	if (check === 'x') {
		return x;
	}
	if (check === 'y') {
		return y;
	}
	if (check === 'z') {
		return z;
	}
}

const one = cnst(1);
const two = cnst(2);