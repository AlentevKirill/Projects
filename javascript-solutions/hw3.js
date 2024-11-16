const funcInvoke = (f) => Object.assign(f, { valueOf: f } );

const curry = funcInvoke((f) => (...args) => {
    if (args.length === 1) {
        if (typeof args[0] !== "number") {
            throw new Error("the argument is not a number")
        }
    } else {
        if (typeof args[1] !== "number") {
            throw new Error("the argument is not a number")
        }
    }
    return args.length? funcInvoke(curry(f.bind(0, ...args))): f()
});

const eval = (...values) => {
    /*if (typeof values[0] !== 'number') {
        throw new Error("the argument is not a number")
    }*/
    let result = values[0];
    for (let key = 1; key < values.length;) {
        /*if (typeof values[key + 1] !== 'number') {
            throw new Error("the argument is not a number")
        }*/
        switch(values[key]) {
            case '+':
                result += values[key + 1];
                break;
            case '-':
                result -= values[key + 1];
                break;
            case '*':
                result = result * values[key + 1];
                break;
            case '/':
                result = result / values[key + 1];
                break;
            case '%':
                result = result % values[key + 1];
                break;
            case '**':
                result = result ** values[key + 1];
                break;
            default:
                throw new Error("unsupported sign");
        }
        key += 2;
    }
    return result;
}

let calc = curry(eval);


console.log(calc(1)('+', 2)('-', 1)('*', 2) + 0);
console.log(typeof calc(1) === 'function');