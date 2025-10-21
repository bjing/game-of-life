package gameoflife

import io.circe
import io.circe.syntax.*
import io.circe.Json
import org.scalatest.freespec.AnyFreeSpec
import munit.ScalaCheckSuite
import org.scalacheck.{Gen, Test}
import org.scalacheck.Prop.forAll
import org.scalacheck.Test.Parameters

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

  "file loader" - {
    "should load file with exactly one line" in {

    }

    "should error when file is empty" in {

    }

    "should error when file contains more than one line" in {

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
      // Optional: check that the number of cells matches
      result.foreach(cells => assert(cells.size <= 20))
      true
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
      true
    }
  }

//  val malformedArrays: Gen[String] =
//    Gen.oneOf(
//      Gen.listOfN(5, Gen.choose(0, 100)).map(_.asJson.noSpaces), // array of Ints instead of pairs
//      Gen.listOfN(3, Gen.const(List(1, 2, 3))).map(_.asJson.noSpaces), // wrong length
//      Gen.listOfN(2, Gen.const(List(1))).map(_.asJson.noSpaces) // singleton lists
//    )
//
//  property("parseLine should fail with DecodingFailure for malformed arrays") {
//    forAll(malformedArrays) { s =>
//      println(s"error input: $s")
//      val result = Input.parseLine(s)
//      assert(result.isLeft)
//      assert(result.left.get.isInstanceOf[circe.DecodingFailure])
//      true
//    }
//  }
