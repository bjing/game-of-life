package gameoflife

import org.scalatest.freespec.AnyFreeSpec

class GameOfLifeSpec extends AnyFreeSpec {
  "Game of Life" - {
    "should be able to calculate next state for cell" in {
      /*
      Initial matrix state is as follows
          x x y x
          x y y x
          x x x x
       */
      val liveCells = Set((0, 2), (1, 1), (1, 2))

      assert(GameOfLife.nextStateForCell((0, 1), liveCells))
      assert(GameOfLife.nextStateForCell((0, 2), liveCells))
      assert(GameOfLife.nextStateForCell((1, 1), liveCells))
      assert(GameOfLife.nextStateForCell((1, 2), liveCells))

      assert(!GameOfLife.nextStateForCell((0, 0), liveCells))
      assert(!GameOfLife.nextStateForCell((0, 3), liveCells))
      assert(!GameOfLife.nextStateForCell((1, 0), liveCells))
      assert(!GameOfLife.nextStateForCell((1, 3), liveCells))
      assert(!GameOfLife.nextStateForCell((2, 0), liveCells))
      assert(!GameOfLife.nextStateForCell((2, 1), liveCells))
      assert(!GameOfLife.nextStateForCell((2, 2), liveCells))
      assert(!GameOfLife.nextStateForCell((2, 3), liveCells))
    }

    "should get matrix state for next generation" in {
      val liveCells = Set((0, 2), (1, 1), (1, 2))
      val newLiveCells= GameOfLife.nextGeneration(liveCells, 4)
      assert(newLiveCells == liveCells + ((0, 1)))
    }

    "should format matrix with one state for printing" in {
      val matrixCells = List(Set((0, 2), (1, 1), (1, 2)))
      val formatted = GameOfLife.formatStates(matrixCells)
      val expected = List("1: [[0, 2], [1, 1], [1, 2]]")
      assert(formatted == expected)
    }

    "should format matrix with multiple states for printing" in {
      val matrixCells = List(
        Set((0, 2), (1, 1), (1, 2)),
        Set((0, 1), (1, 1), (1, 2), (2, 1))
      )
      val formatted = GameOfLife.formatStates(matrixCells)
      val expected = List(
        "1: [[0, 2], [1, 1], [1, 2]]",
        "2: [[0, 1], [1, 1], [1, 2], [2, 1]]",
      )
      assert(formatted == expected)
    }
  }
}
