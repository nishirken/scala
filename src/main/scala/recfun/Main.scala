package recfun

object Main {
	def main(args: Array[String]) {
		println("Pascal's Triangle")
		for (row <- 0 to 10) {
			for (col <- 0 to row)
				print(pascal(col, row) + " ")
			println()
		}
	}

	/**
	  * Exercise 1
	  */
	def pascal(coll: Int, row: Int): Int = {
		def factorial(x: Int): Int = {
			def iter(x: Int, acc: Int): Int = if (x < 2) acc else iter(x - 1, x * acc)
			iter(x, 1)
		}

		factorial(row) / (factorial(coll) * factorial(row - coll))
	}

	/**
	  * Exercise 2
	  */
	def balance(chars: List[Char]): Boolean =
		chars
			.filter(char => char == '(' || char == ')')
    		.foldRight[Int](0)((x: Char, acc: Int) =>
				if (x == ')') acc - 1
				else (if (acc >= 0) acc else acc + 1)) == 0

	/**
	  * Exercise 3
	  */
	def countChange(money: Int, coins: List[Int]): Int = {
		if (money == 0) 1
		else (if
			(money < 0 || coins.isEmpty) 0
			else countChange(money, coins.tail) + countChange(money - coins.head, coins))
	}
}
