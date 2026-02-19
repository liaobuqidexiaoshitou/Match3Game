package com.example.match3game;

/**
 * 宝石类
 * 表示游戏中的单个宝石
 */
public class Gem {
    public int color;
    public int row;
    public int col;
    public boolean isSelected;
    public boolean isMatched;

    public Gem(int color, int row, int col) {
        this.color = color;
        this.row = row;
        this.col = col;
        this.isSelected = false;
        this.isMatched = false;
    }
}
