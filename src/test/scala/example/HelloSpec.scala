package example

import org.scalatest.freespec.AnyFreeSpec

class HelloSpec extends AnyFreeSpec {
  "Simple test" - {
    "should pass" in {
      assert(true)
    }
  }
}
