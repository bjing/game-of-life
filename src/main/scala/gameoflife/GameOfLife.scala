package gameoflife

import cats.effect.IO
import cats.implicits.*
import cats.syntax.all.*
import gameoflife.Matrix.{Cell, Matrix}  // for traverse_

object GameOfLife {
  def runGame(initMatrix: Matrix, matrixSize: Int, generations: Int): IO[Unit] = {
    // keep iterating and generating a new matrix of live cells based on the previous generation
    val matrixStates =
      LazyList.iterate(initMatrix)(nextStateForMatrix(_, matrixSize))
        .slice(1, generations + 2)
        .filter(_.nonEmpty)
        .toList

    // format all generation of matrix and print them out
    val formatted = Matrix.formatMatrix(matrixStates)
    formatted.traverse_(IO.println)
  }

  /*
    Generate next state for matrix
   */
  def nextStateForMatrix(matrix: Matrix, matrixSize: Int): Matrix = {
    // When going through the matrix, only go through live cells and their neighbours
    // instead of going through the whole matrix to be efficient.
    // This is particularly efficient for sparse matrix
    val relevantCells: Set[Cell] =
      matrix.flatMap {
        // make sure cells retrieved are within bounds of matrix/grid
        val withinBounds: Cell => Boolean =
          (x, y) => x >= 0 && x < matrixSize && y >= 0 && y < matrixSize

        Matrix.getNeighbourCells(_, withinBounds)
      }

    // Calculate the next state for the relevant cells, and then filter only live Cells
    relevantCells
      .filter(nextStateForCell(_, matrix))
  }

  /*
    Calculate next state for given cell coordinates
  */
  def nextStateForCell(cell: Cell, matrix: Matrix): Boolean = {
    val liveNeighbours = Matrix.getNeighbourCells(cell, matrix.contains)

    val alive = matrix.contains(cell)
    (alive, liveNeighbours.size) match {
      case (true, 2|3) => true
      case (false, 3) => true
      case _ => false
    }
  }

}
