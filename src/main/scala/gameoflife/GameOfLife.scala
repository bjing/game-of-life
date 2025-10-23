package gameoflife

import cats.effect.IO
import cats.implicits.*
import cats.syntax.all.*
import gameoflife.Matrix.{Cell, Matrix}  // for traverse_

object GameOfLife {
  /**
   * Run Game of Life:
   *  1. parse input
   *  2. run the simulation for the number of times as specified by [[Matrix.Generations]]
   *  3. Print out the result
   *
   * @param input the input string containing an array of number pairs
   */
  def run(input: String): IO[Unit] = {
    for {
      // initialise matrix
      initMatrix <- IO.fromEither(Input.parseInput(input))
      // Run the game simulation
      results = simulate(initMatrix, Matrix.MatrixSize, Matrix.Generations)
      _ <- printResults(results)
    } yield ()
  }

  /**
   * Run the game simulation on an initial matrix state
   * keep iterating and generating a new matrix of live cells based on the previous generation
   *
   * @param initMatrix  initial state of the matrix
   * @param matrixSize  the size of the square matrix
   * @param generations the number of iterations to run before producing the result
   */
  def simulate(initMatrix: Matrix, matrixSize: Int, generations: Int): List[Matrix] = {
    LazyList.iterate(initMatrix)(nextStateForMatrix(_, matrixSize))
      // ignore the initial state in the result set
      .slice(1, generations + 1)
      // filter out empty states
      .filter(_.nonEmpty)
      .toList
  }

  /**
   * Generate next state for matrix
   *
   * When going through the matrix, only go through live cells and their neighbours
   * as opposed to going through the whole matrix to be efficient.
   * This is particularly efficient for sparse matrix
   *
   * @param matrix     the state of current matrix
   * @param matrixSize the size of the square matrix
   * @return the new Matrix state
   */
  def nextStateForMatrix(matrix: Matrix, matrixSize: Int): Matrix = {
    // make sure cells retrieved are within bounds of matrix/grid
    val withinBoundsCondition: Cell => Boolean =
      (x, y) => x >= 0 && x < matrixSize && y >= 0 && y < matrixSize

    val relevantCells: Set[Cell] = matrix.flatMap(
      Matrix.getNeighbourCells(_, withinBoundsCondition)
    ) ++ matrix

    // Calculate the next state for the relevant cells, while keeping only live Cells
    relevantCells.filter(nextStateForCell(_, matrix))
  }

  /**
   * Calculate next state for given cell coordinates
   *
   * @param cell the cell in question
   * @param matrix the current state of the matrix (live cells)
   * @return whether the next state of the cell in question is live or not
   */
  def nextStateForCell(cell: Cell, matrix: Matrix): Boolean = {
    val liveNeighbours = Matrix.getNeighbourCells(cell, matrix.contains)

    val alive = matrix.contains(cell)
    (alive, liveNeighbours.size) match {
      case (true, 2 | 3) => true
      case (false, 3) => true
      case _ => false
    }
  }

  /**
   * format all generation of matrix and print them out
   *
   * @param states a list of all generations of matrix states
   */
  private def printResults(states: List[Matrix]): IO[Unit] = {
    val formatted = Matrix.formatMatrix(states)
    formatted.traverse_(IO.println)
  }
}
