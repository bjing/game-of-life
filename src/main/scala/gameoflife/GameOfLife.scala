package gameoflife

import cats.effect.IO
import cats.implicits.*
import cats.syntax.all.*
import gameoflife.Types.{Cell, Matrix}  // for traverse_

object GameOfLife {
  def runGame(initMatrix: Matrix, matrixSize: Int, generations: Int): IO[Unit] = {
    // keep iterating and generating a new matrix of live cells based on the previous generation
    val matrixStates =
      LazyList.iterate(initMatrix)(nextGeneration(_, matrixSize))
        .slice(1, generations + 2)
        .filter(_.nonEmpty)
        .toList

    // format all generation of matrix and print them out
    val formatted = formatStates(matrixStates)
    formatted.traverse_(IO.println)
  }

  /*
    Generate next state for matrix
   */
  def nextGeneration(matrix: Matrix, matrixSize: Int): Matrix = {
    // When going through the matrix, only go through live cells and their neighbours
    // instead of going through the whole matrix to be efficient.
    // This is particularly efficient for sparse matrix
    val relevantCells: Set[Cell] = {
      matrix.flatMap { (x, y) =>
        for {
          dx <- -1 to 1
          dy <- -1 to 1
          newX = x + dx
          newY = y + dy
          if newX >= 0 && newX < matrixSize && newY >= 0 && newY < matrixSize
        } yield (newX, newY)
      }
    }

    // Calculate the next state for the relevant cells, and then filter only live Cells
    relevantCells
      .filter(nextStateForCell(_, matrix))
  }

  /*
    Calculate next state for given cell coordinates
  */
  def nextStateForCell(cell: Cell, matrix: Matrix): Boolean = {
    val (x, y) = cell
    val neighbourStates = for {
      dx <- -1 to 1
      dy <- -1 to 1
      if (dx, dy) != (0, 0) && matrix.contains((x + dx, y + dy))
    } yield (x + dx, y + dy)

    val alive = matrix.contains(cell)
    val numLiveNeighbours = neighbourStates.size
    (alive, numLiveNeighbours) match {
      case (true, 2|3) => true
      case (false, 3) => true
      case _ => false
    }
  }

  /*
    Format a list of matrix states so that it can be printed out per the required output format
   */
  def formatStates(matrixStates: List[Matrix]): List[String] =
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
