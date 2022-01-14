package ChessPieceGame;

import java.util.ArrayList;

/***
 * Queen class [implements PieceSkeleton]
 * @Author Trevor Scott
 * @Author Ananta Moharana
 */
public class Queen implements PieceSkeleton{
    /***
     * Color of this specific instance of chess piece.
     */
    private final color color;

    /***
     * Queen piece.
     * @param piece color of piece.
     */
    public Queen(color piece) {
        this.color = piece;
    }

    /***
     * toString() method
     * @return String piece
     */
    public String toString(){
        if (color == PieceSkeleton.color.WHITE){
            return "wQ";
        } else {
            return "bQ";
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

        int tmpRow = row, tmpCol = col;

        while (tmpRow -1 >= 0) {
            if (board[tmpRow-1][col] != null){
                if (board[tmpRow-1][col].getColor() != color)
                    list.add(new int[]{tmpRow - 1, col});
                break;
            }
            list.add(new int[]{tmpRow - 1, col});
            tmpRow--;
        }
        tmpRow = row;
        while (tmpRow+1 <= 7) {
            if (board[tmpRow+1][col] != null){
                if (board[tmpRow+1][col].getColor() != color)
                    list.add(new int[]{tmpRow + 1, col});
                break;
            }
            list.add(new int[]{tmpRow + 1, col});
            tmpRow++;
        }
        while (tmpCol-1 >= 0) {
            if (board[row][tmpCol-1] != null){
                if (board[row][tmpCol-1].getColor() != color)
                    list.add(new int[]{row, tmpCol-1});
                break;
            }
            list.add(new int[]{row, tmpCol - 1});
            tmpCol--;
        }
        tmpCol = col;
        while (tmpCol+1 <= 7) {
            if (board[row][tmpCol+1] != null){
                if (board[row][tmpCol+1].getColor() != color)
                    list.add(new int[]{row, tmpCol+1});
                break;
            }
            list.add(new int[]{row, tmpCol + 1});
            tmpCol++;
        }
        tmpRow = row;
        tmpCol = col;
        while (tmpRow-1 >= 0 && tmpCol-1 >= 0) {
            if (board[tmpRow-1][tmpCol-1] != null){
                if (board[tmpRow-1][tmpCol-1].getColor() != color)
                    list.add(new int[]{tmpRow-1, tmpCol-1});
                break;
            }
            list.add(new int[]{tmpRow - 1, tmpCol - 1});
            tmpRow--;
            tmpCol--;
        }
        tmpRow = row;
        tmpCol = col;
        while (tmpRow+1 <= 7 && tmpCol+1 <= 7) {
            if (board[tmpRow+1][tmpCol+1] != null){
                if (board[tmpRow+1][tmpCol+1].getColor() != color)
                    list.add(new int[]{tmpRow+1, tmpCol+1});
                break;
            }
            list.add(new int[]{tmpRow + 1, tmpCol + 1});
            tmpRow++;
            tmpCol++;
        }
        tmpRow = row;
        tmpCol = col;
        while (tmpRow-1 >= 0 && tmpCol+1 <= 7) {
            if (board[tmpRow-1][tmpCol+1] != null){
                if (board[tmpRow-1][tmpCol+1].getColor() != color)
                    list.add(new int[]{tmpRow-1, tmpCol+1});
                break;
            }
            list.add(new int[]{tmpRow - 1, tmpCol + 1});
            tmpRow--;
            tmpCol++;
        }
        tmpRow = row;
        tmpCol = col;
        while (tmpRow+1 <= 7 && tmpCol-1 >= 0) {
            if (board[tmpRow+1][tmpCol-1] != null){
                if (board[tmpRow+1][tmpCol-1].getColor() != color)
                    list.add(new int[]{tmpRow+1, tmpCol-1});
                break;
            }
            list.add(new int[]{tmpRow + 1, tmpCol - 1});
            tmpRow++;
            tmpCol--;
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
}
