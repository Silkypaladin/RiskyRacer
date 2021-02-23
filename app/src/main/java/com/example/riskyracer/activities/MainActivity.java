package com.example.riskyracer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.riskyracer.BackgroundMusicService;
import com.example.riskyracer.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkMusicPlay()) {
            Intent musicIntent = new Intent(MainActivity.this, BackgroundMusicService.class);
            startService(musicIntent);
        }
    }


    public void loadSettingsActivity(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void loadHighScoresActivity(View view) {
        Intent intent = new Intent(this, HighScoresActivity.class);
        startActivity(intent);
    }

    public void loadGameActivity(View view) {
        final Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    public boolean checkMusicPlay() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean musicChoice = preferences.getBoolean("musicChoice", true);
        return musicChoice;
    }
}