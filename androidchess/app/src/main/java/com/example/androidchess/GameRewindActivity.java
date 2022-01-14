package com.example.androidchess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Comparator;

import ChessPieceGame.GameInstance;
import ChessPieceGame.SavedGames;

/***
 * GameRewindActivity - to show rewinded games
 * @Author Trevor Scott
 * @Author Ananta Moharana
 */
public class GameRewindActivity extends AppCompatActivity {

    private ScrollView view;

    /***
     * Initialization method for this activity
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_home);

        view = (ScrollView) this.findViewById(R.id.scrollView2);

        try {
            ArrayList<GameInstance> gList = SavedGames.read(this);
            for (GameInstance g: gList){
                addReplay(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * Sort By Date
     * @param view view
     */
    public void sortByDate(View view){
        try {
            ArrayList<GameInstance> gList = SavedGames.read(this);
            gList.sort(Comparator.comparing(a -> a.date));
            SavedGames.write(gList, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent game_intent = new Intent(this, GameRewindActivity.class);
        startActivity(game_intent);
    }

    /***
     * Sort By Name
     * @param view view
     */
    public void sortByName(View view){
        try {
            ArrayList<GameInstance> gList = SavedGames.read(this);
            gList.sort(Comparator.comparing(a -> a.name));
            SavedGames.write(gList, this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent game_intent = new Intent(this, GameRewindActivity.class);
        finish();
        startActivity(game_intent);
    }

    /***
     * Adds replays to listview
     * @param g GameInstance to add
     */
    public void addReplay(GameInstance g){

        LinearLayout lay = (LinearLayout) this.findViewById(R.id.linlay);

        TextView textName = new TextView(this);
        textName.setText(g.name);

        TextView textDate = new TextView(this);
        textDate.setText(g.date);

        Button button = new Button(this);
        button.setText("Play");

        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, RewindActivity.class);
            intent.putExtra("board_array", g);
            startActivity(intent);
        });

        lay.addView(textName);
        lay.addView(textDate);
        lay.addView(button);
    }
}
