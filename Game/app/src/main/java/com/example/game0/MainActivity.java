package com.example.game0;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    MediaPlayer player;
    private boolean isMute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(new Intent(MainActivity.this, GameActivity.class));

            }

        });

        player = MediaPlayer.create(this, R.raw.cartoon_music);
        player.setLooping(true);
        player.start();
        /*if(player == null)
        {
            player = MediaPlayer.create(this, R.raw.cartoon_music);
            player.setOnCompletionListener((new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopPlayer();
                }
            }));
        }
        player.start(); */


        TextView highScoreTxt = findViewById(R.id.highScoreTxt);

        SharedPreferences prefs = getSharedPreferences("game", MODE_PRIVATE);
        highScoreTxt.setText("HighScore: "+(prefs.getInt("highscore", 0)));

        isMute = prefs.getBoolean("isMute", false);
        ImageView volumeCtrl = findViewById(R.id.volumeCtrl);

        if (isMute)
            volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
        else
            volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);

        volumeCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMute = !isMute;
                if (isMute)
                    volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_off_24);
                else
                    volumeCtrl.setImageResource(R.drawable.ic_baseline_volume_up_24);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isMute", isMute);
                editor.apply();
            }
        });


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


}