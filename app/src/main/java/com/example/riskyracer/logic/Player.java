package com.example.riskyracer.logic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import com.example.riskyracer.Constants;
import com.example.riskyracer.R;

public class Player implements GameObject {

    private Rect rectangle;
    private int color;
    private Animation driving;
    private AnimationManager animManager;

    public Player(Rect rect, int color) {
        rectangle = rect;
        this.color = color;

        BitmapFactory bf = new BitmapFactory();
        Bitmap blink1 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.first);
        Bitmap blink2 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.second);
        Bitmap blink3 = bf.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.third);

        driving = new Animation(new Bitmap[]{blink1, blink2, blink3}, 0.5f);
        animManager = new AnimationManager(new Animation[]{driving});
    }

    @Override
    public void draw(Canvas canvas) {
        driving.draw(canvas, rectangle);
    }

    @Override
    public void update() {
        animManager.update();
    }

    public void update(Point point) {
        rectangle.set(point.x - rectangle.width()/2, point.y - rectangle.height()/2,
                point.x + rectangle.width()/2, point.y + rectangle.height()/2);
        animManager.playAnim(0);
        animManager.update();
    }

    public Rect getRectangle() {
        return rectangle;
    }

    public int getColor() {
        return color;
    }
}
