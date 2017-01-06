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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import java.io.File;

import interfaces.Constants;
import models.MultiPlayerGameResult;
import models.MySharedPreferences;
import models.PlayerData;
import models.SinglePlayerGameResult;

/**
 * Created by andre
 */

public class PlayerProfileActivity extends Activity {
    ImageView ivPlayerImage;
    EditText etPlayerName;

    PlayerData playerData;

    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySharedPreferences.loadTheme(this);
        setContentView(R.layout.activity_player_profile);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        ivPlayerImage = (ImageView) findViewById(R.id.iv_player_image);
        ivPlayerImage.setOnClickListener(onPlayerImageClick);
        etPlayerName = (EditText) findViewById(R.id.et_player_name);

        playerData = PlayerData.loadData(this);

        if(playerData != null) {
            getActionBar().setTitle(R.string.my_player_profile_text);
            if (playerData.getPhoto() != null)
                ivPlayerImage.setImageBitmap(playerData.getPhoto());
            else
                ivPlayerImage.setImageResource(R.drawable.drawable_no_user_img);

            etPlayerName.setText(playerData.getName());

            disableAllViewElements();
        } else {
            getActionBar().setTitle(R.string.create_player_profile_text);
            ivPlayerImage.setImageResource(R.drawable.drawable_no_user_img);
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
                        Toast.makeText(this, R.string.error_receiving_photo_text, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, R.string.error_taking_photo, Toast.LENGTH_SHORT).show();
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
            PlayerData player = new PlayerData(playerBitmap, name);
            player.saveData(getApplicationContext());
            finish();
        } else {
            Toast.makeText(getApplicationContext(), R.string.all_the_fields_must_be_filled_text+"!",
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
            playerData.saveData(this);
            disableAllViewElements();
        } else {
            Toast.makeText(getApplicationContext(), R.string.all_the_fields_must_be_filled_text,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean infoIsFilled() {
        if(etPlayerName.getText().toString().isEmpty())
            return false;
        return true;
    }

    private void enableAllViewElements() {
        ivPlayerImage.setEnabled(true);
        etPlayerName.setEnabled(true);
    }

    private void disableAllViewElements() {
        ivPlayerImage.setEnabled(false);
        etPlayerName.setEnabled(false);
    }

    private void deletePlayerData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.are_you_sure_text)+"?")
                .setPositiveButton(R.string.yes_text, dialogClickListener)
                .setNegativeButton(R.string.no_text, dialogClickListener)
                .show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    File playerData = new File(getFilesDir(), Constants.playerFileName);
                    playerData.delete();
                    SinglePlayerGameResult.deleteAllData(getApplicationContext());
                    MultiPlayerGameResult.deleteAllData(getApplicationContext());
                    Toast.makeText(getApplication(), R.string.player_deleted_text, Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE: break;
            }
        }
    };
}
