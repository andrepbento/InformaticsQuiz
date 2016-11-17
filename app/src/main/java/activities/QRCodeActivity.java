package activities;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.andre.informaticsquiz.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeActivity extends Activity {

    ImageView ivQRCode;

    String QRcode;
    public final static int WIDTH=500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        getActionBar().hide();

        final ImageView ivQRCode = (ImageView) findViewById(R.id.iv_qr_code);

        // create thread to avoid ANR Exception
        Thread t = new Thread(new Runnable() {
            public void run() {
                // this is the msg which will be encode in QRcode
                QRcode="Test for the Bitmap";

                try {
                    synchronized (this) {
                        wait(5000);
                        // runOnUiThread method used to do UI task in main thread.
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Bitmap bitmap = null;

                                    bitmap = encodeAsBitmap(QRcode);
                                    ivQRCode.setImageBitmap(bitmap);

                                } catch (WriterException e) {
                                    e.printStackTrace();
                                } // end of catch block

                            } // end of run method
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            }
        });
        t.start();
    }

    private Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
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
}