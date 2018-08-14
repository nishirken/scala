package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

	test("contains works with negative set") {
		assert(contains(x => x < 0, -100))
		assert(!contains(x => x < 0, 100))
	}

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
		val s1s2 = union(s1, s2)
		val s1s3 = union(s1, s3)
		val allUnioned = union(s1, union(s2, s3))
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

	test("singletonSet(1) does not contains 5") {
		new TestSets {
			assert(!contains(s1, 5), "Singleton")
		}
	}

  test("union contains all elements of each set") {
    new TestSets {
      val s = s1s2
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

	test("intersect contains elements all elements that are both in 1 and 2 sets") {
		new TestSets {
			val s = intersect(s1s2, s1)
			assert(contains(s, 1), "Intersection 1")
			assert(!contains(s, 2), "Intersection 2")
			assert(!contains(s, 3), "Intersection 3")
		}
	}

	test("diff between two sets") {
		new TestSets {
			val s = diff(s1s2, s1)
			assert(!contains(s, 1), "Diff 1")
			assert(contains(s, 2), "Diff 2")
			assert(!contains(s, 3), "Diff 3")

			val t = diff(s1s2, s1s3)
			assert(contains(t, 2), "Diff 4")
			assert(contains(t, 3), "Diff 5")
			assert(!contains(t, 1), "Diff 6")
		}
	}

	test("filter set") {
		new TestSets {
			val s = filter(allUnioned, x => x < 3)
			assert(contains(s, 1), "Filter 1")
			assert(contains(s, 2), "Filter 2")
			assert(!contains(s, 3), "Filter 3")

			val t = filter(allUnioned, x => x > 3)
			assert(!contains(t, 1), "Filter 4")
			assert(!contains(t, 2), "Filter 5")
			assert(!contains(t, 3), "Filter 6")

			val k = filter(allUnioned, x => x === 3)
			assert(!contains(k, 1), "Filter 7")
			assert(!contains(k, 2), "Filter 8")
			assert(contains(k, 3), "Filter 9")

			val r = filter(allUnioned, x => x > 0)
			assert(contains(r, 1), "Filter 10")
			assert(contains(r, 2), "Filter 11")
			assert(contains(r, 3), "Filter 12")
		}
	}

	test("forall in set") {
		new TestSets {
			assert(forall(s1s2, x => x < 3), "Forall 1")
			assert(!forall(s3, x => x < 3), "Forall 2")
			assert(!forall(s1s2, x => x > 3), "Forall 3")
			assert(!forall(s3, x => x > 3), "Forall 4")
		}
	}

	test("exists in set") {
		new TestSets {
			assert(exists(s1s2, x => x == 2), "Exists 1")
			assert(!exists(s1s2, x => x == 3), "Exists 2")
			assert(exists(s1s2, x => x < 3), "Exists 3")
			assert(!exists(s1s2, x => x > 3), "Exists 4")
		}
	}

	test("map set") {
		new TestSets {
			val squareSet = map(s2, x => x * x)
			val sevenSet = map(allUnioned, _ => 7)
			assert(forall(squareSet, _ == 4), "Map 1")
			assert(!contains(squareSet, 2), "Map 2")
			assert(forall(sevenSet, _ == 7), "Map 3")
		}
	}
}
