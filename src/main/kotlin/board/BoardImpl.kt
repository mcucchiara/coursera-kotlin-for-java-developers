package board

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

open class SquareBoardImpl(final override val width: Int) : SquareBoard {
	private val cells: List<List<Cell>>

	init {
		require(width > 0) { "Width must be a positive integer. Current: [$width]" }
		cells = (1..width).map { i -> (1..width).map { j -> Cell(i, j) } }
	}

	override fun getCellOrNull(i: Int, j: Int): Cell? = cells.getOrNull(i - 1)?.getOrNull(j - 1)

	override fun getCell(i: Int, j: Int): Cell {
		require(i in 1..width) { "i must be a number between 1 and $width " }
		require(j in 1..width) { "j must be a number between 1 and $width " }
		return getCellOrNull(i, j)!!
	}

	override fun getAllCells(): Collection<Cell> = cells.flatten()

	override fun getRow(i: Int, jRange: IntProgression): List<Cell> = jRange.mapNotNull { j -> getCellOrNull(i, j) }

	override fun getColumn(iRange: IntProgression, j: Int): List<Cell> = iRange.mapNotNull { i -> getCellOrNull(i, j) }

	override fun Cell.getNeighbour(direction: Direction): Cell? {
		val (i1, j1) = when (direction) {
			Direction.UP -> i - 1 to j
			Direction.DOWN -> i + 1 to j
			Direction.LEFT -> i to j - 1
			Direction.RIGHT -> i to j + 1
		}
		return getCellOrNull(i1, j1)
	}
}

fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

class GameBoardImpl<T>(override val width: Int) : GameBoard<T>, SquareBoard by SquareBoardImpl(width) {
	private val idx = hashMapOf<Cell, T?>()

	override fun get(cell: Cell): T? = idx[cell]

	override fun set(cell: Cell, value: T?) {
		idx[cell] = value
	}

	override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = idx.filterValues(predicate).keys

	override fun find(predicate: (T?) -> Boolean): Cell? = idx.filterValues(predicate).keys.firstOrNull()

	override fun any(predicate: (T?) -> Boolean): Boolean = mapCellsToValues().any(predicate)

	override fun all(predicate: (T?) -> Boolean): Boolean = mapCellsToValues().all(predicate)

	private fun mapCellsToValues() = getAllCells().map { idx[it] }
}

