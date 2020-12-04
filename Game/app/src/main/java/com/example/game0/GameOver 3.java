package com.example.game0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.view.SurfaceView;
import android.graphics.Canvas;
import android.widget.TextView;


public class GameOver extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game_over);

        findViewById(R.id.play_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(GameOver.this, GameActivity.class));
            }
        });

        TextView highScoreTxt = findViewById(R.id.highScoreTxt);

        SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highScoreTxt.setText("HighScore: "+(prefs.getInt("highscore", 0)));

        TextView currentScoreTxt = findViewById(R.id.currentScoreTxt);
        currentScoreTxt.setText("Score: "+(prefs.getInt("currentScore", 0)));

    }
}