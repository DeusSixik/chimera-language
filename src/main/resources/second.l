// Factorial function
fn factorial(n: int) {
    if n <= 0 return 0;
    if n == 1 return 1;
    return factorial(n-1) * n;
}

// Fib function
fib = fn(n: int) {
    if n <= 0 return 0;
    if n == 1 return 1;
    return fib(n-1) + fib(n-2);
};

first = factorial(7);
second = fib(10);

print("Result: ", first + second);