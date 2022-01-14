package ChessPieceGame;

import java.util.ArrayList;

/***
 * Pawn class [implements PieceSkeleton]
 * @Author Trevor Scott
 * @Author Ananta Moharana
 */
public class Pawn implements PieceSkeleton {
    /***
     * Color of this specific instance of chess piece.
     */
    private final color color;
    /***
     * counts how many moves the pawn has made. If it is greater than one the opponent cant enpassant
     */
    int moves;
    /***
     * checks if the piece is in a positon where it can be taken via enpassant
     */
    boolean enpassant;

    /***
     * Pawn piece.
     * @param piece color of piece
     * @param moves number of moves piece has made
     * @param enpassant true if can attempt en passant
     */
    public Pawn(color piece, int moves,boolean enpassant){
        this.color = piece;
        this.moves=moves;
        this.enpassant=enpassant;
    }
    /***
     * toString() method
     * @return String piece
     */
    public String toString(){
        if (color == PieceSkeleton.color.WHITE){
            return "wp";
        } else {
            return "bp";
        }
    }

    /***
     * Gets all valid moves that a piece is able to make.
     * @param pos the position of the current piece.
     * @param board the current state of the game board.
     * @return list of valid moves this piece instance can make.
     */
    @Override
    public ArrayList<int[]> getAllValidMoves(int[] pos, PieceSkeleton[][] board) {
        ArrayList<int[]> list = new ArrayList<>();

        int row = pos[0];
        int col = pos[1];

        if (board[pos[0]][pos[1]].getColor() == PieceSkeleton.color.WHITE){
            //if the piece is at original spot, and no blockers, can move 1 or 2 spots
            if (row == 1 && board[row+1][col] == null){
                list.add(new int[]{row+1, col});
                if (board[row+2][col] == null){
                    list.add(new int[]{row+2, col});
                }
            } else if (row+1 <= 7 && board[row+1][col] == null) {
                list.add(new int[]{row + 1, col});
            }

            //capture
            if (row+1 <= 7 && col-1 >=0 && board[row+1][col-1] != null && board[row+1][col-1].getColor() != color){
                list.add(new int[]{row+1, col-1});
            }
            if (row+1 <= 7 && col+1 <= 7 && board[row+1][col+1] != null && board[row+1][col+1].getColor() != color){
                list.add(new int[]{row+1, col+1});
            }

        }
        else if (board[pos[0]][pos[1]].getColor() == PieceSkeleton.color.BLACK) {
            //if the piece is at original spot, and no blockers, can move 1 or 2 spots
            if (row == 6 && board[row-1][col] == null){
                list.add(new int[]{row-1, col});
                if (board[row-2][col] == null){
                    list.add(new int[]{row-2, col});
                }
            } else if (row-1 >= 0 && board[row-1][col] == null) {
                list.add(new int[]{row - 1, col});
            }

            //capture
            if (row-1 >= 0 && col-1 >=0 && board[row-1][col-1] != null && board[row-1][col-1].getColor() != color){
                list.add(new int[]{row-1, col-1});
            }
            if (row-1 >= 0 && col+1 <= 7 && board[row-1][col+1] != null && board[row-1][col+1].getColor() != color){
                list.add(new int[]{row-1, col+1});
            }
        }

        return list;
    }
    /***
     * Grabs the color of this piece.
     * @return enum color, either black or white.
     */
    public color getColor(){
        return color;
    }
    /***
     * Grabs the number of moves for this piece.
     * @return int moves.
     */
    public int getMoves(){
        return moves;
    }
    /***
     * Is this piece able to enpassant
     * @return true if possible
     */
    public boolean getEnpassant(){
        return enpassant;
    }
}