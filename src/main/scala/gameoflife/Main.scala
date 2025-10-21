package gameoflife

import cats.effect.{ExitCode, IO, IOApp}
import gameoflife.GameOfLife

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    args match
    case input :: Nil =>
      (for {
        cells <- IO.fromEither(Input.parseInput(input))
        matrix = GameOfLife.initMatrix(cells)
        _ <- GameOfLife.runGame(matrix, MatrixSize, Generations)
      } yield ExitCode.Success)
        .handleErrorWith { e =>
          IO.println(s"Error: ${e.getMessage}").as(ExitCode.Error)
        }
    case _ =>
      IO.println("Usage: sbt run -- <input string>").as(ExitCode.Error)
  }
}
