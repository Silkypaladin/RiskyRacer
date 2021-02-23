package com.example.riskyracer.logic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;

import com.example.riskyracer.Constants;

import java.util.ArrayList;

public class ObstacleManager {

    private ArrayList<Obstacle> obstacles;
    private int playerGap;
    private int obstacleGap;
    private int obstacleHeight;

    private long startTime;
    private long initTime;
    private int score = 0;

    public ObstacleManager(int playerGap, int obstacleGap, int obstacleHeight) {
        //Przesylac ilosc gapow - wtedy obliczac wielkosc przeszkod = width - n*gaps i generowac sobie jakies okreslone przeszkody zeby sie w tym width zmiescily (mozna rozne przeszkody)
        this.playerGap = playerGap;
        obstacles = new ArrayList<>();
        this.obstacleGap = obstacleGap;
        this.obstacleHeight = obstacleHeight;
        startTime = initTime = System.currentTimeMillis();
        createObstacles();
    }

    private void createObstacles() {
        int currY = (-5* Constants.SCREEN_HEIGHT)/4;
        while (currY < 0) {
            int xStart = (int) (Math.random()*(Constants.SCREEN_WIDTH - playerGap));
            obstacles.add(new Obstacle(obstacleHeight, Color.rgb(0,0,0), xStart, currY, playerGap));
            currY += obstacleHeight + obstacleGap;
        }
    }

    public boolean playerHit(Player player) {
        for (Obstacle o: obstacles) {
            if (o.playerHit(player))
                return true;
        }
        return false;
    }

    public void update() {
            if (startTime < Constants.INIT_TIME)
                startTime = Constants.INIT_TIME;
            int elapsedTime = (int) (System.currentTimeMillis() - startTime);
            startTime = System.currentTimeMillis();
            float speed = (float) (Math.sqrt(1 + (startTime - initTime) / 5000))  * Constants.SCREEN_HEIGHT/(10000.0f);
            for(Obstacle o: obstacles) {
                o.incrementY(speed * elapsedTime);
            }
            if (obstacles.get(obstacles.size() - 1).getRectangle().top >= Constants.SCREEN_HEIGHT) {
                int xStart = (int) (Math.random() * (Constants.SCREEN_WIDTH - playerGap));
                obstacles.add(0, new Obstacle(obstacleHeight, Color.BLACK, xStart, obstacles.get(0).getRectangle().top - obstacleHeight - obstacleGap, playerGap));
                obstacles.remove(obstacles.size() - 1);
                score ++;
            }
    }

    public void draw(Canvas canvas) {
        for (Obstacle o: obstacles) {
            o.draw(canvas);
        }
        Paint paint = new Paint();
        paint.setTextSize(100);
        paint.setColor(Color.MAGENTA);
        canvas.drawText("" + score, 50, 70 + paint.descent() - paint.ascent(), paint);
    }

    public void saveCurrentScore(Context ctx) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currentScore", score);
        editor.commit();
    }

}
