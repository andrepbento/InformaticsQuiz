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
import java.util.Date;
import java.util.List;

import interfaces.Constants;

/**
 * Created by andre
 */

public class SinglePlayerGameResult extends GameResult implements Serializable {
    static final long serialVersionUID = 1010L;

    private long singlePlayerGameResultID;
    private boolean gameResult;
    private int gameScore;
    private int nRightAnswers;
    private int nWrongAnswers;

    public SinglePlayerGameResult(Date gameDate, int gameDifficulty, boolean gameResult,
                                  int gameScore, int nRightAnswers, int nWrongAnswers) {
        super(gameDate, nRightAnswers + nWrongAnswers, gameDifficulty);
        this.singlePlayerGameResultID = gameDate.getTime();
        this.gameResult = gameResult;
        this.gameScore = gameScore;
        this.nRightAnswers = nRightAnswers;
        this.nWrongAnswers = nWrongAnswers;
    }

    public long getSinglePlayerGameResultID() {
        return singlePlayerGameResultID;
    }

    public boolean getGameResult() {
        return gameResult;
    }

    public int getGameScore() {
        return gameScore;
    }

    public int getnRightAnswers() {
        return nRightAnswers;
    }

    public int getnWrongAnswers() {
        return nWrongAnswers;
    }

    public double getpRightAnswers() { return Math.round((double)nRightAnswers / gameNQuestions * 100.0); }

    public double getpWrongAnswers() { return Math.round((double)nWrongAnswers / gameNQuestions * 100.0); }

    public boolean save(Context context) {
        String directoryName = Constants.singlePlayerPath;
        String fileName = singlePlayerGameResultID+".bin";

        File directory = new File(context.getFilesDir(), directoryName);
        if (!directory.exists())
            directory.mkdir();

        try{
            File file = new File(directory, fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e){
            Log.e("SPGameResult", e.toString());
            return false;
        }
        return true;
    }

    public static List<SinglePlayerGameResult> loadAllData(Context context) {
        List<SinglePlayerGameResult> singlePlayerGameResultList = new ArrayList<>();

        String directoryName = Constants.singlePlayerPath;

        File directory = new File(context.getFilesDir(), directoryName);
        if (!directory.exists())
            return singlePlayerGameResultList;

        for (final File fileEntry : directory.listFiles())
            if (fileEntry.isFile()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(fileEntry);
                    ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                    singlePlayerGameResultList.add((SinglePlayerGameResult) objectInputStream.readObject());
                    objectInputStream.close();
                    fileInputStream.close();
                } catch (FileNotFoundException e) {
                    Log.e("SPGR_loadAllData", e.toString());
                } catch (IOException e) {
                    Log.e("SPGR_loadAllData", e.toString());
                } catch (ClassNotFoundException e) {
                    Log.e("SPGR_loadAllData", e.toString());
                }
            }

        return singlePlayerGameResultList;
    }

    public static boolean deleteAllData(Context context) {
        String directoryName = Constants.singlePlayerPath;

        File directory = new File(context.getFilesDir(), directoryName);
        if (!directory.exists())
            return false;

        for (final File fileEntry : directory.listFiles())
            if (fileEntry.isFile())
                fileEntry.delete();

        return true;
    }

    public static boolean deleteAllData(Context context, long minTime) {
        String directoryName = Constants.singlePlayerPath;

        File directory = new File(context.getFilesDir(), directoryName);
        if (!directory.exists())
            return false;

        for (final File fileEntry : directory.listFiles())
            if (fileEntry.isFile()) {
                String[] nameSplitted = fileEntry.getName().split("\\.");
                if (Long.parseLong(nameSplitted[0]) < minTime)
                    fileEntry.delete();
            }

        return true;
    }
}
