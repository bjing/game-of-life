package gameoflife

import org.scalatest.freespec.AnyFreeSpec

class MatrixSpec extends AnyFreeSpec{
  "Matrix formatter" - {
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
