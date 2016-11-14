package activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

    private Button btnMultiPlayer, btnPlayerStatisctic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        player = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        btnMultiPlayer = (Button) findViewById(R.id.btn_multi_player);
        btnPlayerStatisctic = (Button) findViewById(R.id.btn_player_statistics);

        if(loadPlayerData(this)) {
            btnMultiPlayer.setEnabled(true);
            btnPlayerStatisctic.setEnabled(true);
            //if(player.getPhoto() != null)
            //    main_menu_menu.findItem(R.id.item_user).setIcon((Drawable)new BitmapDrawable(player.getPhoto()));
            //else
            //    main_menu_menu.findItem(R.id.item_user).setIcon(R.drawable.drawable_user);
        } else {
            player = null;
            btnMultiPlayer.setEnabled(false);
            btnPlayerStatisctic.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Não existe conta criada, todos os dados serão perdidos," +
                    " se quiseres que se guardem cria uma conta pff", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu_menu, menu);
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
                startActivity(new Intent(MainMenuActivity.this, QuestionsViewerActivity.class));
                break;
            case R.id.item_tutorial:
                startActivity(new Intent(MainMenuActivity.this, HowToPlayActivity.class));
                break;
            case R.id.item_preferencias:
                /*
                Intent intent_preferences = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent_preferences);
                */
                Toast.makeText(getApplicationContext(), "Por implementar...", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_acerca_de:
                startActivity(new Intent(MainMenuActivity.this, AboutTheAppActivity.class));
                break;
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
            case R.id.btn_player_statistics:
                startActivity(new Intent(MainMenuActivity.this, PlayerStatiscticsActivity.class));
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
