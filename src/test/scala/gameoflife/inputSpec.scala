package gameoflife

import io.circe
import io.circe.syntax._
import io.circe.Json

import org.scalatest.freespec.AnyFreeSpec

class inputSpec extends AnyFreeSpec {
  "input parser" - {
    "should parse line with no cells" in {
      val line = "[]"
      val result = Input.parseLine(line)
      assert(result.isRight)
      assert(result.toOption.contains(Set()))
    }

    "should parse line with a single cell" in {
      val line = "[[1, 1]]"
      val result = Input.parseLine(line)
      assert(result.isRight)
      assert(result.toOption.contains(Set((1,1))))
    }

    "should parse line with a multiple cells" in {
      val line = "[[1, 1], [1, 2], [3, 1]]"
      val result = Input.parseLine(line)
      assert(result.isRight)
      assert(result.toOption.contains(Set((1, 1), (1, 2), (3, 1))))
    }

    "should throw error when there's no data in line" in {
      val line = ""
      val result = Input.parseLine(line)
      assert(result.isLeft)
      assert(result.left.get.isInstanceOf[circe.ParsingFailure])
    }

    "should throw error when data line is invalid" in {
      val line = "[(1, 2)]"
      val result = Input.parseLine(line)
      assert(result.isLeft)
      assert(result.left.get.isInstanceOf[circe.ParsingFailure])
    }
  }
}



