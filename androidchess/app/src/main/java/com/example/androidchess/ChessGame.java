package com.example.androidchess;

import androidx.appcompat.app.AppCompatActivity;

import ChessBoardGame.ChessBoard;
import ChessPieceGame.Bishop;
import ChessPieceGame.King;
import ChessPieceGame.Knight;
import ChessPieceGame.Pawn;
import ChessPieceGame.PieceSkeleton;
import ChessPieceGame.Queen;
import ChessPieceGame.Rook;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;
import java.util.Random;

/***
 * ChessGame - Main Activity for gameplay
 * @Author Trevor Scott
 * @Author Ananta Moharana
 */
public class ChessGame extends AppCompatActivity {

    private String moveTo = null;
    private String moveFrom = null;

    private ChessBoard board;
    private TextView theMove;

    private final HashMap<Integer, String> map = new HashMap<>();
    private final HashMap<String, Integer> buttonMap = new HashMap<>();
    private boolean gameInProgress = true;
    private String pawnPromotion = "Q"; // should always be queen per instructions

    //Where the moves are stored [NEED THIS FOR STORING/REPLAYING GAMES].
    private Stack<PieceSkeleton[][]> moves = new Stack<>();

    enum Turn {
        BLACK,
        WHITE
    }

    private Turn turn;

    /***
     * Initialization method for this activity
     * @param savedInstanceState Bundle instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_game);

        this.board = new ChessBoard();

        //map [x, y] coords to FileRank ('d3')
        for (int i= 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String s = (char)(i+'a') + "" + (j+1);
                int[] x = board.getArrayPositionFromString(s);
                map.put(x[0]*10 + x[1], s);
            }
        }

        theMove = (TextView) findViewById(R.id.playersTurn);
        putOnMap();
        turn = Turn.WHITE;
    }

    /***
     * Force a draw between players
     * @param click View
     */
    public void draw(View click){
        gameInProgress = false;
        theMove.setText("Draw was initiated. Game over.");

        Button moveOn = (Button)findViewById(R.id.finish);
        moveOn.setVisibility(gameInProgress ? View.GONE : View.VISIBLE);
    }

    /***
     * Force a resign between players
     * @param click View
     */
    public void resign(View click){
        gameInProgress = false;

        if (turn == Turn.BLACK){
            theMove.setText("Black resigned. White wins!");
        } else {
            theMove.setText("White resigned. Black wins!");
        }

        Button moveOn = (Button)findViewById(R.id.finish);
        moveOn.setVisibility(gameInProgress ? View.GONE : View.VISIBLE);
    }

    /***
     * Inner (nested) class for AI related moves.
     */
    class PossibleMove {
        public int[] to;
        public int[] from;

        public PossibleMove(int[] to, int[] from){
            this.to = Arrays.copyOf(to, 2);
            this.from = Arrays.copyOf(from, 2);
        }
    }

    /***
     * Generated random move for robot to click into
     * @param click View
     */
    public void AIMove(View click){
        ArrayList<PossibleMove> lst = new ArrayList<>();

        PieceSkeleton.color color = null;
        if (turn == Turn.WHITE)
            color = PieceSkeleton.color.WHITE;
        else
            color = PieceSkeleton.color.BLACK;

        int[] kingPostion=board.getKingPosition(color);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.getBoard()[i][j] != null && board.getBoard()[i][j].getColor() == color) {

                    ArrayList<int[]> validMoves = board.getBoard()[i][j].getAllValidMoves(new int[]{i, j}, board.getBoard());

                    for (int[] validMove: validMoves){
                        PieceSkeleton tmp = board.getBoard()[validMove[0]][validMove[1]];
                        board.getBoard()[validMove[0]][validMove[1]] = board.getBoard()[i][j];
                        //need to check that i,j are not the king
                        board.getBoard()[i][j] = null;
                        if(Arrays.equals(kingPostion,new int[]{i,j})){
                            if (!board.isInCheck(color,new int[]{validMove[0],validMove[1]})) {
                                lst.add(new PossibleMove(validMove, new int[]{i, j}));
                            }
                            board.getBoard()[i][j] = board.getBoard()[validMove[0]][validMove[1]];
                            board.getBoard()[validMove[0]][validMove[1]] = tmp;
                        }else{
                            if (!board.isInCheck(color,kingPostion)) {
                                lst.add(new PossibleMove(validMove, new int[]{i, j}));
                            }
                            board.getBoard()[i][j] = board.getBoard()[validMove[0]][validMove[1]];
                            board.getBoard()[validMove[0]][validMove[1]] = tmp;
                        }
                    }
                }
            }
        }

        if (lst.size() == 0){
            if (turn == Turn.WHITE)
                theMove.setText("Checkmate - Black Wins");
            else
                theMove.setText("Checkmate - White Wins");
            gameInProgress=false;
            Button moveOn = (Button)findViewById(R.id.finish);
            moveOn.setVisibility(gameInProgress ? View.GONE : View.VISIBLE);
            return;
        }

        Random random = new Random();
        PossibleMove ptr = lst.get(random.nextInt(lst.size()));

        if (turn == Turn.WHITE) {
            PieceSkeleton[][] copy = Arrays.stream(board.getBoard()).map(PieceSkeleton[]::clone).toArray(PieceSkeleton[][]::new);
            board.movePieceToPos(map.get(ptr.from[0] * 10 + ptr.from[1]), map.get(ptr.to[0] * 10 + ptr.to[1]), pawnPromotion, PieceSkeleton.color.WHITE,click);
            moves.push(copy);
            turn = Turn.BLACK;
            theMove.setText(map.get(ptr.from[0] * 10 + ptr.from[1]) + " -> " + map.get(ptr.to[0] * 10 + ptr.to[1]) + " Black's turn!");
        }
        else {
            PieceSkeleton[][] copy = Arrays.stream(board.getBoard()).map(PieceSkeleton[]::clone).toArray(PieceSkeleton[][]::new);
            board.movePieceToPos(map.get(ptr.from[0] * 10 + ptr.from[1]), map.get(ptr.to[0] * 10 + ptr.to[1]), pawnPromotion, PieceSkeleton.color.BLACK,click);
            moves.push(copy);
            turn = Turn.WHITE;
            theMove.setText(map.get(ptr.from[0] * 10 + ptr.from[1]) + " -> " + map.get(ptr.to[0] * 10 + ptr.to[1]) + " White's turn!");
        }

        updateBoard(board.getBoard());
    }

    /***
     * Moves to save screen activity
     * @param click View
     */
    public void moveToSaveScreen(View click){
        Intent intent = new Intent(this, SaveGameActivity.class);
        intent.putExtra("game_board", moves);
        startActivity(intent);
    }

    /***
     * Undoes the last move
     * @param click View
     */
    public void undo(View click) {
        if (moves.isEmpty()) return;

        PieceSkeleton[][] copy = moves.pop();

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                board.getBoard()[i][j] = copy[i][j];
            }
        }
        updateBoard(board.getBoard());

        if (turn == Turn.BLACK) {
            turn = Turn.WHITE;
            theMove.setText("White's turn!");
        }
        else {
            turn = Turn.BLACK;
            theMove.setText("Black's turn!");
        }
        Button undoButton=(Button)findViewById(R.id.undoButton);
        undoButton.setEnabled(false);
    }

    /***
     * Main method for game. Called when any piece is clicked
     * @param click View
     */
    public void touchBoard(View click){
        ImageButton clicked = (ImageButton) click;
        String boardPosition = click.getResources().getResourceName(clicked.getId());

        if (gameInProgress) {
            String pos = boardPosition.substring(boardPosition.length() - 2);

            updateBoard(board.getBoard());

            if (moveFrom == null) {
                moveFrom = pos;
                if (turn == Turn.WHITE)
                    theMove.setText("White: " + pos + " -> ");
                else
                    theMove.setText("Black: " + pos + " -> ");
            } else {
                moveTo = pos;

                if (board.isCheckmate(PieceSkeleton.color.WHITE)) {
                    gameInProgress = false;
                    theMove.setText("Checkmate - Black Wins");
                    Button moveOn = (Button) findViewById(R.id.finish);
                    moveOn.setVisibility(gameInProgress ? View.GONE : View.VISIBLE);
                    return;
                }
                if (board.isCheckmate(PieceSkeleton.color.BLACK)) {
                    gameInProgress = false;
                    theMove.setText("Checkmate - White Wins");
                    Button moveOn = (Button) findViewById(R.id.finish);
                    moveOn.setVisibility(gameInProgress ? View.GONE : View.VISIBLE);
                    return;
                }
                if (board.isInCheck(PieceSkeleton.color.WHITE, board.getKingPosition(PieceSkeleton.color.WHITE)))
                    theMove.setText("Check");

                if (turn == Turn.WHITE) {
                    if (board.positionIsEmpty(moveFrom) || board.getColorFromPosition(moveFrom) != PieceSkeleton.color.WHITE) {
                        Snackbar.make(click, "Illegal move. Try again.", BaseTransientBottomBar.LENGTH_SHORT).show();
                        moveTo = null;
                        moveFrom = null;
                    } else {
                        PieceSkeleton[][] copy = Arrays.stream(board.getBoard()).map(PieceSkeleton[]::clone).toArray(PieceSkeleton[][]::new);

                        boolean turnOver = !board.movePieceToPos(moveFrom, moveTo, "P", PieceSkeleton.color.WHITE,click);

                        if(!turnOver){
                            int[] whitepos=board.getArrayPositionFromString(moveTo);

                            if (board.getBoard()[whitepos[0]][whitepos[1]] instanceof Pawn && whitepos[0]==7){
                                //pop up code and then set the value of pawnPromotion
                                LayoutInflater inflater = (LayoutInflater)
                                        getSystemService(LAYOUT_INFLATER_SERVICE);
                                View popupView = inflater.inflate(R.layout.popup_window, null);
                                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                boolean focusable = true; // lets taps outside the popup also dismiss it
                                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                                // show the popup window
                                // which view you pass in doesn't matter, it is only used for the window tolken
                                popupWindow.showAtLocation(click, Gravity.CENTER, 0, 0);
                                Button wqp=(Button)popupView.findViewById(R.id.QueenPromotion);
                                Button wkp=(Button)popupView.findViewById(R.id.KnightPromotion);
                                Button wbp=(Button)popupView.findViewById(R.id.BishopPromotion);
                                Button wrp=(Button)popupView.findViewById(R.id.RookPromotion);

                                wqp.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    //method for handling what happens when you click Rock buttonImage.
                                    public void onClick(View v) {
                                        //Snackbar.make(click, "Made a queen", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        board.getBoard()[whitepos[0]][whitepos[1]]=new Queen(board.getBoard()[whitepos[0]][whitepos[1]].getColor());
                                        updateBoard(board.getBoard());
                                        popupWindow.dismiss();
                                    }
                                });

                                wkp.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    //method for handling what happens when you click Rock buttonImage.
                                    public void onClick(View v) {
                                        //Snackbar.make(click, "Made a Knight", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        board.getBoard()[whitepos[0]][whitepos[1]]=new Knight(board.getBoard()[whitepos[0]][whitepos[1]].getColor());
                                        updateBoard(board.getBoard());
                                        popupWindow.dismiss();
                                    }
                                });

                                wbp.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    //method for handling what happens when you click Rock buttonImage.
                                    public void onClick(View v) {
                                        //Snackbar.make(click, "Made a Knight", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        board.getBoard()[whitepos[0]][whitepos[1]]=new Bishop(board.getBoard()[whitepos[0]][whitepos[1]].getColor());
                                        updateBoard(board.getBoard());
                                        popupWindow.dismiss();
                                    }
                                });

                                wrp.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    //method for handling what happens when you click Rock buttonImage.
                                    public void onClick(View v) {
                                        //Snackbar.make(click, "Made a Knight", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        board.getBoard()[whitepos[0]][whitepos[1]]=new Rook(board.getBoard()[whitepos[0]][whitepos[1]].getColor(),((Pawn) board.getBoard()[whitepos[0]][whitepos[1]]).getMoves());
                                        updateBoard(board.getBoard());
                                        popupWindow.dismiss();
                                    }
                                });

                            }



                        }

                        if (!turnOver) {
                            if (moveFrom != null)
                                theMove.setText(moveTo + " -> " + moveFrom + ". Black's turn!");
                            turn = Turn.BLACK;
                            moves.push(copy);
                            moveTo = null;
                            moveFrom = null;
                        } else {
                            Snackbar.make(click, "Illegal move. Try again.", BaseTransientBottomBar.LENGTH_SHORT).show();
                            moveTo = null;
                            moveFrom = null;
                            theMove.setText("White's turn");
                        }
                        updateBoard(board.getBoard());
                    }
                } else if (turn == Turn.BLACK) {
                    if (board.isCheckmate(PieceSkeleton.color.BLACK)) {
                        gameInProgress = false;
                        theMove.setText("Checkmate - White Wins");
                        Button moveOn = (Button) findViewById(R.id.finish);
                        moveOn.setVisibility(gameInProgress ? View.GONE : View.VISIBLE);
                        return;
                    }
                    if (board.isCheckmate(PieceSkeleton.color.WHITE)) {
                        gameInProgress = false;
                        theMove.setText("Checkmate - Black Wins");
                        Button moveOn = (Button) findViewById(R.id.finish);
                        moveOn.setVisibility(gameInProgress ? View.GONE : View.VISIBLE);
                        return;
                    }
                    if (board.positionIsEmpty(moveFrom) || board.getColorFromPosition(moveFrom) != PieceSkeleton.color.BLACK) {
                        Snackbar.make(click, "Illegal move. Try again.", BaseTransientBottomBar.LENGTH_SHORT).show();
                        moveTo = null;
                        moveFrom = null;
                    } else { //promotion check
                        PieceSkeleton[][] copy = Arrays.stream(board.getBoard()).map(PieceSkeleton[]::clone).toArray(PieceSkeleton[][]::new);

                        boolean turnOver = !board.movePieceToPos(moveFrom, moveTo, "P", PieceSkeleton.color.BLACK,click);
                        if(!turnOver){
                            int[] blackpos=board.getArrayPositionFromString(moveTo);


                            if (board.getBoard()[blackpos[0]][blackpos[1]] instanceof Pawn && blackpos[0]==0){
                                //pop up code and then set the value of pawnPromotion
                                LayoutInflater inflater = (LayoutInflater)
                                        getSystemService(LAYOUT_INFLATER_SERVICE);
                                View popupView = inflater.inflate(R.layout.popup_window, null);
                                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                boolean focusable = true; // lets taps outside the popup also dismiss it
                                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                                // show the popup window
                                // which view you pass in doesn't matter, it is only used for the window tolken
                                popupWindow.showAtLocation(click, Gravity.CENTER, 0, 0);

                                Button bqp=(Button)popupView.findViewById(R.id.QueenPromotion);
                                Button bkp=(Button)popupView.findViewById(R.id.KnightPromotion);
                                Button bbp=(Button)popupView.findViewById(R.id.BishopPromotion);
                                Button brp=(Button)popupView.findViewById(R.id.RookPromotion);

                                bqp.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    //method for handling what happens when you click Rock buttonImage.
                                    public void onClick(View v) {
                                        //Snackbar.make(click, "Made a queen", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        board.getBoard()[blackpos[0]][blackpos[1]]=new Queen(board.getBoard()[blackpos[0]][blackpos[1]].getColor());
                                        updateBoard(board.getBoard());
                                        popupWindow.dismiss();
                                    }
                                });

                                bkp.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    //method for handling what happens when you click Rock buttonImage.
                                    public void onClick(View v) {
                                        //Snackbar.make(click, "Made a Knight", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        board.getBoard()[blackpos[0]][blackpos[1]]=new Knight(board.getBoard()[blackpos[0]][blackpos[1]].getColor());
                                        updateBoard(board.getBoard());
                                        popupWindow.dismiss();
                                    }
                                });

                                bbp.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    //method for handling what happens when you click Rock buttonImage.
                                    public void onClick(View v) {
                                        //Snackbar.make(click, "Made a Knight", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        board.getBoard()[blackpos[0]][blackpos[1]]=new Bishop(board.getBoard()[blackpos[0]][blackpos[1]].getColor());
                                        updateBoard(board.getBoard());
                                        popupWindow.dismiss();
                                    }
                                });

                                brp.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    //method for handling what happens when you click Rock buttonImage.
                                    public void onClick(View v) {
                                        //Snackbar.make(click, "Made a Knight", BaseTransientBottomBar.LENGTH_SHORT).show();
                                        board.getBoard()[blackpos[0]][blackpos[1]]=new Rook(board.getBoard()[blackpos[0]][blackpos[1]].getColor(),((Pawn) board.getBoard()[blackpos[0]][blackpos[1]]).getMoves());
                                        updateBoard(board.getBoard());
                                        popupWindow.dismiss();
                                    }
                                });



                            }




                        }
                        if (!turnOver) {
                            if (moveFrom != null)
                                theMove.setText(moveTo + " -> " + moveFrom + ". White's turn!");
                            turn = Turn.WHITE;
                            moves.push(copy);
                            moveTo = null;
                            moveFrom = null;
                        } else {
                            Snackbar.make(click, "Illegal move. Try again.", BaseTransientBottomBar.LENGTH_SHORT).show();
                            moveTo = null;
                            moveFrom = null;
                            theMove.setText("Black's turn");
                        }
                        updateBoard(board.getBoard());
                    }
                }

                if (board.isCheckmate(PieceSkeleton.color.BLACK)) {
                    gameInProgress = false;
                    theMove.setText("Checkmate - White Wins");
                    Button moveOn = (Button) findViewById(R.id.finish);
                    moveOn.setVisibility(gameInProgress ? View.GONE : View.VISIBLE);
                    return;
                }
                if (board.isCheckmate(PieceSkeleton.color.WHITE)) {
                    gameInProgress = false;
                    theMove.setText("Checkmate - Black Wins");
                    Button moveOn = (Button) findViewById(R.id.finish);
                    moveOn.setVisibility(gameInProgress ? View.GONE : View.VISIBLE);
                    return;
                }
                if (board.isInCheck(PieceSkeleton.color.BLACK, board.getKingPosition(PieceSkeleton.color.BLACK)))
                    theMove.setText("Check");

                updateBoard(board.getBoard());
                //reenable the undo button
                Button undoButton=(Button)findViewById(R.id.undoButton);
                undoButton.setEnabled(true);

                moveTo = null;
                moveFrom = null;
            }
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
        buttonMap.put("a1", R.id.button_a1);
        buttonMap.put("a2", R.id.button_a2);
        buttonMap.put("a3", R.id.button_a3);
        buttonMap.put("a4", R.id.button_a4);
        buttonMap.put("a5", R.id.button_a5);
        buttonMap.put("a6", R.id.button_a6);
        buttonMap.put("a7", R.id.button_a7);
        buttonMap.put("a8", R.id.button_a8);

        buttonMap.put("b1", R.id.button_b1);
        buttonMap.put("b2", R.id.button_b2);
        buttonMap.put("b3", R.id.button_b3);
        buttonMap.put("b4", R.id.button_b4);
        buttonMap.put("b5", R.id.button_b5);
        buttonMap.put("b6", R.id.button_b6);
        buttonMap.put("b7", R.id.button_b7);
        buttonMap.put("b8", R.id.button_b8);

        buttonMap.put("c1", R.id.button_c1);
        buttonMap.put("c2", R.id.button_c2);
        buttonMap.put("c3", R.id.button_c3);
        buttonMap.put("c4", R.id.button_c4);
        buttonMap.put("c5", R.id.button_c5);
        buttonMap.put("c6", R.id.button_c6);
        buttonMap.put("c7", R.id.button_c7);
        buttonMap.put("c8", R.id.button_c8);

        buttonMap.put("d1", R.id.button_d1);
        buttonMap.put("d2", R.id.button_d2);
        buttonMap.put("d3", R.id.button_d3);
        buttonMap.put("d4", R.id.button_d4);
        buttonMap.put("d5", R.id.button_d5);
        buttonMap.put("d6", R.id.button_d6);
        buttonMap.put("d7", R.id.button_d7);
        buttonMap.put("d8", R.id.button_d8);

        buttonMap.put("e1", R.id.button_e1);
        buttonMap.put("e2", R.id.button_e2);
        buttonMap.put("e3", R.id.button_e3);
        buttonMap.put("e4", R.id.button_e4);
        buttonMap.put("e5", R.id.button_e5);
        buttonMap.put("e6", R.id.button_e6);
        buttonMap.put("e7", R.id.button_e7);
        buttonMap.put("e8", R.id.button_e8);

        buttonMap.put("f1", R.id.button_f1);
        buttonMap.put("f2", R.id.button_f2);
        buttonMap.put("f3", R.id.button_f3);
        buttonMap.put("f4", R.id.button_f4);
        buttonMap.put("f5", R.id.button_f5);
        buttonMap.put("f6", R.id.button_f6);
        buttonMap.put("f7", R.id.button_f7);
        buttonMap.put("f8", R.id.button_f8);

        buttonMap.put("g1", R.id.button_g1);
        buttonMap.put("g2", R.id.button_g2);
        buttonMap.put("g3", R.id.button_g3);
        buttonMap.put("g4", R.id.button_g4);
        buttonMap.put("g5", R.id.button_g5);
        buttonMap.put("g6", R.id.button_g6);
        buttonMap.put("g7", R.id.button_g7);
        buttonMap.put("g8", R.id.button_g8);

        buttonMap.put("h1", R.id.button_h1);
        buttonMap.put("h2", R.id.button_h2);
        buttonMap.put("h3", R.id.button_h3);
        buttonMap.put("h4", R.id.button_h4);
        buttonMap.put("h5", R.id.button_h5);
        buttonMap.put("h6", R.id.button_h6);
        buttonMap.put("h7", R.id.button_h7);
        buttonMap.put("h8", R.id.button_h8);
    }
}