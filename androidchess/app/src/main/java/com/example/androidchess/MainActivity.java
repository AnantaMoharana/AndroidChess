package com.example.androidchess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/***
 * Main Activity run when app is loaded up
 * @Author Trevor Scott
 * @Author Ananta Moharana
 */
public class MainActivity extends AppCompatActivity {

    /***
     * Initialization method
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /***
     * Takes user to ChessGame Activity class
     * @param view View
     */
    public void playGameButtonClick(View view){
        Intent game_intent = new Intent(this, ChessGame.class);
        startActivity(game_intent);
    }

    /***
     * Takes user to GameRewind Activity class
     * @param view View
     */
    public void replayGameButtonClick(View view){
        Intent game_intent = new Intent(this, GameRewindActivity.class);
        startActivity(game_intent);
    }
}