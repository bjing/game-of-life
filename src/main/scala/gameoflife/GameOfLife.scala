package gameoflife

import cats.effect.IO
import cats.implicits.*
import cats.syntax.all.*
import gameoflife.constants.{Generations, MatrixSize}
import gameoflife.types.{Cell, Matrix}  // for traverse_

object GameOfLife {
  def runGame(initCells: Set[Cell], matrixSize: Int, generations: Int): IO[Unit] = {
    val matrix = initMatrix(initCells)

    // keep iterating and generating a new matrix of cells based on the previous generation
    val matrixStates =
      LazyList.iterate(matrix)(nextGeneration(_, matrixSize))
        .slice(1, generations + 1)
        .map(_.keySet)
        .filter(_.nonEmpty)
        .toList

    // format all generation of matrix and print them out
    val formatted = formatMatrixStates(matrixStates)
    formatted.traverse_(IO.println)
  }

  /*
    Populate matrix Map with a list of live cell data
  */
  def initMatrix(liveCells: Set[Cell]): Matrix = {
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
          newX = x + dx
          newY = y + dy
          if newX >= 0 && newX < matrixSize && newY >= 0 && newY < matrixSize
        } yield (newX, newY)
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
