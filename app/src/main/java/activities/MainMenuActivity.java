package activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import interfaces.PublicConstantValues;
import models.PlayerData;
import network.Client;

/**
 * Created by andre
 */

public class MainMenuActivity extends Activity {

    private PlayerData player;
    private Button btnMultiPlayer, btnPlayerResults;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnMultiPlayer = (Button) findViewById(R.id.btn_multi_player);
        btnPlayerResults = (Button) findViewById(R.id.btn_player_results);

        this.player = null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        player = PlayerData.loadData(this);

        if(player != null) {
            btnMultiPlayer.setEnabled(true);
            btnPlayerResults.setEnabled(true);
        } else {
            btnMultiPlayer.setEnabled(false);
            btnPlayerResults.setEnabled(false);
            Toast.makeText(getApplicationContext(), "Não existe conta criada, todos os dados serão perdidos," +
                    " se quiseres que se guardem cria uma conta pff", Toast.LENGTH_LONG).show();
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
                Intent intent_preferences = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent_preferences);
                break;
            case R.id.item_acerca_de:
                startActivity(new Intent(MainMenuActivity.this, AboutTheAppActivity.class));
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btn_single_player:
                Intent spIntent = new Intent(MainMenuActivity.this, CreateGameActivity.class);
                spIntent.putExtra("mode", PublicConstantValues.SP_MODE);
                startActivity(spIntent);
                break;
            case R.id.btn_multi_player:
                if(checkNetworkConnection())
                    setUpMultiPlayerDialogBox();
                break;
            case R.id.btn_player_results:
                startActivity(new Intent(MainMenuActivity.this, PlayerResultsActivity.class));
                break;
        }
    }

    private boolean checkNetworkConnection(){
        ConnectivityManager connMgr = (ConnectivityManager)	this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            Toast.makeText(this, "No network connection!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void setUpMultiPlayerDialogBox() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainMenuActivity.this);
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("How?");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainMenuActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Create game");
        arrayAdapter.add("Join game");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            switch(which) {
                case 0:
                    Intent mpIntent = new Intent(MainMenuActivity.this, CreateGameActivity.class);
                    mpIntent.putExtra("mode", PublicConstantValues.MP_MODE);
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
        //builderSingle.setIcon(R.drawable.ic_launcher);
        builderSingle.setTitle("Join method:");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainMenuActivity.this,
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("IP:Port");
        arrayAdapter.add("QRCode");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which) {
                    case 0:
                        setUpServerConnectionDetails();
                        break;
                    case 1:
                        Intent jgIntent = new Intent(MainMenuActivity.this, CameraActivity.class);
                        jgIntent.putExtra("cameraMode", PublicConstantValues.QRCODE_PHOTO);
                        startActivityForResult(jgIntent, PublicConstantValues.QRCODE_PHOTO);
                }
            }
        });

        builderSingle.show();
    }

    private void setUpServerConnectionDetails(){
        AlertDialog.Builder dialogAlert = new AlertDialog.Builder(MainMenuActivity.this);
        dialogAlert.setTitle("Insert Server IP:");

        final EditText etServerIP = new EditText(getBaseContext());
        etServerIP.setHint("192.168.1.100");
        etServerIP.setText("192.168.1.100");
        dialogAlert.setView(etServerIP);
        dialogAlert.setPositiveButton("Conectar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String serverIp = "";
                        if(!etServerIP.getText().toString().isEmpty())
                            serverIp = etServerIP.getText().toString();
                        else {
                            Toast.makeText(MainMenuActivity.this, "Server IP insertion error!",
                                    Toast.LENGTH_SHORT).show();
                            setUpServerConnectionDetails();
                        }
                        InformaticsQuizApp iqa = (InformaticsQuizApp)getApplication();
                        Client client = new Client(getApplicationContext(), serverIp,
                                PublicConstantValues.serverListeningPort);
                        iqa.setLocalClient(client);
                        // Lançar uma progressBar animada
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

        dialogAlert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PublicConstantValues.QRCODE_PHOTO:
                if(resultCode == RESULT_OK) {
                    String connectionDetails = data.getStringExtra("connectionDetails");
                    Client client = new Client(this, connectionDetails,
                            PublicConstantValues.serverListeningPort);
                } else {
                    Toast.makeText(this, "Error reading QRCode", Toast.LENGTH_SHORT).show();
                }
        }
    }

}
