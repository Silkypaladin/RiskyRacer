package com.example.riskyracer.logic;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.example.riskyracer.activities.GameActivity;

import java.util.ArrayList;

public class SceneManager {

    private ArrayList<Scene> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE;

    public SceneManager(GameActivity act) {
        ACTIVE_SCENE = 0;
        scenes.add(new GameScene(act));
    }

    public void update() {
        scenes.get(ACTIVE_SCENE).update();
    }

    public void draw(Canvas canvas) {
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }

    public void receiveTouch(MotionEvent event) {
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }
}
