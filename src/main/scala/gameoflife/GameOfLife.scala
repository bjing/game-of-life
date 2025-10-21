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

    TODO may not need to loop over every coordinate, think about how to do this once
      we've fixed other bugs
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
