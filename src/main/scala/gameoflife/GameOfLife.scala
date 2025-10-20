package gameoflife

import cats.effect.IO
import cats.implicits._
import cats.syntax.all._  // for traverse_

type Cell = (Int, Int)
type Matrix = Map[Cell, Boolean]
val MatrixSize = 200
val Generations = 100

object GameOfLife {
  def runGame(matrix: Matrix, matrixSize: Int, generations: Int): IO[Unit] = {
    (1 to generations).toList.traverse_ { gen =>
      val newState = nextGeneration(matrix, matrixSize)
      IO.println(s"$gen, ${newState.keySet}")
    }
  }

  def initMatrix(liveCells: List[Cell]): Matrix = {
    liveCells.map {
      case (x, y) => ((x, y),  true)
    }.toMap
  }

  // TODO test
  def nextGeneration(matrix: Matrix, matrixSize: Int): Matrix = {
    val newState =
      for {
        x <- 1 to matrixSize
        y <- 1 to matrixSize
      } yield {
        val nextState = nextStateForCell((x, y), matrix)
        ((x, y), nextState)
      }

    newState.toMap
  }

  // Calculate next state for given cell coordinates
  // TODO test
  def nextStateForCell(cell: Cell, matrix: Matrix): Boolean = {
    val neighbourStates = cell match {
      case (x, y) => {
        List(
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
    }

    val currentState = matrix.getOrElse(cell, false)
    val numLiveNeighbours = neighbourStates.length

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
