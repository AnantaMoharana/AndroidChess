package ChessPieceGame;

import java.util.ArrayList;


/***
 * King class [implements PieceSkeleton]
 * @Author Trevor Scott
 * @Author Ananta Moharana
 */
public class King implements PieceSkeleton {
    /***
     * Color of this specific instance of chess piece.
     */
    private final color color;
    /***
     * Keeps track of whether or not the rook has moved, used in castle
     */
    int moves;

    /***
     * King class.
     * @param piece color of piece
     * @param moves moves this piece made
     */
    public King(color piece, int moves) {
        this.color = piece;
        this.moves=moves;
    }

    /***
     * toString() method
     * @return String piece
     */
    public String toString(){
        if (color == PieceSkeleton.color.WHITE){
            return "wK";
        } else {
            return "bK";
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

        if (row-1 >= 0)
            list.add(new int[]{row-1, col});
        if (row+1 <= 7)
            list.add(new int[]{row+1, col});
        if (col-1 >= 0)
            list.add(new int[]{row, col-1});
        if (col+1 <= 7)
            list.add(new int[]{row, col+1});
        if (row-1 >= 0 && col-1 >= 0)
            list.add(new int[]{row-1, col-1});
        if (row+1 <= 7 && col+1 <= 7)
            list.add(new int[]{row+1, col+1});
        if (row-1 >= 0 && col+1 <= 7)
            list.add(new int[]{row-1, col+1});
        if (row+1 <= 7 && col-1 >= 0)
            list.add(new int[]{row+1, col-1});

        //cant go to a space with one of your current pieces there.
        list.removeIf(coords -> board[coords[0]][coords[1]] != null && board[coords[0]][coords[1]].getColor() == this.color);

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
}

