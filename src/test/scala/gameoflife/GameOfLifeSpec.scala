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

    }
  }
}
