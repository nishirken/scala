package patmat

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import patmat.Huffman._

@RunWith(classOf[JUnitRunner])
class HuffmanSuite extends FunSuite {
	trait TestTrees {
		val t1 = Fork(Leaf('a',2), Leaf('b',3), List('a','b'), 5)
		val t2 = Fork(Leaf('d', 3), Fork(Leaf('b', 1), Leaf('a', 1), List('a', 'b'), 2), List('d', 'a', 'b'), 5)
	}


  test("weight of a larger tree") {
    new TestTrees {
      assert(weight(t1) === 5)
    }
  }


  test("chars of a larger tree") {
    new TestTrees {
      assert(chars(t2) === List('d','b','a'))
    }
  }

	test("times") {
		new TestTrees {
			assert(times(List('a', 'c', 'b', ' ', 'a', 'c', ' ', 'c', 'c')) === List((' ', 2), ('a', 2), ('b', 1), ('c', 4)))
		}
	}

  test("string2chars(\"hello, world\")") {
    assert(string2Chars("hello, world") === List('h', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd'))
  }


  test("makeOrderedLeafList for some frequency table") {
    assert(makeOrderedLeafList(List(('t', 2), ('e', 1), ('x', 3))) === List(Leaf('e',1), Leaf('t',2), Leaf('x',3)))
  }


  test("combine of some leaf list") {
    val leaflist = List(Leaf('e', 1), Leaf('t', 2), Leaf('x', 4))
    assert(combine(leaflist) === List(Fork(Leaf('e',1),Leaf('t',2),List('e', 't'),3), Leaf('x',4)))
  }

	test("until") {
    // test str: go go gophers
		val trees = List(Leaf('g', 3), Leaf('o', 3), Leaf(' ', 2), Leaf('h', 1), Leaf('p', 1), Leaf('r', 1), Leaf('s', 1))
    val expected = List(Fork(
      Fork(
        Fork(Leaf('g', 3), Leaf('o', 3), List('g', 'o'), 6),
        Fork(Leaf(' ', 2), Leaf('h', 1), List(' ', 'h'), 3),
        List('g', 'o', ' ', 'h'),
        9
      ),
      Fork(
        Fork(Leaf('p', 1), Leaf('r', 1), List('p', 'r'), 2),
        Leaf('s', 1),
        List('p', 'r', 's'),
        3
      ),
      List('g', 'o', ' ', 'h', 'p', 'r', 's'),
      12
    ))
		assert(until(trees) === expected)
	}

  test("decode short test") {
    new TestTrees {
      assert(decode(t1, List(0, 1)) === List('a', 'b'))
      assert(decode(t1, List(1, 0)) === List('b', 'a'))
    }
  }

  test("decode long test") {
    new TestTrees {
      assert(decode(t2, List(0, 1, 0, 1, 1, 0, 0)) === List('d', 'b', 'a', 'd', 'd')) // dbadd
    }
  }

	test("encode short text") {
		new TestTrees {
			assert(encode(t1)(List('a', 'b')) === List(0, 1))
			assert(encode(t1)(List('b', 'a')) === List(1, 0))
		}
	}

	test("encode long text") {
		new TestTrees {
			assert(encode(t2)(List('d', 'b', 'a', 'd', 'd')) === List(0, 1, 0, 1, 1, 0, 0))
		}
	}

	test("decode and encode a very short text should be identity") {
		new TestTrees {
			assert(decode(t1, encode(t1)("ab".toList)) === "ab".toList)
		}
	}

	test("decode and encode long text should be identity") {
		new TestTrees {
			assert(decode(frenchCode, encode(frenchCode)("shouldbeidentity".toList)) === "shouldbeidentity".toList)
		}
	}

	test("convert with short text") {
		new TestTrees {
			assert(convert(t1) === List(('a', List(0)), ('b', List(1))))
		}
	}

	test("convert with long text") {
		new TestTrees {
			assert(convert(t2) === List(('d', List(0)), ('b', List(1, 0)), ('a', List(1, 1))))
		}
	}

	test("quickEncode short text") {
		new TestTrees {
			assert(quickEncode(t1)(List('a', 'b')) === List(0, 1))
			assert(quickEncode(t1)(List('b', 'a')) === List(1, 0))
		}
	}

	test("quickEncode long text") {
		new TestTrees {
			assert(quickEncode(t2)(List('d', 'b', 'a', 'd', 'd')) === List(0, 1, 0, 1, 1, 0, 0))
		}
	}

//	test("decode and quickEncode a very short text should be identity") {
//		new TestTrees {
//			assert(decode(t1, quickEncode(t1)("ab".toList)) === "ab".toList)
//		}
//	}
//
//	test("decode and quickEncode long text should be identity") {
//		new TestTrees {
//			assert(decode(frenchCode, quickEncode(frenchCode)("shouldbeidentity".toList)) === "shouldbeidentity".toList)
//		}
//	}
}
