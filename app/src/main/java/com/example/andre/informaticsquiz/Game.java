package com.example.andre.informaticsquiz;

import android.content.Context;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import data.Question;
import helper.InformaticsQuizHelper;

/**
 * Created by andre on 02/11/2016.
 */

public class Game implements Serializable {
    private static Integer[] easyGameValues = {60, 30, 10};
    private static Integer easyGameTime = 90;
    private static Integer[] moderateGameValues = {30, 40, 30};
    private static Integer moderateGameTime = 60;
    private static Integer[] hardGameValues = {10, 30, 60};
    private static Integer hardGameTime = 30;

    private InformaticsQuizHelper dbI;

    private String difficulty;
    private int nQuestions;
    private int currentQuestionNum = 0;
    private int score = 0;
    private int nRightQuestions = 0;
    private int nWrongQuestions = 0;
    private int questionTime;

    private String[] diffArray;

    private ArrayList<Question> questionsList;

    public Game(Context context, String difficulty, int nQuestions) {
        dbI = new InformaticsQuizHelper(context);

        dbI.create();

        if(dbI.open()){
            this.difficulty = difficulty;
            this.nQuestions = nQuestions;

            diffArray = context.getResources().getStringArray(R.array.difficulty);

            if(this.difficulty.equals(diffArray[0])) {
                fillQuestionsList(nQuestions, 0);
                setQuestionTime(easyGameTime);
            } else if(this.difficulty.equals(diffArray[1])) {
                fillQuestionsList(nQuestions, 1);
                setQuestionTime(moderateGameTime);
            } else {
                fillQuestionsList(nQuestions, 2);
                setQuestionTime(hardGameTime);
            }
        }
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getnQuestions() {
        return nQuestions;
    }

    public Question getCurrentQuestion() { return questionsList.get(currentQuestionNum); }

    public int getCurrentQuestionNum() {
        return currentQuestionNum;
    }

    public int getScore() {
        return score;
    }

    public int getnRightQuestions() { return nRightQuestions; }

    public int getnWrongQuestions() { return nWrongQuestions; }

    public int getQuestionTime() {
        return this.questionTime;
    }

    private void setQuestionTime(int valor) {
        this.questionTime = valor;
    }

    private void fillQuestionsList(int nQuestions, int gameDifficulty){
        questionsList = new ArrayList<>();

        int nEasyQuestions = 0;
        int nModerateQuestions = 0;
        int nHardQuestions = 0;

        switch(gameDifficulty) {
            case 0:
                nEasyQuestions = (int)Math.ceil(nQuestions*(easyGameValues[0] /100.0f));
                nModerateQuestions = (int)(nQuestions*(easyGameValues[1] /100.0f));
                nHardQuestions = (int)(nQuestions*(easyGameValues[2] /100.0f));
                if(nEasyQuestions + nModerateQuestions + nHardQuestions < nQuestions)
                    nEasyQuestions++;
                if(nEasyQuestions + nModerateQuestions + nHardQuestions > nQuestions)
                    nHardQuestions--;
                break;
            case 1:
                nEasyQuestions = (int)(nQuestions*(moderateGameValues[0] /100.0f));
                nModerateQuestions = (int)Math.ceil(nQuestions*(moderateGameValues[1] /100.0f));
                nHardQuestions = (int)(nQuestions*(moderateGameValues[2] /100.0f));
                if(nEasyQuestions + nModerateQuestions + nHardQuestions < nQuestions)
                    nModerateQuestions++;
                if(nEasyQuestions + nModerateQuestions + nHardQuestions > nQuestions)
                    nHardQuestions--;
                break;
            case 2:
                nEasyQuestions = (int)(nQuestions*(hardGameValues[0] /100.0f));
                nModerateQuestions = (int)(nQuestions*(hardGameValues[1] /100.0f));
                nHardQuestions = (int)Math.ceil(nQuestions*(hardGameValues[2] /100.0f));
                if(nEasyQuestions + nModerateQuestions + nHardQuestions < nQuestions)
                    nHardQuestions++;
                if(nEasyQuestions + nModerateQuestions + nHardQuestions > nQuestions)
                    nEasyQuestions--;
                break;
        }

        List<Question> easyQuestionsList = dbI.getEasyQuestions();

        for(int i = 0; i < nEasyQuestions;i++) {
            int index = getRandomNumberMinMax(0, easyQuestionsList.size()-1);
            questionsList.add(easyQuestionsList.get(index));
            easyQuestionsList.remove(index);
        }

        List<Question> moderateQuestionsList = dbI.getModerateQuestions();

        for(int i = 0; i < nModerateQuestions;i++) {
            int index = getRandomNumberMinMax(0, moderateQuestionsList.size()-1);
            questionsList.add(moderateQuestionsList.get(index));
            moderateQuestionsList.remove(index);
        }

        List<Question> hardQuestionsList = dbI.getHardQuestions();

        for(int i = 0; i < nHardQuestions;i++) {
            int index = getRandomNumberMinMax(0, hardQuestionsList.size()-1);
            questionsList.add(hardQuestionsList.get(index));
            hardQuestionsList.remove(index);
        }
    }

    private int getRandomNumberMinMax(int min, int max) {
        int range = Math.abs(max - min) + 1;
        return (int)(Math.random() * range) + (min <= max ? min : max);
    }

    private Question getQuestion(int questionNumb) {
        return questionsList.get(questionNumb);
    }

    public int getQuestionDifficulty() {
        return Integer.parseInt(questionsList.get(currentQuestionNum).getQuestionDif());
    }

    public Question getNextQuestion() {
        return questionsList.get(currentQuestionNum);
    }

    public boolean checkAnswer(String answerLeter) {
        if(questionsList.get(currentQuestionNum).getRightAnswerLeter().equals(answerLeter)) {
            score += questionsList.get(currentQuestionNum).getQuestionDifInteger();
            nRightQuestions++;
            return true;
        }
        nWrongQuestions++;
        return false;
    }

    public boolean checkEnd() {
        currentQuestionNum++;
        return currentQuestionNum >= this.getnQuestions() ? true : false;
    }

    public boolean getResult() {
        if(score == 0)
            return false;

        return getTotalScore() / 2 < score;
    }

    private int getTotalScore() {
        int totalScore = 0;
        for(Question q : questionsList)
            totalScore += q.getQuestionDifInteger();
        return totalScore;
    }
}
