package com.example.game0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
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
    public static double speed;
    private GameActivity activity;
    private SoundPool soundPool;
    public static float screenRatioX, screenRatioY;   // to make sure it is compatible with different screen sizes
    private GameOverTxt gameOverTxt;
    private Button StartAgain;
    private int sound;
    public static int count = 0;
    private SharedPreferences prefs;
    private int HoleRow = 0;
    private int HoleNum = 5;
    private int deltaY = 500;


    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);

        this.activity = activity;
        prefs = activity.getSharedPreferences("game", Context.MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        sound = soundPool.load(activity,R.raw.gameover, 1);

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

        holes = new Hole[HoleNum];

        for(int i = 0; i < HoleNum; i++){
            //Hole hole = new Hole(getResources(), speed);
            //holes[i] = hole;
            Hole hole = new Hole(getResources(), speed, i * deltaY);
            holes[i] = hole;

//////////////////
            /*
            if (i == 0) {
                Hole hole = new Hole(getResources(), speed, 0 * deltaY);
                holes[i] = hole;
            } else if (i == 1){
                Hole hole = new Hole(getResources(), speed, 1 * deltaY);
                holes[i] = hole;
            } else if (i == 2){
                Hole hole = new Hole(getResources(), speed, 2 * deltaY);
                holes[i] = hole;
            } else if (i == 3){
                Hole hole = new Hole(getResources(), speed, 3* deltaY);
                holes[i] = hole;
            }

             */
///////////

        }

        //////////////////////////

        //////////////////////////

        random = new Random();
        //speed = 10;
        // Initialize game panels
        gameOverTxt = new GameOverTxt(getContext());
        isGameOver = false;
        speed = 12;
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



        score++;
        count++;
        if (count == 150 && speed < 25) {
            speed*= (1 + deltaSpeed);
            count = 0;
        }

        background1.y -= speed;
        background2.y -= speed;

        for(Hole hole : holes) {
            hole.speed = speed;
        }

        //checks if background is completely off the screen
        if (background1.y + background1.background.getHeight() < 0){
            background1.y = screenY;
        }

        if (background2.y + background2.background.getHeight() < 0){
            background2.y = screenY;
        }

/*
        // checks if motion of ball is implemented
        if (ball.isGoingRight) {
            ball.x += 30;
            // ball.x += 30 * screenRatioX;

        }
        if (ball.isGoingLeft) {
            ball.x -= 30;
            // ball.x -= 30 * screenRatioX;
        }
*/
        ball.updateSpeed(activity.roll);
        ball.x += ball.speedX;

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
                //score++;        // score changes

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
/*
                if (HoleRow == 0) {
                    HoleRow++;
                    canvas.drawBitmap(hole.getHole(), hole.x, hole.y, paint);

                } else if (HoleRow == 1){
                    hole.y = hole.y - (2 * hole.height);
                    canvas.drawBitmap(hole.getHole(), hole.x, hole.y, paint);

                    HoleRow++;
                } else if (HoleRow == 2){
                    hole.y = hole.y - (4 * hole.height);
                    canvas.drawBitmap(hole.getHole(), hole.x, hole.y, paint);

                    HoleRow++;
                } else if (HoleRow == 3){
                    hole.y = hole.y - (6 * hole.height);
                    canvas.drawBitmap(hole.getHole(), hole.x, hole.y, paint);

                    HoleRow=0;
                }
*/
            }

            canvas.drawText((score/10) + "", screenX/2 - 50, screenY- 164, paint);
            // drawing ball
            canvas.drawBitmap(ball.getBall(), ball.x, ball.y, paint);

            // checking for game over
            if (isGameOver) {
                //gameOverTxt.draw(canvas);
                isPlaying = false; //breaks thread
                waitBeforeExiting();
                saveIfHighScore();
                setCurrentScore();
                if(!prefs.getBoolean("isMute", false)) {
                    soundPool.play(sound, 1,1,0,0,1);
                }
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
            editor.putInt("highscore", score/10);
            editor.apply();
        }
    }

    private void setCurrentScore() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("currentScore", score/10);
        editor.apply();
    }

    private void waitBeforeExiting() {
        activity.startActivity(new Intent(activity, GameOver.class));
        activity.finish();
        /*
        try {
            Thread.sleep(500);
            activity.startActivity(new Intent(activity, GameOver.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         */
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
/*
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

    }*/

    /*
    public static boolean LastTry() {
        return isGameOver;
    }

     */

}
