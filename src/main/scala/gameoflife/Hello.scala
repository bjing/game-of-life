package gameoflife

import cats.effect.{IO, IOApp}

object GameOfLife extends IOApp.Simple {
  def run: IO[Unit] = {
    IO.println("Hello world!")
  }
}
