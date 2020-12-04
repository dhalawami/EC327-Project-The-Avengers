package com.example.game0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying;      // keeps track whether game is being played or not
    private int screenX, screenY, score = 0;
    public static boolean isGameOver = false;
    private Background background1, background2;
    private Paint paint;
    private Ball ball;
    private Hole[] holes;
    public Random random;
    private double deltaSpeed = 0.3; // increases speed of ball with time
    public static double speed = 10;
    private GameActivity activity;
    public static float screenRatioX, screenRatioY;   // to make sure it is compatible with different screen sizes
    private GameOverTxt gameOverTxt;
    private Button StartAgain;
    public static int count = 0;
    private SharedPreferences prefs;



    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 2220 / screenX;
        screenRatioY = 1080 / screenY;

        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        ball = new Ball(screenX, getResources());

        background2.y = screenY;

        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        holes = new Hole[2];

        for(int i = 0; i < 2; i++){
            Hole hole = new Hole(getResources(), speed);
            holes[i] = hole;
        }

        random = new Random();

        // Initialize game panels
        gameOverTxt = new GameOverTxt(getContext());
        isGameOver = false;
        /*
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("GameOverString", false);
        if (isGameOver) {
            editor.putBoolean("GameOverString", true);
        }
        editor.commit();
        */

    }

    @Override
    public void run() {

        while (isPlaying) {
            update ();
            draw ();
            sleep();
        }

    }

    private void update () {
        // moving the background by 10 px in the y direction
        //background1.y -= 10 * screenRatioY;
        //background2.y -= 10 * screenRatioY;




        count++;
        if (count == 150 && speed < 25) {
            speed*= (1 + deltaSpeed);
            count = 0;
        }

        background1.y -= speed;
        background2.y -= speed;


        //checks if background is completely off the screen
        if (background1.y + background1.background.getHeight() < 0){
            background1.y = screenY;
        }

        if (background2.y + background2.background.getHeight() < 0){
            background2.y = screenY;
        }

        // checks if motion of ball is implemented
        if (ball.isGoingRight) {
            ball.x += 30;
            // ball.x += 30 * screenRatioX;

        }
        if (ball.isGoingLeft) {
            ball.x -= 30;
            // ball.x -= 30 * screenRatioX;
        }

        // ensures ball is always inside the screen view
        if (ball.x < 0)
            ball.x = 0;
        if (ball.x > screenX - ball.width)
            ball.x = screenX - ball.width;



        for(Hole hole : holes) {
           /*
            if (count == 150 && speed < 250) {
                hole.speed*= 1.02;
                count = 0;
            }
            */

            hole.y -= hole.speed;

            // check if off screen
            if (hole.y + hole.height < 0) {
                hole.y = screenY;
                score++;        // score changes

                // ensures visibility within screen window
                hole.x = random.nextInt(screenX - hole.width);
            }

            // check if ball hits hole
            if (Rect.intersects(hole.getCollisionShape(), ball.getCollisionShape())) {
                isGameOver = true;
                //startActivity(new Intent(GameView.this, GameOver.class));
                //SharedPreferences sharedPref = GameActivity.getPreferences(Context.MODE_PRIVATE);
                //SharedPreferences.Editor editor = sharedPreferences.edit();

                return;
            }
        }


    }

    private void draw () {

        if (getHolder().getSurface().isValid()){
        // ensures the canvas was properly initiated

            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);

            // drawing holes
            for (Hole hole : holes) {
                canvas.drawBitmap(hole.getHole(), hole.x, hole.y, paint);
            }

            canvas.drawText((score-1) + "", screenX/2, screenY- 164, paint);
            // drawing ball
            canvas.drawBitmap(ball.getBall(), ball.x, ball.y, paint);

            // checking for game over
            if (isGameOver) {
                //gameOverTxt.draw(canvas);
                isPlaying = false; //breaks thread
                waitBeforeExiting();
                saveIfHighScore();
                setCurrentScore();
                getHolder().unlockCanvasAndPost(canvas);
                return;
                //GameActivity.LaunchGameOver();
                //gameOverTxt.generate("Play Again", getContext());
                //return;
            }

/*
            StartAgain = (Button) findViewById(R.id.play_again);
            StartAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isGameOver = true;
                }
            });
*/
            getHolder().unlockCanvasAndPost(canvas);

        }
    }

    private void saveIfHighScore() {
        if(prefs.getInt("highscore", 0) < score) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highscore", score);
            editor.apply();
        }
    }

    private void setCurrentScore() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("currentScore", score);
        editor.apply();
    }

    private void waitBeforeExiting() {
        try {
            Thread.sleep(500);
            activity.startActivity(new Intent(activity, GameOver.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume () {
        // called when game is started or resumed
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
        /*
        if(isGameOver) {
            GameActivity.setisGameOver(true);
        }

         */

    }

    private void sleep () {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void pause() {

        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    // Change this to be compatible with gyroscope input
    // check AXIS_TILT (https://developer.android.com/reference/android/view/MotionEvent#AXIS_TILT)

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(event.getX() < screenX /2){
                    ball.isGoingLeft = true;
                }
                if(event.getX() > screenX /2){
                    ball.isGoingRight = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                ball.isGoingLeft = false;
                ball.isGoingRight = false;
                break;
        }

        return true;

    }

    /*
    public static boolean LastTry() {
        return isGameOver;
    }

     */

}
