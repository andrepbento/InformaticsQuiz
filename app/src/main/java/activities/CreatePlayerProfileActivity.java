package activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import data.PlayerData;

public class CreatePlayerProfileActivity extends Activity {

    ImageView ivPlayerImage;
    EditText etPlayerName, etPlayerAge, etOcupationSpecif;
    Spinner spinnerPlayerSex, spinnerPlayerOcupation;
    TextView tvOcupationSpecif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_player_profile);

        ivPlayerImage = (ImageView) findViewById(R.id.iv_player_image);
        ivPlayerImage.setOnClickListener(onPlayerImageClick);

        etPlayerName = (EditText) findViewById(R.id.et_player_name);

        spinnerPlayerSex = (Spinner) findViewById(R.id.spinner_player_sex);
        ArrayAdapter<CharSequence> adapterSex = ArrayAdapter.createFromResource(this,
                R.array.sex, android.R.layout.simple_spinner_item);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayerSex.setAdapter(adapterSex);

        etPlayerAge = (EditText) findViewById(R.id.et_player_age);

        spinnerPlayerOcupation = (Spinner) findViewById(R.id.spinner_player_ocupation);
        ArrayAdapter<CharSequence> adapterOcupation = ArrayAdapter.createFromResource(this,
                R.array.ocupation, android.R.layout.simple_spinner_item);
        adapterOcupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayerOcupation.setAdapter(adapterOcupation);
        spinnerPlayerOcupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        tvOcupationSpecif.setText(R.string.tv_estudante_text);
                        break;
                    case 1:
                        tvOcupationSpecif.setText(R.string.tv_trabalhador_text);
                        break;
                    case 2:
                        tvOcupationSpecif.setText(R.string.tv_outro_text);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tvOcupationSpecif = (TextView) findViewById(R.id.tv_ocupation_specification);
        tvOcupationSpecif.setText(R.string.tv_estudante_text);
        etOcupationSpecif = (EditText) findViewById(R.id.et_ocupation_specification);
    }

    View.OnClickListener onPlayerImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent takePlayerPhoto = new Intent(CreatePlayerProfileActivity.this, CameraActivity.class);
            startActivityForResult(takePlayerPhoto, 1);
        }
    };

    AdapterView.OnItemClickListener onSpinnerPlayerOcupationClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                byte[] imageData = extras.getByteArray("playerImage");
                Bitmap playerImage = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                ivPlayerImage.setImageBitmap(playerImage);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("CameraActivityResult", "Error");
            }
        }
    }

    public void onButtonCreatePlayerProfileClick(View view) {
        if(infoIsFilled()) {
            Bitmap playerBitmap = null;
            if(ivPlayerImage.getDrawable() != null)
               playerBitmap = ((BitmapDrawable)ivPlayerImage.getDrawable()).getBitmap();
            String name = etPlayerName.getText().toString();
            int sexId = spinnerPlayerSex.getSelectedItemPosition();
            String sex = spinnerPlayerSex.getSelectedItem().toString();
            int age = Integer.parseInt(etPlayerAge.getText().toString());
            int ocupationId = spinnerPlayerOcupation.getSelectedItemPosition();
            String ocupation = etOcupationSpecif.getText().toString();
            PlayerData player = new PlayerData(playerBitmap, name, sexId, sex, age, ocupationId, ocupation);
            player.saveData(getApplicationContext());
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Todos os campos têm que ser preenchidos!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean infoIsFilled() {
        if(etPlayerName.getText().toString().isEmpty())
            return false;
        if(etPlayerAge.getText().toString().isEmpty() &&
                Integer.parseInt(etPlayerAge.getText().toString()) >= 7) // Only players with age >= 7 are alowed to play
            return false;
        if(etOcupationSpecif.getText().toString().isEmpty())
            return false;
        return true;
    }
}
