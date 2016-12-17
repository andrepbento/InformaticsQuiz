package models;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import interfaces.Constants;

/**
 * Created by andre
 */

public class MultiPlayerGameResult extends GameResult {

    private long multiPlayerGameResultID;
    private Map<PlayerData, Game> multiPlayerGameResultTable;

    public MultiPlayerGameResult(Date gameDate, int gameNQuestions, int gameDifficulty) {
        super(gameDate, gameNQuestions, gameDifficulty);
        multiPlayerGameResultID = new Date().getTime();
        /*
        multiPlayerGameResultTable = new TreeMap<>(new Comparator<Game>() {
            @Override
            public int compare(Game g1, Game g2) {
                return g1.getScore().compareTo(g2.getScore());
            }
        });
        */
    }

    public void addPlayerData(PlayerData playerData, Game game){
        multiPlayerGameResultTable.put(playerData, game);
    }

    public int getPlayersCount(){
        return multiPlayerGameResultTable.size();
    }

    /*
    public void orderTable() {
        Map<PlayerData, Game> temp = new HashMap<>();
        for(multiPlayerGameResultTable)
        multiPlayerGameResultTable
    }
    */

    public boolean save(Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(Constants.multiPlayerPath+"/"+String.valueOf(multiPlayerGameResultID),
                    Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.flush();
        } catch(IOException e) {
            return false;
        }
        return true;
    }

    public static List<MultiPlayerGameResult> load() {
        List<MultiPlayerGameResult> multiPlayerGameResultList = new ArrayList<>();
        try {
            final File folder = new File(Constants.multiPlayerPath);
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isFile()) {
                    FileInputStream fin = new FileInputStream(fileEntry);
                    ObjectInputStream bis = new ObjectInputStream(fin);
                    multiPlayerGameResultList.add((MultiPlayerGameResult) bis.readObject());
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return multiPlayerGameResultList;
    }
}
