package data;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.andre.informaticsquiz.PublicConstantValues;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by andre on 09/11/2016.
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

}
