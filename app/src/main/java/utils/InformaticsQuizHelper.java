package utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import models.Question;

/**
 * Created by andre
 */

public class InformaticsQuizHelper extends SQLiteOpenHelper {
    private static String DB_PATH = "";
    private static String DB_NAME = "informaticsQuiz.db";
    private static int DB_VERSION = 1;

    private static String TABLE = "";
    private static String QUESTIONS_PT_TABLE = "Questions_PT";
    private static String QUESTIONS_EN_TABLE = "Questions_EN";
    private static String DIFFICULTY_COLUMN = "Difficulty";
    private static String EASY_DIFFICULTY = "1";
    private static String MODERATE_DIFFICULTY = "2";
    private static String HARD_DIFFICULTY = "3";

    private final Context context;
    private SQLiteDatabase db;

    public InformaticsQuizHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";

        this.context = context;
    }

    public void create() {
        boolean dbExist = checkDataBase();

        if(dbExist) {
            // Não fazer nada, a base de dados já existe
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String path = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if(checkDB != null)
            checkDB.close();

        return  checkDB != null ? true : false;
    }

    public void copyDataBase() throws IOException {
        InputStream myInput = context.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while((length = myInput.read(buffer)) > 0)
            myOutput.write(buffer, 0, length);

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public boolean open() {
        try {
            String myPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            Locale deviceLocale = context.getResources().getConfiguration().locale;
            if(deviceLocale.equals(Locale.US) || deviceLocale.equals(Locale.ENGLISH)
                    || deviceLocale.equals(Locale.CANADA) || deviceLocale.equals(Locale.UK))
                TABLE = QUESTIONS_EN_TABLE;
            else
                TABLE = QUESTIONS_PT_TABLE;

            return true;
        } catch (SQLiteException sqle) {
            db = null;
            return false;
        }
    }

    @Override
    public synchronized void close() {
        if(db != null)
            super.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private List<Question> getQuestions(String query_arg) {
        List<Question> questions = new ArrayList<>();

        try {
            String query = query_arg;
            SQLiteDatabase db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            Cursor cursor = db.rawQuery(query, null);

            if(cursor == null) {
                Log.e("DB Cursor", "ERROR");
                return null;
            }

            while(cursor.moveToNext()) {
                int questionId = Integer.parseInt(cursor.getString(0));
                String questionDesc = cursor.getString(1);
                String answerA = cursor.getString(2);
                String answerB = cursor.getString(3);
                String answerC = cursor.getString(4);
                String answerD = cursor.getString(5);
                int rightAnswer = Integer.parseInt(cursor.getString(6));
                int questionDif = Integer.parseInt(cursor.getString(7));

                Question question = new Question(questionId, questionDesc, answerA,
                        answerB, answerC, answerD, rightAnswer, questionDif);

                questions.add(question);
            }
        } catch (Exception e) {
            Log.e("InfQuizHelper", e.getMessage());
        }

        return questions;
    }

    public List<Question> getAllQuestions() {
        return getQuestions("select * from " + TABLE + ";");
    }

    public List<Question> getEasyQuestions() {
        return getQuestions("select * from " + TABLE
                + " where " + DIFFICULTY_COLUMN + " = " + EASY_DIFFICULTY + ";");
    }

    public List<Question> getModerateQuestions() {
        return getQuestions("select * from " + TABLE
                + " where " + DIFFICULTY_COLUMN + " = " + MODERATE_DIFFICULTY + ";");
    }

    public List<Question> getHardQuestions() {
        return getQuestions("select * from " + TABLE
                + " where " + DIFFICULTY_COLUMN + " = " + HARD_DIFFICULTY + ";");
    }
}
