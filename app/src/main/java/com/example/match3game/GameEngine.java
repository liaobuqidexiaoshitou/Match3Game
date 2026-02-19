package com.example.match3game;

import java.util.ArrayList;
import java.util.List;

/**
 * 宝石类
 * 表示游戏中的单个宝石
 */

/**
 * 游戏引擎类
 * 处理所有游戏逻辑
 */
public class GameEngine {
    private static final int BOARD_SIZE = 6;
    private Gem[][] board;
    private int score;
    private int moves;
    private boolean isProcessing;

    public GameEngine() {
        board = new Gem[BOARD_SIZE][BOARD_SIZE];
        score = 0;
        moves = 0;
        isProcessing = false;
        initializeBoard();
    }

    /**
     * 初始化游戏棋盘
     */
    private void initializeBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                int color;
                do {
                    color = (int)(Math.random() * 7);
                } while (wouldMatch(row, col, color));
                board[row][col] = new Gem(color, row, col);
            }
        }
    }

    /**
     * 检查放置宝石后是否会立即匹配
     */
    private boolean wouldMatch(int row, int col, int color) {
        // 检查水平方向
        int horizontalCount = 1;
        int r = row;
        while (r > 0 && board[r-1][col].color == color) {
            horizontalCount++;
            r--;
        }
        r = row;
        while (r < BOARD_SIZE - 1 && board[r+1][col].color == color) {
            horizontalCount++;
            r++;
        }
        if (horizontalCount >= 3) return true;

        // 检查垂直方向
        int verticalCount = 1;
        int c = col;
        while (c > 0 && board[row][c-1].color == color) {
            verticalCount++;
            c--;
        }
        c = col;
        while (c < BOARD_SIZE - 1 && board[row][c+1].color == color) {
            verticalCount++;
            c++;
        }
        if (verticalCount >= 3) return true;

        return false;
    }

    /**
     * 获取点击位置的宝石
     */
    public Gem getGemAt(int row, int col) {
        if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
            return board[row][col];
        }
        return null;
    }

    /**
     * 处理宝石点击
     */
    public boolean handleGemClick(int row, int col) {
        if (isProcessing) return false;

        Gem gem = getGemAt(row, col);
        if (gem == null) return false;

        // 如果没有选中任何宝石，选中当前宝石
        if (selectedGem == null) {
            selectedGem = gem;
            gem.isSelected = true;
            return true;
        }

        // 如果点击的是同一个宝石，取消选中
        if (selectedGem == gem) {
            gem.isSelected = false;
            selectedGem = null;
            return true;
        }

        // 检查是否相邻
        if (Math.abs(selectedGem.row - row) + Math.abs(selectedGem.col - col) == 1) {
            // 交换宝石
            swapGems(selectedGem, gem);
            selectedGem.isSelected = false;
            selectedGem = null;

            // 检查是否有匹配
            if (findMatches().isEmpty()) {
                // 没有匹配，交换回来
                swapGems(selectedGem, gem);
                return false;
            }

            moves++;
            return true;
        } else {
            // 不相邻，选中新的宝石
            selectedGem.isSelected = false;
            selectedGem = gem;
            gem.isSelected = true;
            return true;
        }
    }

    private Gem selectedGem;

    /**
     * 交换两个宝石
     */
    private void swapGems(Gem gem1, Gem gem2) {
        Gem temp = board[gem1.row][gem1.col];
        board[gem1.row][gem1.col] = board[gem2.row][gem2.col];
        board[gem2.row][gem2.col] = temp;

        // 更新位置
        if (board[gem1.row][gem1.col] == gem1) {
            board[gem1.row][gem1.col].row = gem1.row;
            board[gem1.row][gem1.col].col = gem1.col;
        }
        if (board[gem2.row][gem2.col] == gem2) {
            board[gem2.row][gem2.col].row = gem2.row;
            board[gem2.row][gem2.col].col = gem2.col;
        }
    }

    /**
     * 查找所有匹配的宝石
     */
    public List<List<Gem>> findMatches() {
        List<List<Gem>> matches = new ArrayList<>();

        // 检查水平匹配
        for (int row = 0; row < BOARD_SIZE; row++) {
            List<Gem> match = new ArrayList<>();
            int color = board[row][0].color;
            if (color == -1) continue;

            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col].color == color) {
                    match.add(board[row][col]);
                } else {
                    if (match.size() >= 3) {
                        matches.add(new ArrayList<>(match));
                    }
                    match = new ArrayList<>();
                    color = board[row][col].color;
                }
            }
            if (match.size() >= 3) {
                matches.add(new ArrayList<>(match));
            }
        }

        // 检查垂直匹配
        for (int col = 0; col < BOARD_SIZE; col++) {
            List<Gem> match = new ArrayList<>();
            int color = board[0][col].color;
            if (color == -1) continue;

            for (int row = 0; row < BOARD_SIZE; row++) {
                if (board[row][col].color == color) {
                    match.add(board[row][col]);
                } else {
                    if (match.size() >= 3) {
                        matches.add(new ArrayList<>(match));
                    }
                    match = new ArrayList<>();
                    color = board[row][col].color;
                }
            }
            if (match.size() >= 3) {
                matches.add(new ArrayList<>(match));
            }
        }

        return matches;
    }

    /**
     * 移除匹配的宝石并下落
     */
    public void removeMatches() {
        List<List<Gem>> matches = findMatches();
        isProcessing = true;

        // 标记匹配的宝石
        for (List<Gem> match : matches) {
            for (Gem gem : match) {
                gem.isMatched = true;
            }
        }

        // 下落宝石
        for (int col = 0; col < BOARD_SIZE; col++) {
            int emptyRow = BOARD_SIZE - 1;
            for (int row = BOARD_SIZE - 1; row >= 0; row--) {
                if (!board[row][col].isMatched) {
                    if (row != emptyRow) {
                        board[emptyRow][col] = board[row][col];
                        board[row][col] = null;
                        board[emptyRow][col].row = emptyRow;
                    }
                    emptyRow--;
                }
            }

            // 在顶部填充新宝石
            for (int row = 0; row <= emptyRow; row++) {
                board[row][col] = new Gem((int)(Math.random() * 7), row, col);
            }
        }

        // 清除匹配标记
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col].isMatched = false;
            }
        }

        // 计算分数
        int totalMatches = 0;
        for (List<Gem> match : matches) {
            totalMatches += match.size();
        }
        score += totalMatches * 10;

        isProcessing = false;
    }

    /**
     * 获取当前分数
     */
    public int getScore() {
        return score;
    }

    /**
     * 获取剩余步数
     */
    public int getMoves() {
        return moves;
    }

    /**
     * 检查游戏是否结束
     */
    public boolean isGameOver() {
        // 检查是否还有可能移动
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                // 检查右边是否可以交换
                if (col < BOARD_SIZE - 1) {
                    swapGems(board[row][col], board[row][col+1]);
                    if (findMatches().isEmpty()) {
                        swapGems(board[row][col], board[row][col+1]);
                        return false;
                    }
                    swapGems(board[row][col], board[row][col+1]);
                }
                // 检查下边是否可以交换
                if (row < BOARD_SIZE - 1) {
                    swapGems(board[row][col], board[row+1][col]);
                    if (findMatches().isEmpty()) {
                        swapGems(board[row][col], board[row+1][col]);
                        return false;
                    }
                    swapGems(board[row][col], board[row+1][col]);
                }
            }
        }
        return true;
    }
}
