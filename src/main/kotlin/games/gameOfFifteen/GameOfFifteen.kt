package games.gameOfFifteen

import board.Direction
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game = GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
	private val board = createGameBoard<Int?>(4)

	override fun initialize() {
		board.getAllCells().zip(initializer.initialPermutation) { cell, value ->
			board[cell] = value
		}
	}

	override fun canMove() = true

	override fun hasWon(): Boolean {
		return board.getAllCells()
				.asSequence()
				.map { board[it] }
				.filterNotNull()
				.zipWithNext()
				.all { (a, b) -> a < b }
	}

	override fun processMove(direction: Direction) {
		val emptyCell = board.find { it == null }!!

		with(board) {
			emptyCell.getNeighbour(direction.reversed())
		}?.let { neighbourCell ->
			board[emptyCell] = board[neighbourCell]
			board[neighbourCell] = null
		}
	}

	override fun get(i: Int, j: Int): Int? = board[board.getCell(i, j)]
}


