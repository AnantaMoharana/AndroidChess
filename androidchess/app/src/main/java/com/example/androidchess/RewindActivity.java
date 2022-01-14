package com.example.androidchess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import ChessBoardGame.ChessBoard;
import ChessPieceGame.Bishop;
import ChessPieceGame.GameInstance;
import ChessPieceGame.King;
import ChessPieceGame.Knight;
import ChessPieceGame.Pawn;
import ChessPieceGame.PieceSkeleton;
import ChessPieceGame.Queen;
import ChessPieceGame.Rook;

public class RewindActivity extends AppCompatActivity {

    private GameInstance gameToShow;

    private final HashMap<String, Integer> buttonMap = new HashMap<>();
    private final HashMap<Integer, String> map = new HashMap<>();
    private int index = 1;
    private boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_replay);

        ChessBoard board = new ChessBoard();

        Bundle bundle = getIntent().getExtras();
        gameToShow = (GameInstance) bundle.get("board_array");

        //map [x, y] coords to FileRank ('d3')
        for (int i= 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String s = (char)(i+'a') + "" + (j+1);
                int[] x = board.getArrayPositionFromString(s);
                map.put(x[0]*10 + x[1], s);
            }
        }

        putOnMap();
    }

    /***
     * Move Forward to next board
     * @param click View
     */
    public void moveForward(View click){

        Button tmp = (Button) findViewById(R.id.moveForwardButton);
        if (done){
            Intent game_intent = new Intent(this, MainActivity.class);
            startActivity(game_intent);
        }
        else if (index < gameToShow.board.size()){
            updateBoard(gameToShow.board.get(index));
            index++;
        } else {
            tmp.setText("Return Home");
            done = true;
        }
    }

    /***
     * Updates the game board display
     * @param board PieceSkeleton board
     */
    public void updateBoard(PieceSkeleton[][] board){
        ImageButton tmp;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                String FileRank = map.get((i)*10 + (j));
                    tmp = (ImageButton) findViewById(buttonMap.get(FileRank));
                    if (board[i][j] != null) {
                        tmp.setImageResource(getImageOfPiece(board, i, j));
                    } else {
                        tmp.setImageResource(0);
                    }
                }
        }
    }

    /***
     * Turns a board ID into png
     * @param board PieceSkeleton board
     * @param i index row
     * @param j index column
     * @return image index to display
     */
    public int getImageOfPiece(PieceSkeleton[][] board, int i, int j){
        if (board[i][j].getColor() == PieceSkeleton.color.WHITE){
            if (board[i][j] instanceof Pawn) return R.drawable.wpawn;
            if (board[i][j] instanceof King) return R.drawable.wking;
            if (board[i][j] instanceof Queen) return R.drawable.wqueen;
            if (board[i][j] instanceof Rook) return R.drawable.whiterook;
            if (board[i][j] instanceof Bishop) return R.drawable.wbishop;
            if (board[i][j] instanceof Knight) return R.drawable.wknight;
        } else if (board[i][j].getColor() == PieceSkeleton.color.BLACK){
            if (board[i][j] instanceof Pawn) return R.drawable.bpawn;
            if (board[i][j] instanceof King) return R.drawable.bking;
            if (board[i][j] instanceof Queen) return R.drawable.bqueen;
            if (board[i][j] instanceof Rook) return R.drawable.brook;
            if (board[i][j] instanceof Bishop) return R.drawable.bbishop;
            if (board[i][j] instanceof Knight) return R.drawable.bknight;
        }
        return -1;
    }

    /***
     * Adds all String -> Image index R values for hashmap
     */
    public void putOnMap(){
        buttonMap.put("a1", R.id.replay_a1);
        buttonMap.put("a2", R.id.replay_a2);
        buttonMap.put("a3", R.id.replay_a3);
        buttonMap.put("a4", R.id.replay_a4);
        buttonMap.put("a5", R.id.replay_a5);
        buttonMap.put("a6", R.id.replay_a6);
        buttonMap.put("a7", R.id.replay_a7);
        buttonMap.put("a8", R.id.replay_a8);

        buttonMap.put("b1", R.id.replay_b1);
        buttonMap.put("b2", R.id.replay_b2);
        buttonMap.put("b3", R.id.replay_b3);
        buttonMap.put("b4", R.id.replay_b4);
        buttonMap.put("b5", R.id.replay_b5);
        buttonMap.put("b6", R.id.replay_b6);
        buttonMap.put("b7", R.id.replay_b7);
        buttonMap.put("b8", R.id.replay_b8);

        buttonMap.put("c1", R.id.replay_c1);
        buttonMap.put("c2", R.id.replay_c2);
        buttonMap.put("c3", R.id.replay_c3);
        buttonMap.put("c4", R.id.replay_c4);
        buttonMap.put("c5", R.id.replay_c5);
        buttonMap.put("c6", R.id.replay_c6);
        buttonMap.put("c7", R.id.replay_c7);
        buttonMap.put("c8", R.id.replay_c8);

        buttonMap.put("d1", R.id.replay_d1);
        buttonMap.put("d2", R.id.replay_d2);
        buttonMap.put("d3", R.id.replay_d3);
        buttonMap.put("d4", R.id.replay_d4);
        buttonMap.put("d5", R.id.replay_d5);
        buttonMap.put("d6", R.id.replay_d6);
        buttonMap.put("d7", R.id.replay_d7);
        buttonMap.put("d8", R.id.replay_d8);

        buttonMap.put("e1", R.id.replay_e1);
        buttonMap.put("e2", R.id.replay_e2);
        buttonMap.put("e3", R.id.replay_e3);
        buttonMap.put("e4", R.id.replay_e4);
        buttonMap.put("e5", R.id.replay_e5);
        buttonMap.put("e6", R.id.replay_e6);
        buttonMap.put("e7", R.id.replay_e7);
        buttonMap.put("e8", R.id.replay_e8);

        buttonMap.put("f1", R.id.replay_f1);
        buttonMap.put("f2", R.id.replay_f2);
        buttonMap.put("f3", R.id.replay_f3);
        buttonMap.put("f4", R.id.replay_f4);
        buttonMap.put("f5", R.id.replay_f5);
        buttonMap.put("f6", R.id.replay_f6);
        buttonMap.put("f7", R.id.replay_f7);
        buttonMap.put("f8", R.id.replay_f8);

        buttonMap.put("g1", R.id.replay_g1);
        buttonMap.put("g2", R.id.replay_g2);
        buttonMap.put("g3", R.id.replay_g3);
        buttonMap.put("g4", R.id.replay_g4);
        buttonMap.put("g5", R.id.replay_g5);
        buttonMap.put("g6", R.id.replay_g6);
        buttonMap.put("g7", R.id.replay_g7);
        buttonMap.put("g8", R.id.replay_g8);

        buttonMap.put("h1", R.id.replay_h1);
        buttonMap.put("h2", R.id.replay_h2);
        buttonMap.put("h3", R.id.replay_h3);
        buttonMap.put("h4", R.id.replay_h4);
        buttonMap.put("h5", R.id.replay_h5);
        buttonMap.put("h6", R.id.replay_h6);
        buttonMap.put("h7", R.id.replay_h7);
        buttonMap.put("h8", R.id.replay_h8);
    }

}