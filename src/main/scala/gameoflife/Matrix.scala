package gameoflife

object Matrix {
  /*
    Type of the matrix/grid

    We are only store live cells
   */
  type Matrix = Set[Cell]

  /* a cell is represented by a tuple */ 
  type Cell = (Int, Int)

  // Constantts
  val MatrixSize = 200 // 200 x 200
  val Generations = 99 //

  /*
    Get all the neighbouring cells that satisfy a certain condition

    @param cell: the cell in question
    @param cond: the condition a neighbouring cell needs to satisfy to be included in the result
    @return a set of all the neighbouring cells satisfying the given condition
   */
  def getNeighbourCells(cell: Cell, cond: Cell => Boolean): Set[Cell] = {
    val (x, y) = cell
    val neighbourCells = for {
      dx <- -1 to 1
      dy <- -1 to 1
      if (dx, dy) != (0, 0) && cond((x + dx, y + dy))
    } yield (x + dx, y + dy)

    neighbourCells.toSet
  }

  /*
    Format a list of matrix states so that it can be printed out per the required output format
   */
  def formatMatrix(matrixStates: List[Matrix]): List[String] =
    if matrixStates.nonEmpty then
      matrixStates
        .zipWithIndex
        .flatMap (
          (state, index) => {
            val liveCells = state
              .map { case (x, y) => s"[$x, $y]" }
              .mkString("[", ", ", "]")
            List(s"${index+1}: $liveCells")
          }
        )
    else
      List("[]")
}
