package models;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import interfaces.Constants;

/**
 * Created by andre
 */

public class MultiPlayerGameResult extends GameResult implements Serializable {
    static final long serialVersionUID = 1011L;

    private long multiPlayerGameResultID;
    private List<PlayerResult> multiPlayerGameResultTable;

    public MultiPlayerGameResult(Date gameDate, int gameNQuestions, int gameDifficulty) {
        super(gameDate, gameNQuestions, gameDifficulty);
        multiPlayerGameResultID = new Date().getTime();
        multiPlayerGameResultTable = new ArrayList<>();
    }

    public long getMultiPlayerGameResultID() {
        return this.multiPlayerGameResultID;
    }

    public void addPlayerInfo(PlayerData playerData, Game game){
        multiPlayerGameResultTable.add(new PlayerResult(playerData, game));
        orderResults();
    }

    public List<PlayerResult> getMultiPlayerGameResultTable() {
        return multiPlayerGameResultTable;
    }

    private void orderResults() {
        Collections.sort(multiPlayerGameResultTable, Collections.reverseOrder());
    }

    public boolean save(Context context) {
        String directoryName = Constants.multiPlayerPath;
        String fileName = multiPlayerGameResultID+".bin";

        File directory = new File(context.getFilesDir(), directoryName);
        if(!directory.exists())
            directory.mkdir();

        try{
            File file = new File(directory, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch(IOException e) {
            Log.e("MPGameResult", e.toString());
            return false;
        }
        return true;
    }

    public static List<MultiPlayerGameResult> loadAllData(Context context) {
        List<MultiPlayerGameResult> multiPlayerGameResultList = new ArrayList<>();

        String directoryName = Constants.multiPlayerPath;

        File directory = new File(context.getFilesDir(), directoryName);
        if(!directory.exists())
            return multiPlayerGameResultList;

        for(final File fileEntry : directory.listFiles())
            if(fileEntry.isFile()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(fileEntry);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    multiPlayerGameResultList.add((MultiPlayerGameResult) objectInputStream.readObject());
                    objectInputStream.close();
                    fileInputStream.close();
                } catch (FileNotFoundException e) {
                    Log.e("MPGR_loadAllData", e.toString());
                } catch (IOException e) {
                    Log.e("MPGR_loadAllData", e.toString());
                } catch (ClassNotFoundException e) {
                    Log.e("MPGR_loadAllData", e.toString());
                }
            }

        return multiPlayerGameResultList;
    }

    public static MultiPlayerGameResult loadData(Context context, long multiPlayerGameResultID) {
        MultiPlayerGameResult multiPlayerGameResult = null;

        String directoryName = Constants.multiPlayerPath;

        File directory = new File(context.getFilesDir(), directoryName);
        if(!directory.exists())
            return multiPlayerGameResult;

        for(final File fileEntry : directory.listFiles())
            if(fileEntry.isFile()) {
                String[] fileNameSplitted = fileEntry.getName().split("\\.");
                String fileName = fileNameSplitted[0];
                if(fileName.equals(String.valueOf(multiPlayerGameResultID))) {
                    try {
                        FileInputStream fileInputStream = new FileInputStream(fileEntry);
                        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                        multiPlayerGameResult = (MultiPlayerGameResult) objectInputStream.readObject();
                        objectInputStream.close();
                        fileInputStream.close();
                    } catch (FileNotFoundException e) {
                        Log.e("MPGR_loadAllData", e.toString());
                    } catch (IOException e) {
                        Log.e("MPGR_loadAllData", e.toString());
                    } catch (ClassNotFoundException e) {
                        Log.e("MPGR_loadAllData", e.toString());
                    }
                }
            }

        return multiPlayerGameResult;
    }

    public static boolean deleteAllData(Context context) {
        String directoryName = Constants.multiPlayerPath;

        File directory = new File(context.getFilesDir(), directoryName);
        if(!directory.exists())
            return false;

        for(final File fileEntry : directory.listFiles())
            if(fileEntry.isFile())
                fileEntry.delete();

        return true;
    }

    public static boolean deleteAllData(Context context, long minTime) {
        String directoryName = Constants.multiPlayerPath;

        File directory = new File(context.getFilesDir(), directoryName);
        if(!directory.exists())
            return false;

        for(final File fileEntry : directory.listFiles())
            if(fileEntry.isFile()) {
                String[] nameSplitted = fileEntry.getName().split("\\.");
                if (Long.parseLong(nameSplitted[0]) < minTime)
                    fileEntry.delete();
            }

        return true;
    }

    public class PlayerResult implements Serializable, Comparable {
        static final long serialVersionUID = 1010L;

        private PlayerData playerData;
        private Game game;

        public PlayerResult(PlayerData playerData, Game game) {
            this.playerData = playerData;
            this.game = game;
        }

        public PlayerData getPlayerData() {
            return playerData;
        }

        public Game getGame() {
            return game;
        }

        @Override
        public int compareTo(Object o) {
            PlayerResult playerResult = (PlayerResult) o;
            return this.getGame().getScore() - playerResult.getGame().getScore();
        }
    }
}
