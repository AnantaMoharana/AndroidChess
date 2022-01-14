package ChessPieceGame;

import java.io.Serializable;
import java.util.ArrayList;

/** Interface skeleton for all chess pieces.
 * @author Trevor Scott
 * @author Ananta Moharana
 */
public interface PieceSkeleton extends Serializable {
    /***
     * Color of this specific instance of chess piece.
     */
    enum color {BLACK, WHITE}

    /***
     * Gets all valid moves that a piece is able to make.
     * @param pos the position of the current piece.
     * @param board the current state of the game board.
     * @return list of valid moves this piece instance can make.
     */
    ArrayList<int[]> getAllValidMoves(int[] pos, PieceSkeleton[][] board);

    /***
     * Grabs the color of this piece.
     * @return enum color, either black or white.
     */
    color getColor();
}