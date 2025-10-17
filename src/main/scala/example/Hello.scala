package example

import cats.effect.{IO, IOApp}

object Hello extends IOApp.Simple {
  def run: IO[Unit] = {
    IO.println("Hello world!")
  }
}
