package activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andre.informaticsquiz.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import application.InformaticsQuizApp;
import interfaces.PublicConstantValues;
import models.Game;
import network.Client;
import network.Server;

/**
 * Created by andre
 */

public class QRCodeActivity extends Activity {

    public final static int HEIGHT=500;
    public final static int WIDTH=500;

    String QRcode;

    public TextView tvPlayersConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.qrcode_text);

        final ImageView ivQRCode = (ImageView) findViewById(R.id.iv_qr_code);
        final TextView tvServerDetails = (TextView) findViewById(R.id.tv_server_details);
        tvPlayersConnected = (TextView) findViewById(R.id.tv_players_connected);

        final InformaticsQuizApp iqa = (InformaticsQuizApp) getApplication();

        Intent receivedIntent = getIntent();
        int nPlayers = receivedIntent.getIntExtra("nPlayers", 0);
        Game game = (Game) receivedIntent.getSerializableExtra("game");

        final Server server = new Server(this, nPlayers, game);
        iqa.setLocalServer(server);

        // create thread to avoid ANR Exception
        Thread t = new Thread(new Runnable() {
            public void run() {
                // this is the msg which will be encode in QRcode
                QRcode = server.getLocalIpAddress() + " " + server.getListeningPort();

                try {
                    synchronized (this) {
                        wait(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bitmap = null;

                                    bitmap = encodeAsBitmap(QRcode);
                                    ivQRCode.setImageBitmap(bitmap);

                                    tvServerDetails.setText("IP: " + server.getLocalIpAddress()
                                            + "   Port: " + server.getListeningPort());

                                    tvPlayersConnected.setText("Players connected: 0/" + server.getnPlayers());

                                    Client client = new Client(getApplicationContext(), server.getLocalIpAddress(),
                                            PublicConstantValues.listeningPort);
                                    iqa.setLocalClient(client);
                                } catch (WriterException e) {
                                    e.printStackTrace();
                                }
                            } // end of run method
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();

        /*

        Thread launchClientThread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        launchClientThread.start();
        */
    }

    private Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, HEIGHT, WIDTH, null);
            int w = result.getWidth();
            int h = result.getHeight();
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                int offset = y * w;
                for (int x = 0; x < w; x++)
                    pixels[offset + x] = result.get(x, y) ?
                            getResources().getColor(R.color.black) : getResources().getColor(R.color.white);
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, 500, 0, 0, w, h);
            return bitmap;
        } catch (IllegalArgumentException e) {
            // Unsupported format
            Log.e("QRCode", e.getMessage());
            return null;
        } catch (WriterException e) {
            Log.e("QRCode", e.getMessage());
            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}