package activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andre.informaticsquiz.PublicConstantValues;
import com.example.andre.informaticsquiz.R;

import java.io.File;

import data.PlayerData;
import helper.InformaticsQuizHelper;

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

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        playerData = (PlayerData) intent.getSerializableExtra("playerData");

        ivPlayerImage = (ImageView) findViewById(R.id.iv_player_image);
        ivPlayerImage.setOnClickListener(onPlayerImageClick);
        if(playerData.getPhoto() != null)
            ivPlayerImage.setImageBitmap(playerData.getPhoto());
        else
            ivPlayerImage.setImageResource(R.drawable.drawable_user);

        etPlayerName = (EditText) findViewById(R.id.et_player_name);
        etPlayerName.setText(playerData.getName());

        spinnerPlayerSex = (Spinner) findViewById(R.id.spinner_player_sex);
        ArrayAdapter<CharSequence> adapterSex = ArrayAdapter.createFromResource(this,
                R.array.sex, android.R.layout.simple_spinner_item);
        adapterSex.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayerSex.setAdapter(adapterSex);
        spinnerPlayerSex.setSelection(playerData.getSexId());

        etPlayerAge = (EditText) findViewById(R.id.et_player_age);
        etPlayerAge.setText(String.valueOf(playerData.getAge()));

        spinnerPlayerOcupation = (Spinner) findViewById(R.id.spinner_player_ocupation);
        ArrayAdapter<CharSequence> adapterOcupation = ArrayAdapter.createFromResource(this,
                R.array.ocupation, android.R.layout.simple_spinner_item);
        adapterOcupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlayerOcupation.setAdapter(adapterOcupation);
        spinnerPlayerOcupation.setSelection(playerData.getOcupationId());

        etOcupationSpecif = (EditText) findViewById(R.id.et_ocupation_specification);
        etOcupationSpecif.setText(playerData.getOcupation());

        disableAllViewElements();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onMenuItemSelected(featureId, item);
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
            enableAllViewElements();
        } else {
            edit = false;
            ((Button)view).setText("Editar dados");
            //playerData.setPhoto(((BitmapDrawable)ivPlayerImage.getDrawable()).getBitmap());
            playerData.setName(etPlayerName.getText().toString());
            playerData.setSexId(spinnerPlayerSex.getSelectedItemPosition());
            playerData.setSex(spinnerPlayerSex.getSelectedItem().toString());
            playerData.setAge(Integer.parseInt(etPlayerAge.getText().toString()));
            playerData.setOcupationId(spinnerPlayerOcupation.getSelectedItemPosition());
            playerData.setOcupation(etOcupationSpecif.getText().toString());
            playerData.saveData(this);
            disableAllViewElements();
        }
    }

    private void enableAllViewElements() {
        ivPlayerImage.setEnabled(true);
        //ivPlayerImage.setFocusable(true);
        etPlayerName.setEnabled(true);
        //etPlayerName.setFocusable(true);
        spinnerPlayerSex.setEnabled(true);
        //spinnerPlayerSex.setFocusable(true);
        etPlayerAge.setEnabled(true);
        //etPlayerAge.setFocusable(true);
        spinnerPlayerOcupation.setEnabled(true);
        //spinnerPlayerOcupation.setFocusable(true);
        etOcupationSpecif.setEnabled(true);
        //etOcupationSpecif.setFocusable(true);
    }

    private void disableAllViewElements() {
        ivPlayerImage.setEnabled(false);
        //ivPlayerImage.setFocusable(false);
        etPlayerName.setEnabled(false);
        //etPlayerName.setFocusable(false);
        spinnerPlayerSex.setEnabled(false);
        //spinnerPlayerSex.setFocusable(false);
        etPlayerAge.setEnabled(false);
        //etPlayerAge.setFocusable(false);
        spinnerPlayerOcupation.setEnabled(false);
        //spinnerPlayerOcupation.setFocusable(false);
        etOcupationSpecif.setEnabled(false);
        //etOcupationSpecif.setFocusable(false);
    }

    private void deletePlayerData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    File playerData = new File(getFilesDir(), PublicConstantValues.playerFileName);
                    boolean delete = playerData.delete();
                    InformaticsQuizHelper dbI = new InformaticsQuizHelper(getApplicationContext());
                    dbI.create();
                    if(dbI.open()) {
                        dbI.deleteSinglePlayerGameResults();
                        //dbI.deleteMultiPlayerGameResults();
                        dbI.close();
                    }
                    Toast.makeText(getApplication(), "Player deleted", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE: break;
            }
        }
    };
}
