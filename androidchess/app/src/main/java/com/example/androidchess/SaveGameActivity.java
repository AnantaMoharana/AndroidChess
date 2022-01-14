package com.example.androidchess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import ChessPieceGame.GameInstance;
import ChessPieceGame.PieceSkeleton;
import ChessPieceGame.SavedGames;

/***
 * Save Game Activity - to save games
 * @Author Trevor Scott
 * @Author Ananta Moharana
 */
public class SaveGameActivity extends AppCompatActivity {

    private ArrayList<PieceSkeleton[][]> savedGame;

    /***
     * Initialization method for save game activity
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_activity);

        Bundle bundle = getIntent().getExtras();
        savedGame = (ArrayList<PieceSkeleton[][]>) bundle.get("game_board");
    }

    /***
     * Save game method on click - saves the game and returns home.
     * @param click View
     */
    public void saveGame(View click) throws IOException, ClassNotFoundException {
        TextView t = (TextView)findViewById(R.id.editTextTextPersonName);

        String date = Calendar.getInstance().getTime().toString();
        String name = t.getText().toString();

        GameInstance g = new GameInstance(date, name, savedGame);

        //Save the instance of the game
        ArrayList<GameInstance> gList = SavedGames.read(this);
        gList.add(g);
        SavedGames.write(gList, this);

        Intent game_intent = new Intent(this, MainActivity.class);
        startActivity(game_intent);
    }

    /***
     * Go home to main screen.
     * @param click View
     */
    public void goHome(View click){
        Intent game_intent = new Intent(this, MainActivity.class);
        startActivity(game_intent);
    }
}