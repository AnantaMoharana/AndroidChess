package ChessPieceGame;

import java.io.Serializable;
import java.util.ArrayList;

public class GameInstance implements Serializable {
    public String date;
    public String name;
    public ArrayList<PieceSkeleton[][]> board;

    public GameInstance(String date, String name, ArrayList<PieceSkeleton[][]> board){
        this.date = date;
        this.name = name;
        this.board = board;
    }
}
