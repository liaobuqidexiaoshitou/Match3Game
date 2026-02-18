package com.example.match3game

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * 游戏界面 Activity
 * 显示游戏棋盘、分数和步数
 */
class GameActivity : AppCompatActivity() {

    private lateinit var boardView: BoardView
    private lateinit var scoreText: TextView
    private lateinit var movesText: TextView
    private lateinit var resetButton: Button
    private lateinit var adManager: AdManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // 初始化广告管理器
        adManager = AdManager(this)

        // 初始化视图
        boardView = findViewById(R.id.boardView)
        scoreText = findViewById(R.id.scoreText)
        movesText = findViewById(R.id.movesText)
        resetButton = findViewById(R.id.resetButton)

        // 加载横幅广告
        // TODO: 替换为真实的广告位 ID
        val bannerAd = findViewById<com.google.android.gms.ads.AdView>(R.id.bannerAd)
        adManager.loadBannerAd(bannerAd)

        // 初始化插屏广告
        adManager.showAdOnGameStart()

        // 更新分数显示
        updateScore()

        // 重置按钮
        resetButton.setOnClickListener {
            // 显示确认对话框（可选）
            // TODO: 添加确认对话框
            resetGame()
        }
    }

    /**
     * 重置游戏
     */
    private fun resetGame() {
        boardView.resetGame()
        updateScore()
        updateMoves()
        adManager.showAdOnInterval(boardView.getMoves(), 10)
    }

    /**
     * 更新分数显示
     */
    private fun updateScore() {
        val score = boardView.getScore()
        scoreText.text = "分数: $score"
    }

    /**
     * 更新步数显示
     */
    private fun updateMoves() {
        val moves = boardView.getMoves()
        movesText.text = "步数: $moves"
    }

    override fun onResume() {
        super.onResume()
        // 每次回到游戏界面时，加载新的插屏广告
        adManager.loadInterstitialAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        // 游戏结束时显示插屏广告
        adManager.showAdOnGameOver()
    }
}
