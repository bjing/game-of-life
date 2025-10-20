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
        // TODO bug found, we are not iterating on the previous state
        (1 to generations).toList.flatMap { gen =>
          val newState = nextGeneration(matrix, matrixSize)
          if newState.keySet.size.isEmpty then None
          else Some(newState.keySet)
        }
      }
      _ <- printMatrixStates(matrixStates)
    } yield ()

  /*
    Populate matrix Map with a list of live cell data
  */
  def initMatrix(liveCells: List[Cell]): Matrix = {
    liveCells.map {
      case (x, y) => ((x, y),  true)
    }.toMap
  }

  def printMatrixStates(matrixStates: List[Set[Cell]]): IO[Unit] =
    if matrixStates.nonEmpty then
      matrixStates
        .zip(1 to matrixStates.length)
        .traverse_ (
          (state, index) => {
            val formattedState = state
              .map { case (x, y) => s"[$x, $y]" }
              .mkString("[", ", ", "]")
            IO.println(s"$index: $formattedState")
          }
        )
    else
      IO.println("[]")

  /*
    Generate next state for matrix
    Only store live cells coordinates in Map
   */
  def nextGeneration(matrix: Matrix, matrixSize: Int): Matrix = {
    val newState =
      (for {
        x <- 0 until matrixSize
        y <- 0 until matrixSize
      } yield {
        val nextState = nextStateForCell((x, y), matrix)
        if nextState then Some((x, y), nextState)
        else None
      }).flatten

    newState.toMap
  }

  /*
    Calculate next state for given cell coordinates
  */
  def nextStateForCell(cell: Cell, matrix: Matrix): Boolean = {
    val neighbourStates = cell match {
      case (x, y) =>
        List(
          // No need to consider coordinates that are out of range, cuz that
          // out of range cell would be equivalent to a dead cell (false)
          matrix.getOrElse((x - 1, y - 1), false),
          matrix.getOrElse((x, y - 1), false),
          matrix.getOrElse((x + 1, y - 1), false),
          matrix.getOrElse((x - 1, y), false),
          matrix.getOrElse((x + 1, y), false),
          matrix.getOrElse((x - 1, y + 1), false),
          matrix.getOrElse((x, y + 1), false),
          matrix.getOrElse((x + 1, y + 1), false),
        )
    }

    val currentState = matrix.getOrElse(cell, false)
    val numLiveNeighbours = neighbourStates.count(_ == true)

    if !currentState then {
      if numLiveNeighbours == 3 then true
      else false
    } else {
      if numLiveNeighbours == 2 || numLiveNeighbours == 3 then
        true
      else false
    }
  }
}
