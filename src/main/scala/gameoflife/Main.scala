package gameoflife

import cats.effect.{IO, IOApp}
import gameoflife.GameOfLife

object Main extends IOApp.Simple {
  def run: IO[Unit] =
    for {
      matrix <- IO.pure(
        // TODO read initial cells from command line
        GameOfLife.initMatrix(List((1, 1)))
      )
      _ <- GameOfLife.runGame(matrix, MatrixSize, Generations)
    } yield ()
}
