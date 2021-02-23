package com.example.riskyracer.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.riskyracer.Constants;
import com.example.riskyracer.activities.GameActivity;

public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private MainThread main;
    private SceneManager manager;

    public GamePanel(Context ctx, GameActivity act) {
        super(ctx);
        getHolder().addCallback(this);
        Constants.CURRENT_CONTEXT = ctx;
        main = new MainThread(getHolder(), this);
        manager = new SceneManager(act);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        main = new MainThread(getHolder(), this);
        Constants.INIT_TIME = System.currentTimeMillis();
        main.setRunning(true);
        main.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                main.setRunning(false);
                main.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
            retry = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        manager.receiveTouch(event);
        return true;
    }

    public void update() {
        manager.update();
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        manager.draw(canvas);
    }


}
