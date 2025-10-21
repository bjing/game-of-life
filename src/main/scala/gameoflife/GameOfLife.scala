package gameoflife

import cats.effect.IO
import cats.implicits._
import cats.syntax.all._  // for traverse_

type Cell = (Int, Int)
// TODO no need for map because the boolean value are redundant
// However, the constant-time access of a Map is efficient for updating states
// Think over it once tests are done
type Matrix = Map[Cell, Boolean]
val MatrixSize = 200 // 200 x 200
val Generations = 100 //

object GameOfLife {
  def runGame(matrix: Matrix, matrixSize: Int, generations: Int): IO[Unit] =
    for {
      matrixStates <- IO.pure {
        LazyList.iterate(matrix)(nextGeneration(_, matrixSize))
          .slice(1, generations + 1)
          .map(_.keySet)
          .filter(_.nonEmpty)
          .toList
      }
      formatted = formatMatrixStates(matrixStates)
      _ <- formatted.traverse_(liveCells => IO.println(liveCells))
    } yield {}

  /*
    Populate matrix Map with a list of live cell data
  */
  def initMatrix(liveCells: List[Cell]): Matrix = {
    liveCells.map {
      case (x, y) => ((x, y),  true)
    }.toMap
  }


  /*
    Format a list of matrix states so that it can be printed out per the required output format
   */
  def formatMatrixStates(matrixStates: List[Set[Cell]]): List[String] =
    if matrixStates.nonEmpty then
      matrixStates
        .zip(1 to matrixStates.length)
        .flatMap (
          (state, index) => {
            val liveCells = state
              .map { case (x, y) => s"[$x, $y]" }
              .mkString("[", ", ", "]")
            List(s"$index: $liveCells")
          }
        )
    else
      List("[]")

  /*
    Generate next state for matrix
    Only store live cells coordinates in Map
   */
  def nextGeneration(matrix: Matrix, matrixSize: Int): Matrix = {
    // When going through the matrix, only go through live cells and their neighbours
    // instead of going through the whole matrix to be efficient.
    // This is particularly efficient for sparse matrix
    val relevantCells: Set[Cell] = {
      matrix.keySet.flatMap { (x, y) =>
        for {
          dx <- -1 to 1
          dy <- -1 to 1
          currentX = x + dx
          currentY = y + dy
          if 0 <= currentX && currentX <= matrixSize && currentY >= 0 && currentY < matrixSize
        } yield (currentX, currentY)
      }
    }

    // Calculate the next state for the relevant cells, and then filter only live Cells
    relevantCells
      .filter(nextStateForCell(_, matrix))
      .map(_ -> true)
      .toMap
  }

  /*
    Calculate next state for given cell coordinates
  */
  def nextStateForCell(cell: Cell, matrix: Matrix): Boolean = {
    val (x, y) = cell
    val neighbourStates = for {
      dx <- -1 to 1
      dy <- -1 to 1
      if (dx, dy) != (0, 0)
    } yield {
      matrix.getOrElse((x + dx, y + dy), false)
    }

    val alive = matrix.getOrElse(cell, false)
    val numLiveNeighbours = neighbourStates.count(_ == true)
    (alive, numLiveNeighbours) match {
      case (true, 2|3) => true
      case (false, 3) => true
      case _ => false
    }
  }
}
