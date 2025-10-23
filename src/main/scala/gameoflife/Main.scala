package gameoflife

import cats.effect.{ExitCode, IO, IOApp}
import gameoflife.GameOfLife

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] = {
    args match
      case input :: Nil =>
        (for {
          _ <- GameOfLife.run(input)
        } yield ExitCode.Success)
          // catch and handle error from the above IO sequence
          .handleErrorWith { e =>
            IO.println(s"Error: ${e.getMessage}").as(ExitCode.Error)
          }
      case _ =>
        IO.println("Usage: sbt run -- <input string>").as(ExitCode.Error)
  }
}
