package activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import models.Question;
import utils.InformaticsQuizHelper;

public class QuestionsViewerActivity extends Activity {

    private static String QUESTION = "QUESTION";
    private static String ANSWER_A = "ANSWER_A";
    private static String ANSWER_B = "ANSWER_B";
    private static String ANSWER_C = "ANSWER_C";
    private static String ANSWER_D = "ANSWER_D";
    private static String RIGHT_ANSWER = "RIGHT_ANSWER";
    private static String DIFFICULTY = "DIFFICULTY";

    ArrayList<HashMap<String, Object>> data;

    private InformaticsQuizHelper dbI;

    private void addValuesToAdapter(String question, String answerA, String answerB, String answerC,
                                    String answerD, String rightAnswer, int difficulty) {
        HashMap<String, Object> hm = new HashMap<>();
        hm.put(QUESTION,question);
        hm.put(ANSWER_A,answerA);
        hm.put(ANSWER_B,answerB);
        hm.put(ANSWER_C,answerC);
        hm.put(ANSWER_D,answerD);
        hm.put(RIGHT_ANSWER,rightAnswer);
        hm.put(DIFFICULTY,difficulty);
        data.add(hm);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_viewer);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        data = new ArrayList<>();

        dbI = new InformaticsQuizHelper(getApplicationContext());

        dbI.create();

        if(dbI.open()) {
            List<Question> questionsList = dbI.getAllQuestions();

            for (int i = 0; i < questionsList.size(); i++)
                    addValuesToAdapter(questionsList.get(i).getQuestionDesc(),
                            questionsList.get(i).getAnswerA(),
                            questionsList.get(i).getAnswerB(),
                            questionsList.get(i).getAnswerC(),
                            questionsList.get(i).getAnswerD(),
                            questionsList.get(i).getRightAnswerLeter(),
                            questionsList.get(i).getQuestionDif());

            ListView listView = (ListView) findViewById(R.id.listView);
            listView.setAdapter(new QuestionsListAdapter());

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getApplicationContext(), "Clicked on element n." + position+1,
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class QuestionsListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public HashMap<String, Object> getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View layout = getLayoutInflater().inflate(R.layout.row_question_viewer, null);

            String question = (String) data.get(i).get(QUESTION);
            String answerA = (String) data.get(i).get(ANSWER_A);
            String answerB = (String) data.get(i).get(ANSWER_B);
            String answerC = (String) data.get(i).get(ANSWER_C);
            String answerD = (String) data.get(i).get(ANSWER_D);
            String rightAnswer = (String) data.get(i).get(RIGHT_ANSWER);
            int difficulty = (int) data.get(i).get(DIFFICULTY);

            LinearLayout ll = (LinearLayout)layout.findViewById(R.id.question_viewer_line);

            switch (difficulty) {
                case 1:
                    ll.setBackground(getResources().getDrawable(R.drawable.green_white_gradient));
                    break;
                case 2:
                    ll.setBackground(getResources().getDrawable(R.drawable.yellow_white_gradient));
                    break;
                case 3:
                    ll.setBackground(getResources().getDrawable(R.drawable.red_white_gradient));
                    break;
            }

            ((TextView) layout.findViewById(R.id.tv_pergunta))
                    .setText((i+1) + " - " + question);
            ((TextView) layout.findViewById(R.id.tv_resposta_a))
                    .setText("A: " + answerA);
            ((TextView) layout.findViewById(R.id.tv_resposta_b))
                    .setText("B: " + answerB);
            ((TextView) layout.findViewById(R.id.tv_resposta_c))
                    .setText("C: " + answerC);
            ((TextView) layout.findViewById(R.id.tv_resposta_d))
                    .setText("D: " + answerD);

            // RIGHT ANSWER NEEDED THINK COLOR

            return layout;
        }
    }
}
