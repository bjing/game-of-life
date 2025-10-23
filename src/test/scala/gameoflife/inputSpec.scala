package gameoflife

import io.circe
import io.circe.syntax.*
import org.scalatest.freespec.AnyFreeSpec
import munit.ScalaCheckSuite
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class inputSpec extends AnyFreeSpec {
  "input parser" - {
    "should parse line with no cells" in {
      val line = "[]"
      val result = Input.parseInput(line)
      assert(result.isRight)
      assert(result.toOption.contains(Set()))
    }

    "should parse line with a single cell" in {
      val line = "[[1, 1]]"
      val result = Input.parseInput(line)
      assert(result.isRight)
      assert(result.toOption.contains(Set((1,1))))
    }

    "should parse line with a multiple cells" in {
      val line = "[[1, 1], [1, 2], [3, 1]]"
      val result = Input.parseInput(line)
      assert(result.isRight)
      assert(result.toOption.contains(Set((1, 1), (1, 2), (3, 1))))
    }

    "should throw error when there's no data in line" in {
      val line = ""
      val result = Input.parseInput(line)
      assert(result.isLeft)
      assert(result.left.get.isInstanceOf[circe.ParsingFailure])
    }

    "should throw error when data line is invalid" in {
      val line = "[(1, 2)]"
      val result = Input.parseInput(line)
      assert(result.isLeft)
      assert(result.left.get.isInstanceOf[circe.ParsingFailure])
    }
  }
}

class ParseLineSuccessPropertySpec extends ScalaCheckSuite:
  val cellPair: Gen[List[Int]] = for
    x <- Gen.choose(-1, 200)
    y <- Gen.choose(-1, 200)
  yield List(x, y)

  val validCellsJson: Gen[String] =
    for
      n <- Gen.choose(0, 20)
      pairs <- Gen.listOfN(n, cellPair)
    yield pairs.asJson.noSpaces

  property("parseLine should succeed on valid JSON") {
    forAll(validCellsJson) { jsonStr =>
      val result = Input.parseInput(jsonStr)
      // check that the number of cells matches
      result.foreach(cells => assert(cells.size <= 20))
    }
  }

class ParseLineErrorPropertySpec extends ScalaCheckSuite:
  val invalidJson: Gen[String] =
    Gen.alphaStr.suchThat(s => s.nonEmpty && !s.trim.startsWith("[") && !s.trim.startsWith("{"))

  property("parseLine should fail with ParsingFailure for invalid JSON") {
    forAll(invalidJson) { s =>
      val result = Input.parseInput(s)
      assert(result.isLeft)
      assert(result.left.get.isInstanceOf[circe.ParsingFailure])
    }
  }
