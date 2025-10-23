package gameoflife

object Types {
  /*
    Type of the matrix/grid

    We are only store live cells
   */
  type Matrix = Set[Cell]

  /* a cell is represented by a tuple */ 
  type Cell = (Int, Int)
}
