package gameoflife

import gameoflife.Matrix.Cell
import org.scalatest.freespec.AnyFreeSpec

class MatrixSpec extends AnyFreeSpec{
  "get neighbouring cells" - {
    "should get neighbour cells withOUT satisfying a condition" in {
      val result = Matrix.getNeighbourCells((0,0), _ => true)
      assert(result == Set(
        (-1, -1),
        (0, -1),
        (1, -1),
        (-1, 0),
        (1, 0),
        (-1, 1),
        (0, 1),
        (1, 1)
      ))
    }

    "should get the cells that stay within bound of the grid" in {
      val matrixSize = 200
      val withinBoundCondition: Cell => Boolean =
        (x, y) => x >= 0 && x < matrixSize && y >= 0 && y < matrixSize
      val result = Matrix.getNeighbourCells((0, 0), withinBoundCondition)

      assert(result == Set(
        (1, 0),
        (0, 1),
        (1, 1)
      ))
    }

    "should not get any neighbour cells for a 1-cell grid while staying in bound" in {
      val matrixSize = 1
      val condition: Cell => Boolean =
        (x, y) => x >= 0 && x < matrixSize && y >= 0 && y < matrixSize
      val result = Matrix.getNeighbourCells((0, 0), condition)

      assert(result == Set())
    }
  }

  "formatter" - {
    "should format matrix with one state for printing" in {
      val matrixCells = List(Set((0, 2), (1, 1), (1, 2)))
      val formatted = Matrix.formatMatrix(matrixCells)
      val expected = List("1: [[0, 2], [1, 1], [1, 2]]")
      assert(formatted == expected)
    }

    "should format matrix with multiple states for printing" in {
      val matrixCells = List(
        Set((0, 2), (1, 1), (1, 2)),
        Set((0, 1), (1, 1), (1, 2), (2, 1))
      )
      val formatted = Matrix.formatMatrix(matrixCells)
      val expected = List(
        "1: [[0, 2], [1, 1], [1, 2]]",
        "2: [[0, 1], [1, 1], [1, 2], [2, 1]]",
      )
      assert(formatted == expected)
    }
  }
}
