package activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.andre.informaticsquiz.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import data.Question;
import helper.InformaticsQuizHelper;

public class DataBaseActivity extends Activity {

    private static String QUESTION = "question";
    private static String ANSWER_A = "answerA";
    private static String ANSWER_B = "answerB";
    private static String ANSWER_C = "answerC";
    private static String ANSWER_D = "answerD";
    private static String RIGHT_ANSWER = "rightAnswer";
    private static String DIFFICULTY = "difficulty";

    protected InformaticsQuizHelper dbI;

    protected ListView listView;

    ArrayList<HashMap<String, String>> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_browser);

        questions = new ArrayList<>();

        String [] camposDados = {QUESTION, ANSWER_A, ANSWER_B, ANSWER_C, ANSWER_D, RIGHT_ANSWER,
                DIFFICULTY};
        int [] camposUI = {R.id.tv_pergunta, R.id.tv_resposta_a, R.id.tv_resposta_b,
                R.id.tv_resposta_c, R.id.tv_resposta_d, R.id.tv_resposta_certa, R.id.tv_dificuldade};
        SimpleAdapter sa = new SimpleAdapter(this, questions, R.layout.row_question, camposDados, camposUI);

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(sa);
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Clicaste em " + dados.get(position).get("titulo").toString(), Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    protected void addValues(String question, String answerA, String answerB, String answerC,
                             String answerD, String rightAnswer, String difficulty) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put(QUESTION,question);
        hm.put(ANSWER_A,answerA);
        hm.put(ANSWER_B,answerB);
        hm.put(ANSWER_C,answerC);
        hm.put(ANSWER_D,answerD);
        hm.put(RIGHT_ANSWER,rightAnswer);
        hm.put(DIFFICULTY,difficulty);
        questions.add(hm);
    }

    @Override
    protected void onResume() {
        super.onResume();

        dbI = new InformaticsQuizHelper(getApplicationContext());

        dbI.create();

        List<Question> questionsList;

        if(dbI.open()) {
            questionsList = dbI.getAllQuestions();

            for (int i = 0; i < questionsList.size(); i++)
                addValues(questionsList.get(i).getQuestionId() +
                        " - " + questionsList.get(i).getQuestionDesc(),
                        "A: " + questionsList.get(i).getAnswerA(),
                        "B: " + questionsList.get(i).getAnswerB(),
                        "C: " + questionsList.get(i).getAnswerC(),
                        "D: " + questionsList.get(i).getAnswerD(),
                        "Resposta certa: " + questionsList.get(i).getRightAnswerLeter(),
                        "Dificuldade: " + questionsList.get(i).getQuestionDif());
        }

    }
}
