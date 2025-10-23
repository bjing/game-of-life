# Game of Life
## Introduction

### Coding style
I implemented the solution in functional Scala utilising Cats and Cats Effect.

For those who are not familiar with functional style of coding, It's a coding paradigm that
utilises heavily on functions and data instead of object abstraction. 

The principle is that it separates side effects from pure code, Generally speaking, business 
logic should sit mostly, if not all, in pure code so that the logic can be more easily tested 
without having to resort to mocking.

### Data structure considerations
I initially implemented the data structure as a Map. However, as the matrix gets bigger, 
it can be wasteful for storage space, especially for sparse matrix, so I changed it to storing 
only the live cells in the Map. 

This is actually equivalent to storing the live cells in a Set as both have an average 
case of `O(1)` time complexity for adding and removing, and worst case of `O(n)`/`O(logn)` time
complexity for the same actions depending on the underlying implementation.

Therefore, the data structure evolved from a Map to a Set storing only live cells.

### Other considerations
I coded the matrix size `200` and the generations to run `100` as constants.

These could be extracted as command line arguments or made configurable in a config file. 
I didn't do either to keep the program simple.

## How to run the program
```shell
./run "input string"
```
For example
```shell
./run.sh "[[1,1]]"
```
or
```shell
./run.sh "[[5, 5], [6, 5], [7, 5], [5, 6], [6, 6], [7, 6]]"
```


## How to build the project and run tests
### Build the project
```shell
sbt compile
```

### Run all tests
```shell
sbt test
```
