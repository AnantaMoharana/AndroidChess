package ChessBoardGame;

import android.view.View;

import ChessPieceGame.PieceSkeleton;
import ChessPieceGame.Rook;
import ChessPieceGame.King;
import ChessPieceGame.Bishop;
import ChessPieceGame.Queen;
import ChessPieceGame.Knight;
import ChessPieceGame.Pawn;
import ChessPieceGame.PieceSkeleton.color;

import java.util.ArrayList;
import java.util.Arrays;

/** Chess Board object to be used by the main driver.
 * @author Trevor Scott
 * @author Ananta Moharana
 */
public class ChessBoard {

    /***
     * Size of the board - constant.
     */
    private final int BOARD_SIZE = 8;

    /***
     * Game board to be used for chess.
     */
    private final PieceSkeleton[][] board = new PieceSkeleton[BOARD_SIZE][BOARD_SIZE];

    /***
     * The main game board.
     */
    public ChessBoard(){
        populateBoard();
    }

    private void populateBoard(){
        board[0][0] = new Rook(color.WHITE,0);
        board[0][7] = new Rook(color.WHITE,0);

        board[0][1] = new Knight(color.WHITE);
        board[0][6] = new Knight(color.WHITE);

        board[0][2] = new Bishop(color.WHITE);
        board[0][5] = new Bishop(color.WHITE);

        board[0][3] = new Queen(color.WHITE);
        board[0][4] = new King(color.WHITE,0);

        for (int i = 0; i < 8; i++)
            board[1][i] = new Pawn(color.WHITE,0,false);

        board[7][0] = new Rook(color.BLACK,0);
        board[7][7] = new Rook(color.BLACK,0);

        board[7][1] = new Knight(color.BLACK);
        board[7][6] = new Knight(color.BLACK);

        board[7][2] = new Bishop(color.BLACK);
        board[7][5] = new Bishop(color.BLACK);

        board[7][3] = new Queen(color.BLACK);
        board[7][4] = new King(color.BLACK,0);

        for (int i = 0; i < 8; i++)
            board[6][i] = new Pawn(color.BLACK,0,false);
    }

    /***
     * Returns a copy of the board.
     * @return object 2D-array of the board.
     */
    public PieceSkeleton[][] getBoard(){
        return board;
    }

    /***
     * Given a string position (ie "a3") grabs the status of the square.
     * @param pos Current string position to look at.
     * @return true if empty, false if occupied.
     */
    public boolean positionIsEmpty(String pos){
        int[] coords = getArrayPositionFromString(pos);
        return (board[coords[0]][coords[1]] == null);
    }

    /***
     * Given string position (ie "a3"), grabs array coordinates. parse "H8" to -> [7, 7], etc.
     * @param pos Current string position to look at.
     * @return int tuple array of equivalent position.
     */
    public int[] getArrayPositionFromString(String pos){
        int col = Character.toLowerCase(pos.charAt(0)) - 'a';
        int row = Integer.parseInt(""+pos.charAt(1))-1;

        if (row < 0) row = 0;
        return new int[]{row, col};
    }

    /***
     * Given a position on the board, this method returns the color of the piece occupying said position.
     * @param pos Current string position to look at.
     * @return enum color of the piece occupying the space.
     */
    public color getColorFromPosition(String pos){
        int[] coords = getArrayPositionFromString(pos);
        return board[coords[0]][coords[1]].getColor();
    }

    /***
     * Given 2 positions on the board, will attempt to move piece from pos1 to pos2
     * @param moveFrom pos1 - the position of the piece attempting to move
     * @param moveTo pos2 - the position the piece in question wants to move to
     * @param options For pawn promotion purposes, this string is only used for options at end of string.
     * @param player The color of the piece attempting the move.
     * @return true if successful, false if failure.
     */
    public boolean movePieceToPos(String moveFrom, String moveTo, String options, color player, View click){
        PieceSkeleton rightPiece=null;
        PieceSkeleton leftPiece=null;
        for(int i=0; i<BOARD_SIZE; i++){
            for (int j=0; j<BOARD_SIZE; j++){
                if(board[i][j] instanceof Pawn && board[i][j].getColor()==player && ((Pawn) board[i][j]).getEnpassant()==true){
                    if(j+1<=7){
                        rightPiece=board[i][j+1];

                    }
                    if(j-1>=0){
                        leftPiece=board[i][j-1];

                    }

                    if(leftPiece instanceof Pawn || rightPiece instanceof Pawn){
                        board[i][j]=new Pawn(player,((Pawn) board[i][j]).getMoves(),false);

                    }

                }

            }
        }
        int[] coordsFrom = getArrayPositionFromString(moveFrom);
        int[] coordsTo = getArrayPositionFromString(moveTo);


        PieceSkeleton pieceCheck=board[coordsFrom[0]][coordsFrom[1]];

        if(pieceCheck instanceof King && (Arrays.equals(coordsTo,new int[]{7,6} ) || Arrays.equals(coordsTo,new int[]{7,2} ) || Arrays.equals(coordsTo,new int[]{0,6} ) || Arrays.equals(coordsTo,new int[]{0,2} ))){ //use search
            //System.out.println("we made it this far");
            //next check if it is possible for the player to castle
            boolean canCastle=castleCheck(moveFrom,moveTo,player); //edit in post check
            if (!canCastle){
                //System.out.println("Check Castle Done");
                //System.out.println("Illegal move, try again.");
                return false;
            }else{
                //System.out.println("We are going to castle");
                //move the king to the castle position

                PieceSkeleton pieceToRemove = board[coordsTo[0]][coordsTo[1]];
                board[coordsTo[0]][coordsTo[1]] = board[coordsFrom[0]][coordsFrom[1]];
                board[coordsFrom[0]][coordsFrom[1]] = null;

                //put the rook in its appropriate place this depends on whether we are performing a long castle of a short castle

                if(coordsTo[1]==6){
                    //short castle
                    board[coordsTo[0]][coordsTo[1]-1] = board[coordsTo[0]][7];
                    board[coordsTo[0]][7]=null;
                    //System.out.println("Short Castle");
                }else if(coordsTo[1]==2){
                    //long castle
                    board[coordsTo[0]][coordsTo[1]+1] = board[coordsTo[0]][0];
                    board[coordsTo[0]][0]=null;

                }
                return true;
            }

        }else if(pieceCheck instanceof  Pawn && attemptEnpassant(moveFrom,moveTo,player)){
            if(player==color.WHITE){
                if(coordsFrom[0]!=4){
                    System.out.println("Illegal move, try again");
                    return false;
                }
                //double check
                if(!(board[coordsTo[0]-1][coordsTo[1]] instanceof Pawn)){
                    System.out.println("Illegal move, try again");
                    return false;
                }else if((board[coordsTo[0]-1][coordsTo[1]] instanceof Pawn)) {
                    if (((Pawn) board[coordsTo[0]-1][coordsTo[1]]).getMoves() != 1 || (((Pawn) board[coordsTo[0]-1][coordsTo[1]]).getEnpassant())==false) {
                        System.out.println("Illegal move, try again");
                        return false;
                    }else{
                        //perform enpassant

                        PieceSkeleton doEnpassant = board[coordsTo[0]][coordsTo[1]];
                        board[coordsTo[0]][coordsTo[1]] = board[coordsFrom[0]][coordsFrom[1]];
                        board[coordsTo[0]-1][coordsTo[1]]=null;
                        board[coordsFrom[0]][coordsFrom[1]] = null;
                        return true;

                    }

                }
            }else if(player==color.BLACK){
                if(coordsFrom[0]!=3){
                    System.out.println("Illegal move, try again");
                    return false;
                }
                //double check
                if(!(board[coordsTo[0]+1][coordsTo[1]] instanceof Pawn)){
                    System.out.println("Illegal move, try again");
                    return false;
                }else if((board[coordsTo[0]+1][coordsTo[1]] instanceof Pawn)) {
                    if (((Pawn) board[coordsTo[0]+1][coordsTo[1]]).getMoves() != 1 || (((Pawn) board[coordsTo[0]+1][coordsTo[1]]).getEnpassant())==false) {
                        System.out.println("Illegal move, try again");
                        return false;
                    }else{
                        //perform enpassant

                        PieceSkeleton doEnpassant = board[coordsTo[0]][coordsTo[1]];
                        board[coordsTo[0]][coordsTo[1]] = board[coordsFrom[0]][coordsFrom[1]];
                        board[coordsTo[0]+1][coordsTo[1]]=null;
                        board[coordsFrom[0]][coordsFrom[1]] = null;
                        return true;

                    }

                }
            }
        }
        ArrayList<int[]> validMoves = board[coordsFrom[0]][coordsFrom[1]].getAllValidMoves(coordsFrom, board);

        if (search(validMoves, coordsTo)){
            //Good to go. make the move.
            PieceSkeleton pieceToRemove = board[coordsTo[0]][coordsTo[1]];
            board[coordsTo[0]][coordsTo[1]] = board[coordsFrom[0]][coordsFrom[1]];
            board[coordsFrom[0]][coordsFrom[1]] = null;

            //Unless it puts the player in check, then we cannot allow that.
            int [] kingPosition=getKingPosition(player); // get the postion of the king
            if (isInCheck(player, kingPosition)){
                board[coordsFrom[0]][coordsFrom[1]] = board[coordsTo[0]][coordsTo[1]];
                board[coordsTo[0]][coordsTo[1]] = pieceToRemove;
                System.out.println("Illegal move, try again.");
                return false;
            }
            PieceSkeleton piece = board[coordsTo[0]][coordsTo[1]];
            //PROMOTION OF PAWN
            if (piece instanceof Pawn && ((coordsTo[0] == 7 && piece.getColor() == color.WHITE) || (coordsTo[0] == 0 && piece.getColor() == color.BLACK))){

                int priorMoves=((Pawn) piece).getMoves();
                switch (options) {
                    case "N":
                        board[coordsTo[0]][coordsTo[1]] = new Knight(piece.getColor());
                        break;
                    case "R":
                        board[coordsTo[0]][coordsTo[1]] = new Rook(piece.getColor(),priorMoves);
                        break;
                    case "B":
                        board[coordsTo[0]][coordsTo[1]] = new Bishop(piece.getColor());
                        break;
                    case "P":
                        break;
                    default:
                        board[coordsTo[0]][coordsTo[1]] = new Queen(piece.getColor());
                        break;
                }
                piece = board[coordsTo[0]][coordsTo[1]];
            }
            //update how many moves have been made if the piece is a king, rook, or pawn

            if(piece instanceof Pawn) {
                int priorMoves=((Pawn) piece).getMoves();
                board[coordsTo[0]][coordsTo[1]]=new Pawn(piece.getColor(),++priorMoves,false);

            }else if(piece instanceof King){
                int priorMoves=((King) piece).getMoves();
                board[coordsTo[0]][coordsTo[1]]=new King(piece.getColor(),priorMoves+1);

            }else if(piece instanceof Rook){
                int priorMoves=((Rook) piece).getMoves();
                board[coordsTo[0]][coordsTo[1]]=new Rook(piece.getColor(),priorMoves+1);
            }
            //check if the pawn has moved into a position that would allow it to be taken in enpassant
            PieceSkeleton pawnCheck=board[coordsTo[0]][coordsTo[1]];
            if(pawnCheck instanceof Pawn && ((Pawn) pawnCheck).getMoves()==1){
                PieceSkeleton leftCheck=null;
                PieceSkeleton rightCheck=null;
                int col=coordsTo[1];
                if(player==color.WHITE){
                    if(coordsTo[0]==3){  //only row where an enpassant can occur
                        if(col+1<=7){
                            rightCheck=board[coordsTo[0]][col+1];

                        }
                        if(col-1>=0){
                            leftCheck=board[coordsTo[0]][col-1];

                        }

                    }

                }else if(player==color.BLACK){
                    if(coordsTo[0]==4){ //only row where an enpassant can occur

                        if(col+1<=7){
                            rightCheck=board[coordsTo[0]][col+1];

                        }
                        if(col-1>=0){
                            leftCheck=board[coordsTo[0]][col-1];

                        }

                    }

                }
                if(leftCheck instanceof Pawn || rightCheck instanceof Pawn){
                    board[coordsTo[0]][coordsTo[1]]=new Pawn(pawnCheck.getColor(),((Pawn) pawnCheck).getMoves(),true);

                }

            }
            return true;
        } else {
            System.out.println("Illegal move, try again.");
            return false;
        }
    }

    /***
     * @param starting The starting position of the piece in question
     * @param destination The final destination of the piece in question
     * @param player color of player to check castling validity
     * @return True if the player is trying to castle and if castling in possible
     */
    //this might be completely useless for all I know
    public boolean castleCheck(String starting,String destination, PieceSkeleton.color player){ //check for logic errors
        int[] coordsFrom = getArrayPositionFromString(starting);
        int[] coordsTo = getArrayPositionFromString(destination);
        //we can not castle if our king is in check
        if(isInCheck(player,coordsFrom)){
            //System.out.print("Illegal move, try again.");
            return false;
        } //we can not castle if the King has moved
        if(((King) board[coordsFrom[0]][coordsFrom[1]]).getMoves()!=0){
            //System.out.print("Illegal move, try again.");
            return false;
        }
        PieceSkeleton rookCheck;
        if(coordsTo[1]==6){//we are performing a short castle
            //checks to make sure we are not castling into a check, through
            rookCheck=board[coordsFrom[0]][7];
            if((!(rookCheck instanceof Rook) || ((Rook) rookCheck).getMoves()!=0) || isInCheck(player, new int[]{coordsFrom[0],coordsFrom[1]+1}) || isInCheck(player, new int[]{coordsFrom[0],coordsFrom[1]+2})){
                //System.out.print("Illegal move, try again.");
                return false;
            }
            //make sure spaces are empty before
            if(board[coordsFrom[0]][6]!=null || board[coordsFrom[0]][5]!=null){
                return false;

            }

        }else if(coordsTo[1]==2) { //we are performing a long castle
            rookCheck=board[coordsFrom[0]][0];
            if((!(rookCheck instanceof Rook) || ((Rook) rookCheck).getMoves()!=0) || isInCheck(player, new int[]{coordsFrom[0],coordsFrom[1]-1}) || isInCheck(player, new int[]{coordsFrom[0],coordsFrom[1]-2})){
                //System.out.print("Illegal move, try again.");
                return false;
            }

            if(board[coordsFrom[0]][1]!=null || board[coordsFrom[0]][2]!=null || board[coordsFrom[0]][3]!=null){
                return false;

            }
        }
        return true;
    }
    /***
     *
     * @param starting The starting position of the piece in question
     * @param destination The destination of the piece in question
     * @param color color of enpassant to attempt
     * @return True if the piece is attempting an enpassant
     * It doesn't check if the move is valid, that is done elsewhere
     */
    public boolean attemptEnpassant(String starting, String destination,PieceSkeleton.color color){
        int[] coordsFrom = getArrayPositionFromString(starting);
        int[] coordsTo = getArrayPositionFromString(destination);

        ArrayList<int[]> diagonalSpaces = new ArrayList<>();
        // first indication of enpassant is trying to move diagonally on to a null space
        if(board[coordsFrom[0]][coordsFrom[1]].getColor()== PieceSkeleton.color.WHITE){
            if (coordsFrom[0]+1 <= 7 && coordsFrom[1]-1 >=0 && board[coordsFrom[0]+1][coordsFrom[1]-1] == null ){
                diagonalSpaces.add(new int[]{coordsFrom[0]+1, coordsFrom[1]-1});
            }
            if (coordsFrom[0]+1 <= 7 && coordsFrom[1]+1 <= 7 && board[coordsFrom[0]+1][coordsFrom[1]+1] == null ){
                diagonalSpaces.add(new int[]{coordsFrom[0]+1, coordsFrom[1]+1});
            }

        }else if(board[coordsFrom[0]][coordsFrom[1]].getColor()== PieceSkeleton.color.BLACK){
            if (coordsFrom[0]-1 >= 0 && coordsFrom[1]-1 >=0 && board[coordsFrom[0]-1][coordsFrom[1]-1] == null){
                diagonalSpaces.add(new int[]{coordsFrom[0]-1, coordsFrom[1]-1});
            }
            if (coordsFrom[0]-1 >= 0 && coordsFrom[1]+1 <= 7 && board[coordsFrom[0]-1][coordsFrom[1]+1] == null ){
                diagonalSpaces.add(new int[]{coordsFrom[0]-1, coordsFrom[1]+1});
            }

        }
        if (!search(diagonalSpaces,coordsTo)){
            return false;
        }else{
            return true;
        }
    }
    /***
     *
     * @param color the color of the king we are trying to find
     * @return the position ot the king.
     */
    public int [] getKingPosition(PieceSkeleton.color color){
        int[] kingPos = null;
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++){
                if (board[i][j] instanceof King && board[i][j].getColor() == color){
                    kingPos = new int[]{i, j};
                    break;
                }
            }
        }
        return kingPos;
    }

    /***
     * Given a color of player, tells if they are currently in check
     * @param color Color of player in question
     * @param kingPos coords of the king in question
     * @return true if their king is in check, false if not.
     */
    public boolean isInCheck(PieceSkeleton.color color,int [] kingPos){
        //Check all opposing pieces against it (see if it can find the king)
        for (int i = 0; i < BOARD_SIZE; i++){
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null && board[i][j].getColor() != color){
                    if (search(board[i][j].getAllValidMoves(new int[]{i, j}, board), kingPos)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /***
     *
     * @param color color of the current player
     * @return true if the player is in checkmate an false otherwise
     * the reason that this doesn't take in the postion of the king like isInCheck is becasue isInCheck is also used
     * to check for potential checks when castling.
     */
    public boolean isCheckmate(PieceSkeleton.color color){
        int[] kingPostion=getKingPosition(color);
        if (!isInCheck(color,kingPostion)) return false;

        //simulate every move and then just return false if NONE will allow player to escape check
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != null && board[i][j].getColor() == color) {

                    ArrayList<int[]> validMoves = board[i][j].getAllValidMoves(new int[]{i, j}, board);

                    for (int[] validMove: validMoves){
                        PieceSkeleton tmp = board[validMove[0]][validMove[1]];
                        board[validMove[0]][validMove[1]] = board[i][j];//i think i know what is going wrong
                        //need to check that i,j are not the king
                        board[i][j] = null;
                        if(Arrays.equals(kingPostion,new int[]{i,j})){
                            if (!isInCheck(color,new int[]{validMove[0],validMove[1]})) { //check tomorrow with trev
                                board[i][j] = board[validMove[0]][validMove[1]];
                                board[validMove[0]][validMove[1]] = tmp;
                                return false;
                            } else {
                                board[i][j] = board[validMove[0]][validMove[1]];
                                board[validMove[0]][validMove[1]] = tmp;
                            }

                        }else{
                            if (!isInCheck(color,kingPostion)) {
                                board[i][j] = board[validMove[0]][validMove[1]];
                                board[validMove[0]][validMove[1]] = tmp;
                                return false;
                            } else {
                                board[i][j] = board[validMove[0]][validMove[1]];
                                board[validMove[0]][validMove[1]] = tmp;
                            }

                        }

                    }
                }
            }
        }
        return true;
    }

    /***
     * Checks to see if the move the player is trying to make is within the set of valid moves
     * @param moves potential moves the piece can make
     * @param coords the spot the user is trying to move
     * @return True if move is in the set of valid moves False otherwise
     *
     */
    private boolean search(ArrayList<int[]> moves, int[] coords){
        for (int[] move : moves) {
            if (move[0] == coords[0] && move[1] == coords[1])
                return true;
        }
        return false;
    }

    /***
     * Displays the current board correctly formatted to System.out.println()
     */
    public void displayBoard(){
        for (int i = BOARD_SIZE-1; i >= 0; i--){
            for (int j = 0; j < BOARD_SIZE; j++){
                if (board[i][j] != null) {
                    System.out.print(board[i][j].toString() + " ");
                } else {
                    if ((i+j)%2 == 0)
                        System.out.print("## ");
                    else
                        System.out.print("   ");
                }
            }
            System.out.println(i+1);
        }
        System.out.print(" ");
        for (int i = 0; i < BOARD_SIZE; i++){
            System.out.print(Character.toChars('a' + i));
            if (i == BOARD_SIZE-1) break;
            System.out.print("  ");
        }
    }
}