package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import interfaces.Constants;
import utils.InformaticsQuizHelper;

/**
 * Created by andre
 */

public class Game implements Serializable {

    static final long serialVersionUID = 1010L;

    private transient InformaticsQuizHelper dbI;

    private String difficulty;
    private int difficultyId;
    private int nQuestions;
    private int currentQuestionNum = 0;
    private int score = 0;
    private int nRightQuestions = 0;
    private int nWrongQuestions = 0;
    private long questionTime;
    private boolean timer;

    private String[] diffArray;

    private ArrayList<Question> questionsList;

    private int gameMode;
    private int nPlayers;

    public Game(InformaticsQuizHelper dbI, String[] diffArray, int difficultyId, int nQuestions, boolean timer) {
        this.dbI = dbI;

        this.diffArray = diffArray;
        this.difficultyId = difficultyId;
        this.difficulty = this.diffArray[difficultyId];
        this.nQuestions = nQuestions;
        this.timer = timer;

        if(this.difficulty.equals(diffArray[0])) {
            fillQuestionsList(nQuestions, 0);
            setQuestionTime(Constants.easyGameTime * 1000);
        } else if(this.difficulty.equals(diffArray[1])) {
            fillQuestionsList(nQuestions, 1);
            setQuestionTime(Constants.moderateGameTime * 1000);
        } else {
            fillQuestionsList(nQuestions, 2);
            setQuestionTime(Constants.hardGameTime * 1000);
        }
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getDifficultyId() { return difficultyId; }

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

    public long getQuestionTime() {
        return this.questionTime;
    }

    public boolean getTimer() { return timer; }

    private void setQuestionTime(long valor) {
        this.questionTime = valor;
    }

    public int getnPlayers() {
        return nPlayers;
    }

    public void setnPlayers(int nPlayers) {
        this.nPlayers = nPlayers;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    private void fillQuestionsList(int nQuestions, int gameDifficulty){
        questionsList = new ArrayList<>();

        int nEasyQuestions = 0;
        int nModerateQuestions = 0;
        int nHardQuestions = 0;

        switch(gameDifficulty) {
            case 0:
                nEasyQuestions = (int)Math.ceil(nQuestions*(Constants.easyGameValues[0] /100.0f));
                nModerateQuestions = (int)(nQuestions*(Constants.easyGameValues[1] /100.0f));
                nHardQuestions = (int)(nQuestions*(Constants.easyGameValues[2] /100.0f));
                if(nEasyQuestions + nModerateQuestions + nHardQuestions < nQuestions)
                    nEasyQuestions++;
                if(nEasyQuestions + nModerateQuestions + nHardQuestions > nQuestions)
                    nHardQuestions--;
                break;
            case 1:
                nEasyQuestions = (int)(nQuestions*(Constants.moderateGameValues[0] /100.0f));
                nModerateQuestions = (int)Math.ceil(nQuestions*(Constants.moderateGameValues[1] /100.0f));
                nHardQuestions = (int)(nQuestions*(Constants.moderateGameValues[2] /100.0f));
                if(nEasyQuestions + nModerateQuestions + nHardQuestions < nQuestions)
                    nModerateQuestions++;
                if(nEasyQuestions + nModerateQuestions + nHardQuestions > nQuestions)
                    nHardQuestions--;
                break;
            case 2:
                nEasyQuestions = (int)(nQuestions*(Constants.hardGameValues[0] /100.0f));
                nModerateQuestions = (int)(nQuestions*(Constants.hardGameValues[1] /100.0f));
                nHardQuestions = (int)Math.ceil(nQuestions*(Constants.hardGameValues[2] /100.0f));
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

    public boolean checkAnswer(String answerLeter) {
        if(questionsList.get(currentQuestionNum).getRightAnswerLeter().equals(answerLeter)) {
            score += questionsList.get(currentQuestionNum).getQuestionValue();
            nRightQuestions++;
            currentQuestionNum++;
            return true;
        }
        nWrongQuestions++;
        currentQuestionNum++;
        return false;
    }

    public boolean checkEnd() {
        return currentQuestionNum >= this.getnQuestions() ? true : false;
    }

    public boolean getResult() {
        if(score == 0)
            return false;
        return getTotalScore() / 2 <= score;
    }

    public int getTotalScore() {
        int totalScore = 0;
        for(Question q : questionsList)
            totalScore += q.getQuestionValue();
        return totalScore;
    }
}
