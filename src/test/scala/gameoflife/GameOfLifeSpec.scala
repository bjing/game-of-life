package gameoflife

import org.scalatest.freespec.AnyFreeSpec

class GameOfLifeSpec extends AnyFreeSpec {
  "Game of Life" - {
    "should produce the correct number of generations" in {
      val liveCells = Set((0, 2), (1, 1), (1, 2))
      val generations = 100
      val result = GameOfLife.simulate(liveCells, 200, generations)

      assert(result.length == generations)
    }

    "should calculate the next state (live=true or dead=false) for a given cell" in {
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
      val matrix = Set((0, 2), (1, 1), (1, 2))
      val newMatrix = GameOfLife.nextStateForMatrix(matrix, 4)

      assert(newMatrix == matrix + ((0, 1)))
    }
  }
}
