package activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import application.InformaticsQuizApp;
import interfaces.Constants;
import models.MySharedPreferences;
import models.PlayerData;
import network.Client;
import network.Connection;

/**
 * Created by andre
 */

public class MainMenuActivity extends Activity {
    private PlayerData player;
    private Button btnMultiPlayer, btnPlayerResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MySharedPreferences.loadTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnMultiPlayer = (Button) findViewById(R.id.btn_multi_player);
        btnPlayerResults = (Button) findViewById(R.id.btn_player_results);

        this.player = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if((player = PlayerData.loadData(this)) != null) {
            btnMultiPlayer.setEnabled(true);
            btnPlayerResults.setEnabled(true);
        } else {
            btnMultiPlayer.setEnabled(false);
            btnPlayerResults.setEnabled(false);
        }

        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(player != null)
            menu.findItem(R.id.item_user).setIcon(new BitmapDrawable(player.getPhoto()));
        else
            menu.findItem(R.id.item_user).setIcon(getResources().getDrawable(R.drawable.drawable_user));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_user:
                startActivity(new Intent(MainMenuActivity.this, PlayerProfileActivity.class));
                break;
            case R.id.item_questions_viewer:
                startActivity(new Intent(MainMenuActivity.this, QuestionsViewerActivity.class));
                break;
            case R.id.item_tutorial:
                startActivity(new Intent(MainMenuActivity.this, HowToPlayActivity.class));
                break;
            case R.id.item_preferencias:
                startActivity(new Intent(MainMenuActivity.this, SettingsActivity.class));
                break;
            case R.id.item_acerca_de:
                startActivity(new Intent(MainMenuActivity.this, AboutTheAppActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_single_player:
                Intent spIntent = new Intent(MainMenuActivity.this, CreateGameActivity.class);
                spIntent.putExtra("gameMode", Constants.SP_MODE);
                startActivity(spIntent);
                break;
            case R.id.btn_multi_player:
                if(Connection.checkNetworkConnection(MainMenuActivity.this))
                    setUpMultiPlayerDialogBox();
                else
                    Toast.makeText(this, "No network connection!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_player_results:
                startActivity(new Intent(MainMenuActivity.this, PlayerResultsActivity.class));
                break;
        }
    }

    private void setUpMultiPlayerDialogBox() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainMenuActivity.this);
        builderSingle.setTitle(getResources().getString(R.string.multi_player_text));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainMenuActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getString(R.string.create_game_text));
        arrayAdapter.add(getString(R.string.enter_game_text));

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        Intent mpIntent = new Intent(MainMenuActivity.this, CreateGameActivity.class);
                        mpIntent.putExtra("gameMode", Constants.MP_MODE);
                        startActivity(mpIntent);
                        break;
                    case 1:
                        setUpJoinGameMethod();
                }
            }
        });

        builderSingle.show();
    }

    private void setUpJoinGameMethod() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainMenuActivity.this);
        builderSingle.setTitle(getResources().getString(R.string.join_using_text));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MainMenuActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add(getString(R.string.server_ip_text));
        arrayAdapter.add(getString(R.string.qrcode_text));

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        setUpServerConnectionDetails();
                        break;
                    case 1:
                        Intent jgIntent = new Intent(MainMenuActivity.this, CameraActivity.class);
                        jgIntent.putExtra("cameraMode", Constants.QRCODE_PHOTO);
                        startActivityForResult(jgIntent, Constants.QRCODE_PHOTO);
                }
            }
        });

        builderSingle.show();
    }

    private void setUpServerConnectionDetails(){
        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(MainMenuActivity.this);
        dialogAlert.setTitle(R.string.insert_server_ip_text);

        final EditText etServerIP = new EditText(MainMenuActivity.this);
        etServerIP.setHint("192.168.1.33");
        etServerIP.setText("192.168.1.33");
        dialogAlert.setView(etServerIP);
        dialogAlert.setPositiveButton(R.string.connect_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String serverIp = "";
                        if(!etServerIP.getText().toString().isEmpty())
                            serverIp = etServerIP.getText().toString();
                        else {
                            Toast.makeText(MainMenuActivity.this, "Server IP insertion error!",
                                    Toast.LENGTH_SHORT).show();
                            setUpServerConnectionDetails();
                        }

                        createClient(serverIp);
                    }
                }).setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        dialogAlert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.QRCODE_PHOTO:
                if(resultCode == RESULT_OK)
                    createClient(data.getStringExtra("connectionDetails"));
                else
                    Toast.makeText(this, "Erro de leitura do QRCode", Toast.LENGTH_SHORT).show();
        }
    }

    private void createClient(String serverIp) {
        InformaticsQuizApp app = (InformaticsQuizApp)getApplication();
        Client client = new Client(MainMenuActivity.this, serverIp,
                Constants.serverListeningPort);
        client.start();
        app.setLocalClient(client);
        // Lançar uma progressBar animada
    }
}
