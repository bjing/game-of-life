package gameoflife

import cats.effect.{IO, Resource}
import gameoflife.types.Cell
import io.circe

import scala.io.Source
import io.circe.parser.*

object Input {
  /*
    Since input is already Json formatted, we use a Json parser to do the job
  */
  def parseInput(input: String): Either[circe.Error, Set[Cell]] = {
    for
      json <- parse(input)
      list <- json.as[List[List[Int]]]
    yield list.map {
      case List(x, y) => (x, y)
    }.toSet
  }

  /*
    Load and parse initial live cell data from input fle.

    We make sure there's only one line in the input file.
   */
  def loadInputFromFile(path: String): IO[Set[Cell]] =
    loadFile(path).use { src =>
      IO(src.getLines().toList).flatMap {
        case List(line) => IO.fromEither(parseInput(line))
        case Nil => IO.raiseError(new RuntimeException(s"File '$path' is empty"))
        case _ => IO.raiseError(new RuntimeException(s"File $path contains more than one line"))
      }
    }

  /*
    Purely functional helper function to access the file
   */
  private def loadFile(path: String): Resource[IO, Source] =
    Resource.make(IO(Source.fromFile(path)))(src => IO(src.close()))
}