package com.example.riskyracer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.example.riskyracer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        TextView score = findViewById(R.id.scoreValue);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int scoreValue = preferences.getInt("currentScore", 0);
        updateHighScoresArrayIfNeeded(scoreValue);
        score.setText(scoreValue + "");
    }

    public void loadGameActivity(View view) {
        final Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public void loadMainMenuActivity(View view) {
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void updateHighScoresArrayIfNeeded(int score) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        ArrayList<String> scores;
        String s = preferences.getString("highScores", "");
        if (!s.equals("")) {
            scores = new ArrayList<>(Arrays.asList(s.split(",")));
            String scoreString = String.valueOf(score);
            String res = "";
            if (!scores.contains(scoreString)) {
                if (Integer.parseInt(scores.get(0)) < score) {
                    scores.add(scoreString);
                    if (scores.size() > 5)
                        scores.remove(0);
                    Collections.sort(scores);
                    for (String sc: scores)
                        res += sc + ",";
                    editor.putString("highScores", res);
                }
            }
        }
        else {
            editor.putString("highScores", String.valueOf(score));
        }
        editor.commit();
    }
}