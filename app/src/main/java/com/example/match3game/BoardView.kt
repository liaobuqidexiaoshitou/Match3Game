package com.example.match3game

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

/**
 * 棋盘视图
 * 自定义 View 用于绘制游戏棋盘
 */
class BoardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val gameEngine = GameEngine()
    private val adManager = AdManager(context as MainActivity) // TODO: 传入正确的 Activity

    private val gemSize: Float
    private val padding: Float
    private val boardPadding: Float
    private val colors = listOf(
        Color.parseColor("#FF6B6B"),   // 红色
        Color.parseColor("#4ECDC4"),   // 青色
        Color.parseColor("#45B7D1"),   // 蓝色
        Color.parseColor("#96CEB4"),   // 绿色
        Color.parseColor("#FFEAA7"),   // 黄色
        Color.parseColor("#DDA0DD"),   // 紫色
        Color.parseColor("#98D8C8")    // 粉色
    )

    private val selectedGem: Gem? = null // 临时存储选中的宝石
    private val selectedGemPosition: Point = Point()

    init {
        // 设置点击监听
        setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x = event.x
                    val y = event.y
                    handleGemClick(x, y)
                }
            }
            true
        }

        // 初始化广告
        adManager.initialize()

        // 资源尺寸
        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        // 计算宝石大小（留出边距）
        val availableWidth = screenWidth - 40
        val availableHeight = screenHeight - 200
        gemSize = minOf(availableWidth / 6, availableHeight / 6)

        padding = (screenWidth - gemSize * 6) / 2
        boardPadding = 10f
    }

    /**
     * 处理宝石点击
     */
    private fun handleGemClick(x: Float, y: Float) {
        // 计算点击的行列
        val col = ((x - padding - boardPadding) / gemSize).toInt()
        val row = ((y - padding - boardPadding) / gemSize).toInt()

        if (row >= 0 && row < 6 && col >= 0 && col < 6) {
            val success = gameEngine.handleGemClick(row, col)
            if (success) {
                invalidate() // 重绘棋盘
                checkForMatches()
            }
        }
    }

    /**
     * 检查是否有匹配
     */
    private fun checkForMatches() {
        val matches = gameEngine.findMatches()
        if (matches.isNotEmpty()) {
            // 延迟执行，让动画播放
            postDelayed({
                gameEngine.removeMatches()
                invalidate()

                // 更新分数
                val score = gameEngine.getScore()
                // TODO: 更新 UI 显示分数

                // 检查游戏是否结束
                if (gameEngine.isGameOver()) {
                    // 游戏结束，显示插屏广告
                    adManager.showAdOnGameOver()
                }
            }, 300)
        }
    }

    /**
     * 绘制宝石
     */
    private fun drawGem(canvas: Canvas, gem: Gem?, x: Float, y: Float) {
        if (gem == null) return

        val paint = Paint().apply {
            color = colors[gem.color]
            style = Paint.Style.FILL
            isAntiAlias = true
        }

        // 绘制圆角矩形
        val rect = RectF(
            x + boardPadding,
            y + boardPadding,
            x + gemSize - boardPadding,
            y + gemSize - boardPadding
        )
        canvas.drawRoundRect(rect, 10f, 10f, paint)

        // 如果被选中，绘制边框
        if (gem.isSelected) {
            val strokePaint = Paint().apply {
                color = Color.WHITE
                style = Paint.Style.STROKE
                strokeWidth = 5f
                isAntiAlias = true
            }
            canvas.drawRoundRect(rect, 10f, 10f, strokePaint)
        }

        // 绘制高光效果
        val highlightPaint = Paint().apply {
            color = Color.parseColor("#FFFFFF")
            alpha = 100
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawRoundRect(
            rect.left + 5,
            rect.top + 5,
            rect.right - 10,
            rect.bottom - 10,
            8f,
            8f,
            highlightPaint
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 清空画布
        canvas.drawColor(Color.parseColor("#2C3E50"))

        // 绘制棋盘背景
        val boardRect = RectF(
            padding,
            padding,
            width - padding,
            height - padding
        )
        val boardPaint = Paint().apply {
            color = Color.parseColor("#34495E")
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawRoundRect(boardRect, 15f, 15f, boardPaint)

        // 绘制每个宝石
        for (row in 0 until 6) {
            for (col in 0 until 6) {
                val gem = gameEngine.getGemAt(row, col)
                val x = padding + col * gemSize
                val y = padding + row * gemSize
                drawGem(canvas, gem, x, y)
            }
        }
    }

    /**
     * 重置游戏
     */
    fun resetGame() {
        gameEngine = GameEngine()
        invalidate()
    }

    /**
     * 获取当前分数
     */
    fun getScore(): Int {
        return gameEngine.getScore()
    }

    /**
     * 获取剩余步数
     */
    fun getMoves(): Int {
        return gameEngine.getMoves()
    }
}
