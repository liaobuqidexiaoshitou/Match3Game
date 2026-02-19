package com.example.match3game;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.ads.AdView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 游戏界面 Activity
 */
public class GameActivity extends AppCompatActivity {

    private BoardView boardView;
    private TextView scoreText;
    private TextView movesText;
    private Button resetButton;
    private AdManager adManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        adManager = new AdManager(this);

        boardView = findViewById(R.id.boardView);
        scoreText = findViewById(R.id.scoreText);
        movesText = findViewById(R.id.movesText);
        resetButton = findViewById(R.id.resetButton);

        AdView bannerAd = findViewById(R.id.bannerAd);
        adManager.loadBannerAd(bannerAd);

        updateScore();
        updateMoves();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }

    private void resetGame() {
        boardView.resetGame();
        updateScore();
        updateMoves();
    }

    private void updateScore() {
        int score = boardView.getScore();
        scoreText.setText("分数: " + score);
    }

    private void updateMoves() {
        int moves = boardView.getMoves();
        movesText.setText("步数: " + moves);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adManager = null;
    }
}
