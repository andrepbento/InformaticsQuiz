package activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.andre.informaticsquiz.R;

import data.PlayerData;

public class PlayerProfileActivity extends Activity {

    ImageView ivPlayerImage;
    EditText etPlayerName, etPlayerAge, etOcupationSpecif;
    Spinner spinnerPlayerSex, spinnerPlayerOcupation;

    PlayerData playerData;

    boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        Intent intent = getIntent();
        playerData = (PlayerData) intent.getSerializableExtra("playerData");

        ivPlayerImage = (ImageView) findViewById(R.id.iv_player_image);
        ivPlayerImage.setOnClickListener(onPlayerImageClick);
        if(playerData.getPhoto() != null)
            ivPlayerImage.setImageBitmap(playerData.getPhoto());
        else
            ivPlayerImage.setImageResource(R.drawable.drawable_user);
        ivPlayerImage.setEnabled(false);

        etPlayerName = (EditText) findViewById(R.id.et_player_name);
        etPlayerName.setText(playerData.getName());
        etPlayerName.setEnabled(false);

        spinnerPlayerSex = (Spinner) findViewById(R.id.spinner_player_sex);
        ArrayAdapter<CharSequence> adapterSex = ArrayAdapter.createFromResource(this,
                R.array.sex, android.R.layout.simple_spinner_item);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayerSex.setAdapter(adapterSex);
        spinnerPlayerSex.setSelection(playerData.getSexId());
        spinnerPlayerSex.setFocusable(false);
        spinnerPlayerSex.setClickable(true);

        etPlayerAge = (EditText) findViewById(R.id.et_player_age);
        etPlayerAge.setText(String.valueOf(playerData.getAge()));
        etPlayerAge.setFocusable(false);
        etPlayerAge.setClickable(true);

        spinnerPlayerOcupation = (Spinner) findViewById(R.id.spinner_player_ocupation);
        ArrayAdapter<CharSequence> adapterOcupation = ArrayAdapter.createFromResource(this,
                R.array.ocupation, android.R.layout.simple_spinner_item);
        adapterOcupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayerOcupation.setAdapter(adapterOcupation);
        spinnerPlayerOcupation.setSelection(playerData.getOcupationId());
        spinnerPlayerOcupation.setClickable(false);

        etOcupationSpecif = (EditText) findViewById(R.id.et_ocupation_specification);
        etOcupationSpecif.setText(playerData.getOcupation());
        etOcupationSpecif.setClickable(false);
    }

    View.OnClickListener onPlayerImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent takePlayerPhoto = new Intent(PlayerProfileActivity.this, CameraActivity.class);
            startActivityForResult(takePlayerPhoto, 1);
        }
    };

    public void onButtonPlayerProfileClick(View view) {
        switch (view.getId()) {
            case R.id.btn_edit_player_data:
                editPlayerData(view);
                break;
            case R.id.btn_delete_player_data:
                deletePlayerData();
                break;
        }
    }

    private void editPlayerData(View view) {
        if(!edit) {
            edit = true;
            ((Button)view).setText("Guardar dados");
            ivPlayerImage.setEnabled(true);
            etPlayerName.setEnabled(true);
            etPlayerAge.setEnabled(true);
            etOcupationSpecif.setEnabled(true);
            spinnerPlayerSex.setEnabled(true);
            spinnerPlayerOcupation.setEnabled(true);
        } else {
            edit = false;
            ((Button)view).setText("Editar dados");
            //playerData.setName();
            // Guardar os dados!
            ivPlayerImage.setEnabled(false);
            etPlayerName.setEnabled(false);
            etPlayerAge.setEnabled(false);
            etOcupationSpecif.setEnabled(false);
            spinnerPlayerSex.setEnabled(false);
            spinnerPlayerOcupation.setEnabled(false);
        }
    }

    private void deletePlayerData() {
        // LANCAR DIALOG BOX A PERGUNTAR SE TEM A CERTEZA
    }
}
