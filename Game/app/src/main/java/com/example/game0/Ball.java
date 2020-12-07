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
    public float speedX;

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

    public void updateSpeed(float angle) {
        
        final int maxSpeed = 35; // max speed of ball
        final int minSpeed = 0; // min speed of ball
        final double minAngle = Math.PI / 36; // angle at which ball achieve max speed
        final double maxAngle = Math.PI / 4; // angle at which ball speed becomes non-zero

        // set speed to zero if absolute value of angle is less than 5 degrees
        if (Math.abs(angle) < minAngle) {
            speedX = minSpeed * Math.signum(angle);
        }
        // cap speed at maxSpeed if angle is greater than maxAngle
        else if(Math.abs(angle) > maxAngle) {
            speedX = maxSpeed * Math.signum(angle);
        } else {
            // speed vs angle will be speed = m(angle) + b where m is the rate of speed vs angel
            // and b is the minimum speed
            double speed_angle_rate = (maxSpeed - minSpeed) / (maxAngle - minAngle);
            speedX = (float)  (Math.signum(angle) * speed_angle_rate * (Math.abs(angle) - minAngle) + minSpeed);
        }
    }
}
