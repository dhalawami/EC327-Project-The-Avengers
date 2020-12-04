package com.example.game0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    //private Button StartAgain;
    //SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        gameView = new GameView(this, point.x, point.y);

        setContentView(gameView);
        //Button playAgain = (Button) findViewById(R.id.play_again);


        //sharedPreferences = getSharedPreferences(mySharedPreferences, MODE_PRIVATE);

        /*if (isGameOver) {
            //startActivity(new Intent(GameActivity.this, GameOver.class));
            startActivity(new Intent(GameActivity.this, MainActivity.class));

        }*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();


        /*
        if(GameView.isGameOver) {
            StartAgain = (Button) findViewById(R.id.play_again);
            StartAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(GameActivity.this, MainActivity.class));
                }
            });
        }

         */
/*
        if (isGameOver) {
            gameView.pause();
            findViewById(R.id.play_again).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(GameActivity.this, MainActivity.class));
                }
            });
        }

 */
 /*
        if (gameView.LastTry()) {
            gameView.pause();
            startActivity(new Intent(GameActivity.this, MainActivity.class));

            findViewById(R.id.play_again).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(GameActivity.this, MainActivity.class));
                }
            });


        }
*/
    }

    /*
     public static LaunchGameOver() {
        // increment elasped time for game over screen
        //elaspedTime += (System.currentTimeMillis() - startTime) / 1000;
        Intent gameOverIntent = new Intent(GameActivity.this, GameOver.class);
        startActivity(gameOverIntent);
    }

 */
/*
    protected void onGameOver() {
        if (isGameOver) {
            Intent gameOverIntent = new Intent(this, GameOver.class);
            startActivity(gameOverIntent);
        }
    }
 */
}