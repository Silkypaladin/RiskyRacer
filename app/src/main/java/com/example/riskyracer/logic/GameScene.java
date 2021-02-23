package com.example.riskyracer.logic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.example.riskyracer.Constants;
import com.example.riskyracer.R;
import com.example.riskyracer.activities.GameActivity;
import com.example.riskyracer.activities.GameOverActivity;

public class GameScene implements Scene {

    private Player player;
    private Point point;
    private ObstacleManager manager;
    GameActivity act;

    private boolean playerMoving = false;
    private boolean gameOver = false;
    private long gameOverTime;

    private OrientationData orientationData;
    private long frameTime;

    public GameScene(GameActivity act) {
        this.act = act;
        player = new Player(new Rect(100,100,200,200), Color.rgb(255,0,0));
        point = new Point( Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(point);
        manager = new ObstacleManager(Constants.PLAYER_GAP, Constants.OBSTACLE_GAP, Constants.OBSTACLE_HEIGHT);
        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
        orientationData.newGame();
    }

    @Override
    public void update() {
        if (!gameOver) {
            if (frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int) (System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if (orientationData.getOrientation() != null && orientationData.getStartOrientation() != null) {
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll  = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];
                float xSpeed = 2 * roll * Constants.SCREEN_WIDTH / Constants.TILT_SPEED_SCALER;
                float ySpeed = pitch * Constants.SCREEN_HEIGHT / Constants.TILT_SPEED_SCALER;

                point.x += Math.abs(xSpeed * elapsedTime) > 5 ? xSpeed * elapsedTime : 0;
                point.y -= Math.abs(ySpeed * elapsedTime) > 5 ? ySpeed * elapsedTime: 0;
            }

            if (point.x < 0)
                point.x = 0;
            else if (point.x > Constants.SCREEN_WIDTH)
                point.x = Constants.SCREEN_WIDTH;

            if (point.y < 0)
                point.y = 0;
            else if (point.y > Constants.SCREEN_HEIGHT)
                point.y = Constants.SCREEN_HEIGHT;

            player.update(point);
            manager.update();
            if (manager.playerHit(player)) {
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
        if (gameOver) {
            manager.saveCurrentScore(act);
            MainThread.running = false;
            act.finish();
            Intent intent = new Intent(act, GameOverActivity.class);
            Constants.CURRENT_CONTEXT.startActivity(intent);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        Bitmap background = BitmapFactory.decodeResource(Constants.CURRENT_CONTEXT.getResources(), R.drawable.back);
        Bitmap scaled = getResizedBitmap(background, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        canvas.drawBitmap(scaled, 0, 0, null);
        player.draw(canvas);
        manager.draw(canvas);
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
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;

    }

    public void reset() {
        point.x = Constants.SCREEN_WIDTH/2;
        point.y = 3*Constants.SCREEN_HEIGHT/4;
        player.update(point);
        manager = new ObstacleManager(200, 400, 50);
        playerMoving = false;
    }

    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!gameOver && player.getRectangle().contains((int) event.getX(), (int) event.getY())) {
                    playerMoving = true;
                }
                if (gameOver && System.currentTimeMillis() - gameOverTime > 2000) {
                    reset();
                    gameOver = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (playerMoving && !gameOver) {
                    point.set((int) event.getX(), (int) event.getY());
                }
                break;
            case MotionEvent.ACTION_UP:
                playerMoving = false;
                break;
        }
    }


}
