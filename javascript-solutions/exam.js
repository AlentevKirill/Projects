
const binarySearch = function (num, arr) {
    let left = 0;
    let right = arr.length - 1;
    while(left <= right) {
        let mid = Math.floor((left + right) / 2);
        if (arr[mid] === num) {
            while (arr[mid] === num) {
                mid--;
            }
            return mid + 1;
        } else if (arr[mid] > num) {
            right = mid - 1;
        } else {
            left = mid + 1;
        }
    }
    return undefined;
}

console.log(binarySearch(1, [0, 1])); // 2
console.log(binarySearch(15, [1,5,16,30,50,100])); // undefined