package com.example.game0;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.game0.GameView.screenRatioX;
import static com.example.game0.GameView.screenRatioY;

public class Hole {

    public double speed;
    int x = 0, y, width, height;
    Bitmap hole;

    Hole (Resources res, double speed) {
        hole = BitmapFactory.decodeResource(res, R.drawable.hole);

        //speed = GameView.speed;

        this.speed = speed;
        width = hole.getWidth();
        height = hole.getHeight();

        // reducing size
        width /= 8;
        height /= 8;

        //making it compatible
        //width *= (int) screenRatioX;
        //height *= (int) screenRatioY;

        //resizing bitmap
        hole = Bitmap.createScaledBitmap(hole, width, height, false);

        // setting initial location of hole
        y = -height;

    }


    Bitmap getHole() {
        return hole;
    }

    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}
