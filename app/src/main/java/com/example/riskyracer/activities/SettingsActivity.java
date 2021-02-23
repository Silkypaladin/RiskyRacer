package com.example.riskyracer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Switch;

import com.example.riskyracer.BackgroundMusicService;
import com.example.riskyracer.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch musicSwitch = findViewById(R.id.musicSwitch);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        boolean musicChoice = preferences.getBoolean("musicChoice", true);
        musicSwitch.setChecked(musicChoice);
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                stopService(new Intent(SettingsActivity.this, BackgroundMusicService.class));
                editor.putBoolean("musicChoice", false);
                editor.commit();
            }
            else if (isChecked) {
                startService(new Intent(SettingsActivity.this, BackgroundMusicService.class));
                editor.putBoolean("musicChoice", true);
                editor.commit();
            }
        });
    }
}