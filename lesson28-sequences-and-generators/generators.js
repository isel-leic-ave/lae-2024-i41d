/**
 * @returns {Generator<number, void, *>} Lazy
 */
function* foo(){
    console.log("Starting")
    yield(7)
    console.log("Step 1")
    yield(11)
    console.log("Step 2")
    yield(33)
    console.log("Step 3")
}

function bar() {
    yield(11) // CANNOT be used in a normal function
}

// bar()

const nrs = foo()
// for (const n of nrs) { console.log(n) }
console.log(nrs.next().value)
console.log(nrs.next().value)