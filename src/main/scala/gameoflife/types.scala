package gameoflife

object types {
  /*
    Type of the matrix/grid
  
    TODO no need for map because the boolean value are redundant
    For example, we could just keep a list/set of live cells
    However, the constant-time access of a Map is efficient for updating states
    Think over it once tests are done
   */ 
  type Matrix = Map[Cell, Boolean]

  /* a cell is represented by a tuple */ 
  type Cell = (Int, Int)
}
