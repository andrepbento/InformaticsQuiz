package activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import java.io.File;

import interfaces.Constants;
import models.PlayerData;
import utils.InformaticsQuizHelper;

/**
 * Created by andre
 */

public class PlayerProfileActivity extends Activity {

    ImageView ivPlayerImage;
    EditText etPlayerName, etPlayerAge;
    Spinner spinnerPlayerSex, spinnerPlayerOcupation;

    PlayerData playerData;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_profile);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

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

        playerData = PlayerData.loadData(this);

        if(playerData != null) {
            getActionBar().setTitle(R.string.my_player_profile_text);
            if (playerData.getPhoto() != null)
                ivPlayerImage.setImageBitmap(playerData.getPhoto());
            else
                ivPlayerImage.setImageResource(R.drawable.drawable_user);

            etPlayerName.setText(playerData.getName());
            spinnerPlayerSex.setSelection(playerData.getSexId());
            etPlayerAge.setText(String.valueOf(playerData.getAge()));
            spinnerPlayerOcupation.setSelection(playerData.getOcupation());

            disableAllViewElements();
        } else {
            getActionBar().setTitle(R.string.create_player_profile_text);
            ivPlayerImage.setImageResource(R.drawable.drawable_user);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if(playerData != null)
            inflater.inflate(R.menu.menu_edit_player_profile, menu);
        else
            inflater.inflate(R.menu.menu_create_player_profile, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_create_user:
                createPlayerProfile();
                break;
            case R.id.item_edit_player_data:
                editPlayerProfile();
                break;
            case R.id.item_save_player_data:
                savePlayerProfile();
                break;
            case R.id.item_delete_player_data:
                deletePlayerData();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    View.OnClickListener onPlayerImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent takePlayerPhoto = new Intent(PlayerProfileActivity.this, CameraActivity.class);
            takePlayerPhoto.putExtra("cameraMode", Constants.PROFILE_PHOTO);
            startActivityForResult(takePlayerPhoto, Constants.PROFILE_PHOTO);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.PROFILE_PHOTO:
                if(resultCode == RESULT_OK) {
                    byte[] pictureData = data.getByteArrayExtra("pictureData");
                    if(pictureData != null) {
                        Bitmap userPicture = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
                        ivPlayerImage.setImageBitmap(Bitmap.createScaledBitmap(userPicture,
                                Constants.BITMAP_WIDHT_LARGE,
                                Constants.BITMAP_HEIGHT_LARGE, false));
                    }else{
                        Toast.makeText(this, "Error receiving photo", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Error taking photo", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void createPlayerProfile() {
        if(infoIsFilled()) {
            Bitmap playerBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.drawable_user)).getBitmap();
            if(ivPlayerImage.getDrawable() != null)
                    playerBitmap = ((BitmapDrawable)ivPlayerImage.getDrawable()).getBitmap();
            String name = etPlayerName.getText().toString();
            int sexId = spinnerPlayerSex.getSelectedItemPosition();
            String sex = spinnerPlayerSex.getSelectedItem().toString();
            int age = Integer.parseInt(etPlayerAge.getText().toString());
            int ocupationId = spinnerPlayerOcupation.getSelectedItemPosition();
            PlayerData player = new PlayerData(playerBitmap, name, sexId, sex, age, ocupationId);
            player.saveData(getApplicationContext());
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Todos os campos têm que ser preenchidos!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void editPlayerProfile() {
        menu.findItem(R.id.item_edit_player_data).setVisible(false);
        menu.findItem(R.id.item_save_player_data).setVisible(true);
        enableAllViewElements();
    }

    private void savePlayerProfile() {
        if(infoIsFilled()) {
            menu.findItem(R.id.item_edit_player_data).setVisible(true);
            menu.findItem(R.id.item_save_player_data).setVisible(false);
            playerData.setPhoto(((BitmapDrawable)ivPlayerImage.getDrawable()).getBitmap());
            playerData.setName(etPlayerName.getText().toString());
            playerData.setSexId(spinnerPlayerSex.getSelectedItemPosition());
            playerData.setSex(spinnerPlayerSex.getSelectedItem().toString());
            playerData.setAge(Integer.parseInt(etPlayerAge.getText().toString()));
            playerData.setOcupation(spinnerPlayerOcupation.getSelectedItemPosition());
            playerData.saveData(this);
            disableAllViewElements();
        } else {
            Toast.makeText(getApplicationContext(), "Todos os campos têm que ser preenchidos!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean infoIsFilled() {
        if(etPlayerName.getText().toString().isEmpty())
            return false;
        if(etPlayerAge.getText().toString().isEmpty())
            return false;
        return true;
    }

    private void enableAllViewElements() {
        ivPlayerImage.setEnabled(true);
        etPlayerName.setEnabled(true);
        spinnerPlayerSex.setEnabled(true);
        etPlayerAge.setEnabled(true);
        spinnerPlayerOcupation.setEnabled(true);
    }

    private void disableAllViewElements() {
        ivPlayerImage.setEnabled(false);
        etPlayerName.setEnabled(false);
        spinnerPlayerSex.setEnabled(false);
        etPlayerAge.setEnabled(false);
        spinnerPlayerOcupation.setEnabled(false);
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
                    File playerData = new File(getFilesDir(), Constants.playerFileName);
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
