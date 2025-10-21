package gameoflife

import cats.effect.{IO, IOApp}
import gameoflife.GameOfLife

object Main extends IOApp.Simple {
  def run: IO[Unit] =
    for {
      matrix <- IO.pure(
        // TODO read initial cells from command line
//        GameOfLife.initMatrix(List((1, 1)))
        GameOfLife.initMatrix(List((5, 5), (6, 5), (7, 5), (5, 6), (6, 6), (7, 6)))

      )
      _ <- GameOfLife.runGame(matrix, MatrixSize, Generations)
    } yield ()
}
