package com.example.game0;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import static com.example.game0.GameView.screenRatioX;
import static com.example.game0.GameView.screenRatioY;

public class Ball {

    public boolean isGoingLeft = false;
    public boolean isGoingRight = false;
    int x, y, width, height, ballCounter = 0;
    Bitmap ball1, ball2;

    Ball(int screenX, Resources res) {

        ball1 = BitmapFactory.decodeResource(res, R.drawable.ball2);
        ball2 = BitmapFactory.decodeResource(res, R.drawable.ball2);    // second frame of ball - change here when new images found

        width = ball1.getWidth();
        height = ball1.getHeight();


        //reducing size of image
        width /= 10;
        height /= 10;

        // resizing bitmap
        ball1 = Bitmap.createScaledBitmap(ball1, width, height, false);
        ball2 = Bitmap.createScaledBitmap(ball2, width, height, false);

        //making the compatible with different devices
        //width *= (int) screenRatioX;
        //height *= (int) screenRatioY;

        //centering ball in the center of the screen
        x = screenX / 2;
        y = 64;
        // y = (int) (64 * screenRatioX);   //compatability



    }

    Bitmap getBall () {
        if (ballCounter == 0) {
            ballCounter++;
            return ball1;
        }

        ballCounter--;
        return ball2;

    }


    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

}
