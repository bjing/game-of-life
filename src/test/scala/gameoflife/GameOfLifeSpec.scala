package gameoflife

import org.scalatest.freespec.AnyFreeSpec

class GameOfLifeSpec extends AnyFreeSpec {
  "Game of Life" - {
    "should be able to initiate matrix with one live cell" in {
      val cells = List((1, 1))
      val matrix = GameOfLife.initMatrix(cells)
      assert(matrix == Map((1,1)->true))
    }

    "should be able to initiate matrix with multiple live cells" in {
      val cells = List(
        (5, 5), (6, 5), (7, 5), (5, 6), (6, 6), (7, 6)
      )
      val matrix = GameOfLife.initMatrix(cells)
      assert(matrix == Map(
        (5, 5) -> true,
        (6, 5) -> true,
        (7, 5) -> true,
        (5, 6) -> true,
        (6, 6) -> true,
        (7, 6) -> true)
      )
    }

    "should be able to calculate next state for cell" in {
      /*
      Initial matrix state is as follows
          x x y x
          x y y x
          x x x x
       */
      val matrix = Map((0, 2) -> true, (1, 1) -> true, (1, 2) -> true)

      assert(GameOfLife.nextStateForCell((0, 1), matrix))
      assert(GameOfLife.nextStateForCell((0, 2), matrix))
      assert(GameOfLife.nextStateForCell((1, 1), matrix))
      assert(GameOfLife.nextStateForCell((1, 2), matrix))

      assert(!GameOfLife.nextStateForCell((0, 0), matrix))
      assert(!GameOfLife.nextStateForCell((0, 3), matrix))
      assert(!GameOfLife.nextStateForCell((1, 0), matrix))
      assert(!GameOfLife.nextStateForCell((1, 3), matrix))
      assert(!GameOfLife.nextStateForCell((2, 0), matrix))
      assert(!GameOfLife.nextStateForCell((2, 1), matrix))
      assert(!GameOfLife.nextStateForCell((2, 2), matrix))
      assert(!GameOfLife.nextStateForCell((2, 3), matrix))
    }

    "should get matrix state for next generation" in {
      val matrix = Map((0, 2) -> true, (1, 1) -> true, (1, 2) -> true)
      val newMatrix = GameOfLife.nextGeneration(matrix, 4)
      assert(newMatrix == matrix + ((0, 1) -> true))
    }

    "should format matrix with one state state for printing" in {
      val matrixCells = List(Set((0, 2), (1, 1), (1, 2)))
      val formatted = GameOfLife.formatMatrixStates(matrixCells)
      val expected = List("1: [[0, 2], [1, 1], [1, 2]]")
      assert(formatted == expected)
    }

    "should format matrix with multiple states for printing" in {
      val matrixCells = List(
        Set((0, 2), (1, 1), (1, 2)),
        Set((0, 1), (1, 1), (1, 2), (2, 1))
      )
      val formatted = GameOfLife.formatMatrixStates(matrixCells)
      val expected = List(
        "1: [[0, 2], [1, 1], [1, 2]]",
        "2: [[0, 1], [1, 1], [1, 2], [2, 1]]",
      )
      assert(formatted == expected)
    }
  }
}
