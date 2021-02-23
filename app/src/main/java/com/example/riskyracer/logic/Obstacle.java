package com.example.riskyracer.logic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.riskyracer.Constants;

import java.util.ArrayList;

public class Obstacle implements GameObject {

    private ArrayList<Rect> rectangles;
    private int color;



    public Obstacle(int rectHeight, int color, int startX, int startY, int playerGap) {
        this.color = color;
        this.rectangles = new ArrayList<>();
        rectangles.add(new Rect(0, startY, startX, startY + rectHeight));
        rectangles.add(new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight));
    }

    public boolean playerHit(Player player) {
        for (int i = 0; i < rectangles.size(); i++) {
            if (Rect.intersects(rectangles.get(i), player.getRectangle()))
                return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        for (int i = 0; i < rectangles.size(); i++) {
            canvas.drawRect(rectangles.get(i), paint);
        }
    }

    private Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    @Override
    public void update() {

    }

    public void incrementY(float y) {
        for (int i = 0; i < rectangles.size(); i++) {
            rectangles.get(i).top += y;
            rectangles.get(i).bottom += y;
        }
    }

    public Rect getRectangle() {
        return rectangles.get(0);
    }
}
