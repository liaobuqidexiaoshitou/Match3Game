package com.example.match3game

/**
 * 宝石类
 * 表示游戏中的单个宝石
 */
class Gem(val color: Int, val row: Int, val col: Int) {
    var isSelected = false
    var isMatched = false
}

/**
 * 游戏引擎类
 * 处理所有游戏逻辑
 */
class GameEngine(private val boardSize: Int = 6) {

    private var board: Array<Array<Gem?>>
    private var score: Int = 0
    private var moves: Int = 0
    private var isProcessing = false

    init {
        // 初始化空棋盘
        board = Array(boardSize) { Array(boardSize) { null } }
        initializeBoard()
    }

    /**
     * 初始化游戏棋盘
     */
    private fun initializeBoard() {
        // 填充棋盘，避免初始就有匹配
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                var color: Int
                do {
                    color = (0 until 7).random() // 0-6: 7种颜色
                } while (wouldMatch(row, col, color))
                board[row][col] = Gem(color, row, col)
            }
        }
    }

    /**
     * 检查放置宝石后是否会立即匹配
     */
    private fun wouldMatch(row: Int, col: Int, color: Int): Boolean {
        // 检查水平方向
        var horizontalCount = 1
        var r = row
        while (r > 0 && board[r-1]?.color == color) {
            horizontalCount++
            r--
        }
        r = row
        while (r < boardSize - 1 && board[r+1]?.color == color) {
            horizontalCount++
            r++
        }
        if (horizontalCount >= 3) return true

        // 检查垂直方向
        var verticalCount = 1
        var c = col
        while (c > 0 && board[r][c-1]?.color == color) {
            verticalCount++
            c--
        }
        c = col
        while (c < boardSize - 1 && board[r][c+1]?.color == color) {
            verticalCount++
            c++
        }
        if (verticalCount >= 3) return true

        return false
    }

    /**
     * 获取点击位置的宝石
     */
    fun getGemAt(row: Int, col: Int): Gem? {
        return if (row >= 0 && row < boardSize && col >= 0 && col < boardSize) {
            board[row][col]
        } else null
    }

    /**
     * 处理宝石点击
     * @return 是否是有效的移动
     */
    fun handleGemClick(row: Int, col: Int): Boolean {
        if (isProcessing) return false

        val gem = getGemAt(row, col) ?: return false

        // 如果没有选中任何宝石，选中当前宝石
        if (selectedGem == null) {
            selectedGem = gem
            gem.isSelected = true
            return true
        }

        // 如果点击的是同一个宝石，取消选中
        if (selectedGem == gem) {
            gem.isSelected = false
            selectedGem = null
            return true
        }

        // 检查是否相邻
        val isAdjacent = abs(selectedGem!!.row - row) + abs(selectedGem!!.col - col) == 1

        if (isAdjacent) {
            // 交换宝石
            swapGems(selectedGem!!, gem)
            selectedGem!!.isSelected = false
            selectedGem = null

            // 检查是否有匹配
            if (findMatches().isEmpty()) {
                // 没有匹配，交换回来
                swapGems(selectedGem!!, gem)
                return false
            }

            moves++
            return true
        } else {
            // 不相邻，选中新的宝石
            selectedGem?.isSelected = false
            selectedGem = gem
            gem.isSelected = true
            return true
        }
    }

    private var selectedGem: Gem? = null

    /**
     * 交换两个宝石
     */
    private fun swapGems(gem1: Gem, gem2: Gem) {
        val temp = board[gem1.row][gem1.col]
        board[gem1.row][gem1.col] = board[gem2.row][gem2.col]
        board[gem2.row][gem2.col] = temp

        // 更新位置
        if (board[gem1.row][gem1.col] == gem1) {
            board[gem1.row][gem1.col]!!.row = gem1.row
            board[gem1.row][gem1.col]!!.col = gem1.col
        }
        if (board[gem2.row][gem2.col] == gem2) {
            board[gem2.row][gem2.col]!!.row = gem2.row
            board[gem2.row][gem2.col]!!.col = gem2.col
        }
    }

    /**
     * 查找所有匹配的宝石
     * @return 匹配的宝石列表（可能包含多个独立的匹配）
     */
    fun findMatches(): List<List<Gem>> {
        val matches = mutableListOf<List<Gem>>()

        // 检查水平匹配
        for (row in 0 until boardSize) {
            var match = mutableListOf<Gem>()
            var color = board[row][0]?.color
            if (color == null) continue

            for (col in 0 until boardSize) {
                if (board[row][col]?.color == color) {
                    match.add(board[row][col]!!)
                } else {
                    if (match.size >= 3) {
                        matches.add(match.toList())
                    }
                    match = mutableListOf()
                    color = board[row][col]?.color
                }
            }
            if (match.size >= 3) {
                matches.add(match.toList())
            }
        }

        // 检查垂直匹配
        for (col in 0 until boardSize) {
            var match = mutableListOf<Gem>()
            var color = board[0][col]?.color
            if (color == null) continue

            for (row in 0 until boardSize) {
                if (board[row][col]?.color == color) {
                    match.add(board[row][col]!!)
                } else {
                    if (match.size >= 3) {
                        matches.add(match.toList())
                    }
                    match = mutableListOf()
                    color = board[row][col]?.color
                }
            }
            if (match.size >= 3) {
                matches.add(match.toList())
            }
        }

        return matches
    }

    /**
     * 移除匹配的宝石并下落
     */
    fun removeMatches() {
        val matches = findMatches()
        isProcessing = true

        // 标记匹配的宝石
        for (match in matches) {
            for (gem in match) {
                gem.isMatched = true
            }
        }

        // 下落宝石
        for (col in 0 until boardSize) {
            var emptyRow = boardSize - 1
            for (row in boardSize - 1 downTo 0) {
                if (!board[row][col]?.isMatched!!) {
                    if (row != emptyRow) {
                        board[emptyRow][col] = board[row][col]
                        board[row][col] = null
                        board[emptyRow][col]!!.row = emptyRow
                    }
                    emptyRow--
                }
            }

            // 在顶部填充新宝石
            for (row in 0..emptyRow) {
                board[row][col] = Gem((0 until 7).random(), row, col)
            }
        }

        // 清除匹配标记
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                board[row][col]?.isMatched = false
            }
        }

        // 计算分数
        val totalMatches = matches.sumOf { it.size }
        score += totalMatches * 10

        isProcessing = false
    }

    /**
     * 获取当前分数
     */
    fun getScore(): Int {
        return score
    }

    /**
     * 获取剩余步数
     */
    fun getMoves(): Int {
        return moves
    }

    /**
     * 检查游戏是否结束
     */
    fun isGameOver(): Boolean {
        // 检查是否还有可能移动
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                // 检查右边是否可以交换
                if (col < boardSize - 1) {
                    swapGems(board[row][col]!!, board[row][col+1]!!)
                    if (findMatches().isEmpty()) {
                        swapGems(board[row][col]!!, board[row][col+1]!!)
                        return false
                    }
                    swapGems(board[row][col]!!, board[row][col+1]!!)
                }
                // 检查下边是否可以交换
                if (row < boardSize - 1) {
                    swapGems(board[row][col]!!, board[row+1][col]!!)
                    if (findMatches().isEmpty()) {
                        swapGems(board[row][col]!!, board[row+1][col]!!)
                        return false
                    }
                    swapGems(board[row][col]!!, board[row+1][col]!!)
                }
            }
        }
        return true
    }
}
