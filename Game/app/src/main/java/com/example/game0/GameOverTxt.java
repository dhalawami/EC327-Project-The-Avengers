package com.example.game0;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;

public class GameOverTxt {

    private Button StartGameAgain;
    private static int screenX = 1080, screenY = 2220;
    private Context context;

    public GameOverTxt(Context context) {
        this.context = context;
    }

    public static void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.WHITE);

        //canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
        //canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
        canvas.drawText("Game Over!", screenX / 2 - 270, screenY / 2, paint);

        //canvas.drawText("Play Again", screenX / 2 - 270, screenY -



    }

}
