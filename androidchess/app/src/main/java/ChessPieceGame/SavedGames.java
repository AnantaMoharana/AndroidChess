package ChessPieceGame;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class SavedGames implements Serializable {

    //to wipe save, just change string name of this
    private static final String storeFile = "gameInstance";

    static final long serialVersionUID = 1L;

    private final ArrayList<GameInstance> gameList;

    public SavedGames() {
        gameList = new ArrayList<>();
    }

    /***
     * Write to dat output file
     * @param list list class to write
     * @throws IOException if file is not found
     */
    public static void write(ArrayList<GameInstance> list, Context context) throws IOException {

        File dir = context.getDir("storedDirectory", Context.MODE_PRIVATE);
        File file = new File(dir, storeFile);

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(list);
    }
    /***
     * Read from dat output file
     * @return UserList class with arraylist filled
     * @throws IOException if file not found
     * @throws ClassNotFoundException if class not found
     */
    public static ArrayList<GameInstance> read(Context context) throws IOException, ClassNotFoundException {
        try {
            File dir = context.getDir("storedDirectory", Context.MODE_PRIVATE);
            File file = new File(dir, storeFile);

            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(file)
            );
            return (ArrayList<GameInstance>) ois.readObject();
        }
        catch (Exception e){
            return new ArrayList<GameInstance>();
        }
    }
}