// 1
const getNewObjWithPrototype = (obj) => {
    return Object.create(obj);
}

// 2
const getEmptyObj = () => {
    // code
}

// 3
const setPrototypeChain = ({ programmer, student, teacher, person }) => {
    Object.setPrototypeOf(programmer, student);
    Object.setPrototypeOf(student, teacher);
    Object.setPrototypeOf(teacher, person);
}

// 4
const getObjWithEnumerableProperty = () => {
    // code
}

// 5
const getWelcomeObject = (person) => {
    let result = {
        voice() {
        return `Hello, my name is ${super.name}. I am ${super.age}.`
        }
    }
    Object.setPrototypeOf(result, person);
    return result;
}

// 6
class Singleton {
    // code
}

// 7
const defineTimes = () => {
    Number.prototype.times = function(callback) {
        let value = this.valueOf();
        for (let i = 1; i <= value; i++) {
            callback(i, value);
        }
    }
}

// 8
const defineUniq = () => {
    Object.defineProperty(Array.prototype, "uniq", {
        get: function () {
            let arr = this.valueOf();
            let set = new Set();
            for (let key = 0; key < arr.length; key++) {
                set.add(arr[key]);
            }
            return Array.from(set);
        }
    });
}

// 9
const defineUniqSelf = () => {
    Object.defineProperty(Array.prototype, "uniqSelf", {
        get: function () {
            let arr = this;
            let set = new Set();
            for (let key = 0; key < arr.length; key++) {
                set.add(arr[key]);
            }
            this.splice(0, this.length);
            let arr1 = Array.from(set);
            for (let i of arr1.values()) {
                this.push(i);
            }
            return arr1;
        }
    });
}


defineUniqSelf();
let arr = [1, 2, 2];

console.log(arr.uniqSelf); // [1,2];
console.log(arr); // [1,2,2];
