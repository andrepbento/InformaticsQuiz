package models;

import java.io.Serializable;

/**
 * Created by andre
 */

public class Question implements Serializable {
    static final long serialVersionUID = 1010L;

    private int questionId;
    private String questionDesc;
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private int rightAnswer;
    private int questionDif;

    public Question(int questionId, String questionDesc, String answerA, String answerB,
                    String answerC, String answerD, int rightAnswer, int questionDif) {
        this.questionId = questionId;
        this.questionDesc = questionDesc;
        this.answerA = answerA;
        this.answerB = answerB;
        this.answerC = answerC;
        this.answerD = answerD;
        this.rightAnswer = rightAnswer;
        this.questionDif = questionDif;
    }

    public String getQuestionDesc() {
        return questionDesc;
    }

    public String getAnswerA() {
        return answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public int getRightAnswer() {
        return rightAnswer;
    }

    public int getQuestionDif() {
        return questionDif;
    }

    public int getQuestionValue() {
        return questionDif * 5;
    }

    public String getRightAnswerLeter() {
        switch(this.getRightAnswer()) {
            case 1: return "A";
            case 2: return "B";
            case 3: return "C";
            case 4: return "D";
            default: throw new RuntimeException("Error");
        }
    }

}
