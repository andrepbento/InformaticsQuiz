package activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.andre.informaticsquiz.PublicConstantValues;
import com.example.andre.informaticsquiz.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import data.PlayerData;

public class MainMenuActivity extends Activity {

    private PlayerData player;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        if(loadPlayerData(this)) {
            if(player.getPhoto() != null)
                menu.findItem(R.id.item_user).setIcon((Drawable)new BitmapDrawable(player.getPhoto()));
        } else {
            player = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_user:
                if(player == null) {
                    startActivity(new Intent(MainMenuActivity.this, CreatePlayerProfileActivity.class));
                } else {
                    Intent intent = new Intent(MainMenuActivity.this, PlayerProfileActivity.class);
                    intent.putExtra("playerData", player);
                    startActivity(intent);
                }
                break;
            case R.id.item_opt_bd:
                Intent intent_bd = new Intent(MainMenuActivity.this, DataBaseActivity.class);
                startActivity(intent_bd);
                return true;
            case R.id.item_preferencias:
                /*
                Intent intent_preferences = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent_preferences);
                */
                Toast.makeText(getApplicationContext(), "Por implementar...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_acerca_de:
                Toast.makeText(getApplicationContext(), "Por implementar...", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClick(View view) {
        int view_id = view.getId();

        switch (view_id) {
            case R.id.btn_single_player:
                startActivity(new Intent(MainMenuActivity.this, GameConfigActivity.class));
                break;
            case R.id.btn_multi_player:
                Toast.makeText(getApplicationContext(), "Por implementar...", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean loadPlayerData(Context context) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(PublicConstantValues.playerFileName);
        } catch (FileNotFoundException e) {
            Log.e("MainMenuActivity", "FileNotFoundException " + e.getMessage());
            return false;
        }
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(fis);
            player = (PlayerData) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            Log.e("MainMenuActivity", "IOException " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            Log.e("MainMenuActivity", "ClassNotFoundException " + e.getMessage());
            return false;
        }

        return true;
    }
}
