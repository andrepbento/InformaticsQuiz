package models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import interfaces.Constants;

/**
 * Created by andre
 */
public class PlayerData implements Serializable {
    static final long serialVersionUID = 1010L;

    private long playerID;
    private byte[] photoData;
    private String name;
    private int sexId;
    private String sex;
    private int age;
    private int ocupation;
    private int singlePlayerPontuation;
    private int multiPlayerPontuation;
    private int totalAnswers;
    private int nRightAnswers;

    public PlayerData(Bitmap photo, String name, int sexId, String sex, int age, int ocupation) {
        this.playerID = new Date().getTime();
        setPhoto(photo);
        this.name = name;
        this.sexId = sexId;
        this.sex = sex;
        this.age = age;
        this.ocupation = ocupation;
        this.singlePlayerPontuation = 0;
        this.multiPlayerPontuation = 0;
        this.nRightAnswers = 0;
        this.totalAnswers = 0;
    }

    @Override
    public boolean equals(Object obj) {
        PlayerData playerData = (PlayerData) obj;
        if(this.playerID == playerData.getPlayerID())
            return true;
        return false;
    }

    public long getPlayerID() {
        return this.playerID;
    }

    public Bitmap getPhoto() {
        return BitmapFactory.decodeByteArray(photoData, 0, photoData.length);
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

    public int getOcupation() {
        return ocupation;
    }

    public void setPhoto(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        photoData = stream.toByteArray();
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

    public void setOcupation(int ocupation) {
        this.ocupation = ocupation;
    }

    public int getSinglePlayerPontuation() {
        return singlePlayerPontuation;
    }

    public void setSinglePlayerPontuation(int singlePlayerPontuation) {
        if(singlePlayerPontuation <= 0)
            this.singlePlayerPontuation = 0;
        else
            this.singlePlayerPontuation = singlePlayerPontuation;
    }

    public int getMultiPlayerPontuation() {
        return  multiPlayerPontuation;
    }

    public void setMultiPlayerPontuation(int multiPlayerPontuation) {
        if(singlePlayerPontuation <= 0)
            this.multiPlayerPontuation = 0;
        else
            this.multiPlayerPontuation = multiPlayerPontuation;
    }

    public int getnRightAnswers() {
        return nRightAnswers;
    }

    public void setnRightAnswers(int nRightAnswers) {
        this.nRightAnswers = nRightAnswers;
    }

    public int getnWrongAnswers() {
        return totalAnswers - nRightAnswers;
    }

    public int getTotalAnswers() {
        return totalAnswers;
    }

    public void setTotalAnswers(int totalAnswers) {
        this.totalAnswers = totalAnswers;
    }

    public void saveData(Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(Constants.playerFileName, Context.MODE_PRIVATE);
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
            fis = context.openFileInput(Constants.playerFileName);
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
