package activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;

import interfaces.PublicConstantValues;
import models.PlayerData;
import network.Client;

public class MainMenuActivity extends Activity {

    private PlayerData player;

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

        player = PlayerData.loadData(this);

        if(player != null) {
            btnMultiPlayer.setEnabled(true);
            btnPlayerStatisctic.setEnabled(true);
            //if(player.getPhoto() != null)
            //    menu_main_menu.findItem(R.id.item_user).setIcon((Drawable)new BitmapDrawable(player.getPhoto()));
            //else
            //    menu_main_menu.findItem(R.id.item_user).setIcon(R.drawable.drawable_user);
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
        inflater.inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_user:
                startActivity(new Intent(MainMenuActivity.this, PlayerProfileActivity.class));
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
                Intent spIntent = new Intent(MainMenuActivity.this, CreateGameActivity.class);
                spIntent.putExtra("mode", PublicConstantValues.SP_MODE);
                startActivity(spIntent);
                break;
            case R.id.btn_multi_player:
                setUpMultiPlayerDialogBox();
                break;
            case R.id.btn_player_statistics:
                startActivity(new Intent(MainMenuActivity.this, PlayerResultsActivity.class));
                break;
        }
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
                        //setUpConnectionDetails();
                        Client client = new Client(MainMenuActivity.this, "192.168.1.75", 9800);
                        client.execute();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PublicConstantValues.QRCODE_PHOTO:
                if(resultCode == RESULT_OK) {
                    String connectionDetails = data.getStringExtra("connectionDetails");
                    connectionDetails.trim();
                    String[] connectionDetailsArray = connectionDetails.split(" ");
                    Client client = new Client(this, connectionDetailsArray[0],
                            Integer.parseInt(connectionDetailsArray[1]));
                    client.execute();
                } else {
                    Toast.makeText(this, "Error reading QRCode", Toast.LENGTH_SHORT).show();
                }
        }
    }

}
