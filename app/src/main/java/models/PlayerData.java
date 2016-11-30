package models;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import interfaces.PublicConstantValues;

/**
 * Created by andre
 */

public class PlayerData implements Serializable {

    private Bitmap photo;
    private String name;
    private int sexId;
    private String sex;
    private int age;
    private int ocupationId;
    private String ocupation;
    private int pontuation;
    private int nRightAnswers;
    private int nWrongAnswers;
    private int totalAnswers;
    private int singlePlayerGamesPlayed;
    private int winSinglePlayerGames;
    private int loseSinglePlayerGames;

    public PlayerData(Bitmap photo, String name, int sexId, String sex, int age, int ocupationId,
                      String ocupation) {
        this.photo = photo;
        this.name = name;
        this.sexId = sexId;
        this.sex = sex;
        this.age = age;
        this.ocupationId = ocupationId;
        this.ocupation = ocupation;
        this.pontuation = 0;
        this.nRightAnswers = 0;
        this.nWrongAnswers = 0;
        this.totalAnswers = 0;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public String getName() {
        return name;
    }

    public int getSexId() {
        return sexId;
    }

    public String getSex() {
        return sex;
    }

    public int getAge() {
        return age;
    }

    public int getOcupationId() {
        return ocupationId;
    }

    public String getOcupation() {
        return ocupation;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSexId(int sexId) {
        this.sexId = sexId;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setOcupationId(int ocupationId) {
        this.ocupationId = ocupationId;
    }

    public void setOcupation(String ocupation) {
        this.ocupation = ocupation;
    }

    public int getPontuation() {
        return pontuation;
    }

    public int getnRightAnswers() {
        return nRightAnswers;
    }

    public int getnWrongAnswers() {
        return nWrongAnswers;
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }

    public void setPontuation(int pontuation) {
        if(pontuation <= 0)
            this.pontuation = 0;
        else
            this.pontuation = pontuation;
    }

    public void setnRightAnswers(int nRightAnswers) {
        this.nRightAnswers = nRightAnswers;
    }

    public void setnWrongAnswers(int nWrongAnswers) {
        this.nWrongAnswers = nWrongAnswers;
    }

    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public void saveData(Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(PublicConstantValues.playerFileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(this);
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static PlayerData loadData(Context context) {
        PlayerData playerData = null;

        FileInputStream fis = null;
        try {
            fis = context.openFileInput(PublicConstantValues.playerFileName);
        } catch (FileNotFoundException e) {
            Log.e("MainMenuActivity", "FileNotFoundException " + e.getMessage());
            return playerData;
        }
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(fis);
            playerData = (PlayerData) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            Log.e("MainMenuActivity", "IOException " + e.getMessage());
            return playerData;
        } catch (ClassNotFoundException e) {
            Log.e("MainMenuActivity", "ClassNotFoundException " + e.getMessage());
            return playerData;
        }

        return playerData;
    }

}
