# Game of Life
# TODO talk about my solution briefly noting anything 
that a non-functional programmer may not know, so that the 
reviewer can look at my code with more insight.

## How to build and run the program
### Build the project
```shell
sbt compile
```

### Run all tests
```shell
sbt test
```

Run the program with input string
```shell
`sbt 'run [[1,1]]'`
```
or
```shell
sbt 'run "[[5, 5], [6, 5], [7, 5], [5, 6], [6, 6], [7, 6]]"'
```