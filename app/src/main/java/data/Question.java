package data;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by andre on 27/10/2016.
 */

public class Question implements Serializable {

    private int questionId;
    private String questionDesc;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private int rightAnswer;
    private String questionDif;

    public Question(int questionId, String questionDesc, String answerA, String answerB,
                    String answerC, String answerD, int rightAnswer, String questionDif) {
        this.questionId = questionId;
        this.questionDesc = questionDesc;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.rightAnswer = rightAnswer;
        this.questionDif = questionDif;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestionDesc() {
        return questionDesc;
    }

    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(int rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getQuestionDif() {
        return questionDif;
    }

    public Integer getQuestionDifInteger() {
        Integer ret = 0;
        try {
            ret = Integer.parseInt(questionDif);
        } catch (NumberFormatException e) {
            Log.e("Question", "NumberFormatException");
        }
        return ret;
    }

    public void setQuestionDif(String questionDif) {
        this.questionDif = questionDif;
    }

    public String getRightAnswerLeter() {
        switch(this.getRightAnswer()) {
            case 1: return "A";
            case 2: return "B";
            case 3: return "C";
            case 4: return "D";
            default: return "Error";
        }
    }

    @Override
    public String toString() {
        return questionId + " - " + questionDesc + "\n" +
                "A: " + answerA + "\n" +
                "B: " + answerB + "\n" +
                "C: " + answerC + "\n" +
                "D: " + answerD + "\n" +
                "Dificuldade: " + questionDif + "\n";
    }

}
