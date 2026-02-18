package com.example.match3game

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * 主界面 Activity
 * 显示游戏入口和设置
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 设置点击事件
        findViewById<View>(R.id.btn_play).setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        findViewById<View>(R.id.btn_settings).setOnClickListener {
            // TODO: 打开设置界面
        }
    }
}
