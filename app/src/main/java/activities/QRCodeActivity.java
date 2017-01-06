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
import models.Game;
import models.MySharedPreferences;
import network.Client;
import network.Server;

/**
 * Created by andre
 */

public class QRCodeActivity extends Activity {
    private InformaticsQuizApp app = null;

    public final static int HEIGHT=500;
    public final static int WIDTH=500;

    private String qrCodeData;

    public TextView tvPlayersConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySharedPreferences.loadTheme(this);
        setContentView(R.layout.activity_qrcode);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.qrcode_text);

        final ImageView ivQRCode = (ImageView) findViewById(R.id.iv_qr_code);
        final TextView tvServerDetails = (TextView) findViewById(R.id.tv_server_details);
        tvPlayersConnected = (TextView) findViewById(R.id.tv_players_connected);

        app = (InformaticsQuizApp) getApplication();

        Intent receivedIntent = getIntent();
        Game game = (Game) receivedIntent.getSerializableExtra("game");

        final Server server = new Server(this, game);
        server.execute();
        app.setLocalServer(server);

        // create thread to avoid ANR Exception
        new Thread(new Runnable() {
            public void run() {
                qrCodeData = server.getLocalIpAddress()+" "+server.getServerPort();
                    synchronized (this) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = null;

                                bitmap = encodeAsBitmap(qrCodeData);
                                ivQRCode.setImageBitmap(bitmap);

                                tvServerDetails.setText(getString(R.string.ip_text)+": " + server.getLocalIpAddress()
                                        +" Port: "+server.getServerPort());

                                tvPlayersConnected.setText(R.string.players_connected_text+": "
                                        +server.getPlayersConnected()+"/"+server.getnPlayers());

                                Client client = new Client(QRCodeActivity.this, server.getLocalIpAddress(),
                                        server.getServerPort());
                                client.start();
                                app.setLocalClient(client);
                            }
                        });
                    }
            }
        }).start();
    }

    private Bitmap encodeAsBitmap(String str) {
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

    @Override
    public void onBackPressed() {
        app.getLocalClient().stopClient();
        app.setLocalClient(null);
        app.getLocalServer().stopServer();
        app.setLocalServer(null);
        super.onBackPressed();
    }
}