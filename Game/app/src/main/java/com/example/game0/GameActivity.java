package com.example.game0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements SensorEventListener {

    MediaPlayer player;
    private GameView gameView;
    //private Button StartAgain;
    //SharedPreferences sharedPreferences;
    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagneticField;
    private float[] gravityVector;
    private float[] geomagneticVector;
    public float roll;
    public float pitch;
    public float azimuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        gameView = new GameView(this, point.x, point.y);

        setContentView(gameView);

        if(player == null)
        {
            player = MediaPlayer.create(this, R.raw.vibe_mountain);
            player.setOnCompletionListener((new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            }));
        }
        player.start();

        //Button playAgain = (Button) findViewById(R.id.play_again);


        //sharedPreferences = getSharedPreferences(mySharedPreferences, MODE_PRIVATE);

        /*if (isGameOver) {
            //startActivity(new Intent(GameActivity.this, GameOver.class));
            startActivity(new Intent(GameActivity.this, MainActivity.class));

        }*/
        //Sensors data
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        mMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gravityVector = new float[3];
        geomagneticVector = new float[3];

    }
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

        this.updateRoll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(player != null)
        {
            player.pause();
        }
        gameView.pause();
        sensorManager.unregisterListener(this);

    }
    
    @Override
    public final void onSensorChanged(SensorEvent event) {

        // if accelerometer changes values
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, gravityVector, 0, 3);
            // if geomagnetic sensor is different
        } else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, geomagneticVector, 0, 3);
        }

        this.updateRoll();
    }
     private void updateRoll() {

        float[] R = new float[9];
        boolean success = SensorManager.getRotationMatrix(R, null, gravityVector, geomagneticVector);
        if(success) {
            float[] orientation = new float[3];
            SensorManager.getOrientation(R, orientation);
            azimuth = orientation[0];
            pitch = orientation[1];
            roll = orientation[2];
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mMagneticField, SensorManager.SENSOR_DELAY_NORMAL);
        gameView.resume();
        player.start();

    }

    private void stopPlayer()
    {
        if(player != null)
        {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        stopPlayer();
    }


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

