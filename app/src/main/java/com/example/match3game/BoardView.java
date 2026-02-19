package com.example.match3game;

import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import android.util.AttributeSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 棋盘视图
 * 自定义 View 用于绘制游戏棋盘
 */
public class BoardView extends View {
    private static final int BOARD_SIZE = 6;
    private static final int GEM_COLORS = 7;

    private GameEngine gameEngine;
    private AdManager adManager;

    private float gemSize;
    private float padding;
    private float boardPadding;

    private int[] colors = new int[]{
            0xFFFF6B6B,   // 红色
            0xFF4ECDC4,   // 青色
            0xFF45B7D1,   // 蓝色
            0xFF96CEB4,   // 绿色
            0xFFE6D08D,   // 黄色
            0xFFD4A5A5,   // 紫色
            0xFF98D8C8    // 粉色
    };

    private Gem selectedGem;
    private Point selectedGemPosition;

    public BoardView(Context context) {
        super(context);
        init(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        gameEngine = new GameEngine();
        selectedGem = null;
        selectedGemPosition = new Point();

        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    float x = event.getX();
                    float y = event.getY();
                    handleGemClick(x, y);
                }
                return true;
            }
        });

        // 初始化广告
        adManager = new AdManager((MainActivity) context);

        // 资源尺寸
        float screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        float screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        // 计算宝石大小
        float availableWidth = screenWidth - 40;
        float availableHeight = screenHeight - 200;
        gemSize = Math.min(availableWidth / BOARD_SIZE, availableHeight / BOARD_SIZE);

        padding = (screenWidth - gemSize * BOARD_SIZE) / 2;
        boardPadding = 10f;
    }

    /**
     * 处理宝石点击
     */
    private void handleGemClick(float x, float y) {
        int col = (int) ((x - padding - boardPadding) / gemSize);
        int row = (int) ((y - padding - boardPadding) / gemSize);

        if (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
            boolean success = gameEngine.handleGemClick(row, col);
            if (success) {
                invalidate();
                checkForMatches();
            }
        }
    }

    /**
     * 检查是否有匹配
     */
    private void checkForMatches() {
        List<List<Gem>> matches = gameEngine.findMatches();
        if (!matches.isEmpty()) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    gameEngine.removeMatches();
                    invalidate();

                    if (gameEngine.isGameOver()) {
                    }
                }
            }, 300);
        }
    }

    /**
     * 绘制宝石
     */
    private void drawGem(Canvas canvas, Gem gem, float x, float y) {
        if (gem == null) return;

        Paint paint = new Paint();
        paint.setColor(colors[gem.color]);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        RectF rect = new RectF(
                x + boardPadding,
                y + boardPadding,
                x + gemSize - boardPadding,
                y + gemSize - boardPadding
        );
        canvas.drawRoundRect(rect, 10f, 10f, paint);

        // 绘制边框
        if (gem.isSelected) {
            Paint strokePaint = new Paint();
            strokePaint.setColor(Color.WHITE);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setStrokeWidth(5f);
            strokePaint.setAntiAlias(true);
            canvas.drawRoundRect(rect, 10f, 10f, strokePaint);
        }

        // 绘制高光
        Paint highlightPaint = new Paint();
        highlightPaint.setColor(Color.WHITE);
        highlightPaint.setAlpha(100);
        highlightPaint.setStyle(Paint.Style.FILL);
        highlightPaint.setAntiAlias(true);
        canvas.drawRoundRect(
                rect.left + 5,
                rect.top + 5,
                rect.right - 10,
                rect.bottom - 10,
                8f,
                8f,
                highlightPaint
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.parseColor("#2C3E50"));

        RectF boardRect = new RectF(
                padding,
                padding,
                getWidth() - padding,
                getHeight() - padding
        );
        Paint boardPaint = new Paint();
        boardPaint.setColor(Color.parseColor("#34495E"));
        boardPaint.setStyle(Paint.Style.FILL);
        boardPaint.setAntiAlias(true);
        canvas.drawRoundRect(boardRect, 15f, 15f, boardPaint);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Gem gem = gameEngine.getGemAt(row, col);
                float x = padding + col * gemSize;
                float y = padding + row * gemSize;
                drawGem(canvas, gem, x, y);
            }
        }
    }

    /**
     * 重置游戏
     */
    public void resetGame() {
        gameEngine = new GameEngine();
        invalidate();
    }

    /**
     * 获取当前分数
     */
    public int getScore() {
        return gameEngine.getScore();
    }

    /**
     * 获取剩余步数
     */
    public int getMoves() {
        return gameEngine.getMoves();
    }
}
