package gameoflife

import cats.effect.{IO, Resource}
import gameoflife.Matrix.{Cell, Matrix}
import io.circe

import scala.io.Source
import io.circe.parser.*

object Input {
  /**
   * Since input is already Json formatted, we use a Json parser to do the job
   *
   * @param input the input string that contains an array of number pairs
   * @return either the parsed result or parsing error
   */
  def parseInput(input: String): Either[circe.Error, Set[Cell]] = {
    for
      json <- parse(input)
      list <- json.as[List[List[Int]]]
    yield list.map {
      case List(x, y) => (x, y)
    }.toSet
  }

  ////////////////////////////////////////////////////////////////////////////////
  // The following functions are not part of the requirements, but I wrote them
  // in an attempt to make the interviewer's job easier. However, they didn't end up
  // being used */
  /////////////////////////////////////////////////////////////////////////////////

  /**
   * Load and parse initial live cell data from input fle.
   * We make sure there's only one line in the input file.
   *
   * Note: this is not part of the requirements. I made this to make the interviewer's job easier
   *
   * @param path the nput file path
   * @return parsed matrix representation
   */
  def loadInputFromFile(path: String): IO[Matrix] =
    loadFile(path).use { src =>
      IO(src.getLines().toList).flatMap {
        case List(line) => IO.fromEither(parseInput(line))
        case Nil => IO.raiseError(new RuntimeException(s"File '$path' is empty"))
        case _ => IO.raiseError(new RuntimeException(s"File $path contains more than one line"))
      }
    }

  /**
   * Purely functional helper function to access the file
   *
   * @param path the input file path
   * @return a Resource handle for us to access the data easily
   */
  private def loadFile(path: String): Resource[IO, Source] =
    Resource.make(IO(Source.fromFile(path)))(src => IO(src.close()))
}