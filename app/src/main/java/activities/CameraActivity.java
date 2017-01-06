package activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.andre.informaticsquiz.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

import interfaces.Constants;
import models.MySharedPreferences;
import models.MyVibrator;

/**
 * Created by andre
 */

public class CameraActivity extends Activity {
    private FaceDetector faceDetector;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;

    private int cameraMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MySharedPreferences.loadTheme(this);
        setContentView(R.layout.activity_camera);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle(R.string.camera_text);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        cameraView = (SurfaceView)findViewById(R.id.camera_view);

        Intent receivedIntent = getIntent();
        cameraMode = receivedIntent.getIntExtra("cameraMode", 0);

        if(cameraMode == 0) {
            Log.e("Camera", "Error");
            onDestroy();
            finish();
        }

        Button btnTakePic = (Button) findViewById(R.id.btn_take_pic);

        if(cameraMode == Constants.PROFILE_PHOTO) {
            faceDetector = new FaceDetector.Builder(this)
                    .setProminentFaceOnly(true)
                    .build();

            faceDetector.setProcessor(new Detector.Processor<Face>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<Face> detections) {
                }
            });

            btnTakePic.setVisibility(View.VISIBLE);
            cameraSource = new CameraSource
                    .Builder(this, faceDetector)
                    .setRequestedPreviewSize(800, 480)
                    .setAutoFocusEnabled(true)
                    .setFacing(CameraSource.CAMERA_FACING_FRONT)
                    .build();
        } else if(cameraMode == Constants.QRCODE_PHOTO) {
            barcodeDetector = new BarcodeDetector.Builder(this)
                    .setBarcodeFormats(Barcode.QR_CODE)
                    .build();

            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                    if (barcodes.size() != 0) {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("connectionDetails", barcodes.valueAt(0).displayValue);
                        setResult(RESULT_OK, returnIntent);
                        new MyVibrator(CameraActivity.this).vibrate(Constants.VIBRATION_MEDIUM);
                        finish();
                    }
                }
            });

            btnTakePic.setVisibility(View.INVISIBLE);
            cameraSource = new CameraSource
                    .Builder(this, barcodeDetector)
                    .setRequestedPreviewSize(1280, 720)
                    .setAutoFocusEnabled(true)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .build();
        }

        cameraView.getHolder().addCallback(cameraCallback);
    }

    SurfaceHolder.Callback cameraCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                cameraSource.start(cameraView.getHolder());
            } catch (IOException e) {
                Log.e("Camera", e.toString());
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            cameraSource.stop();
        }
    };

    private void stopCamera() {
        if(cameraSource != null)
            cameraSource.stop();
        if(cameraMode == Constants.PROFILE_PHOTO)
            faceDetector.release();
        else if(cameraMode == Constants.QRCODE_PHOTO)
            barcodeDetector.release();
    }

    @Override
    protected void onPause() {
        stopCamera();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                stopCamera();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onButtonTakePickClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_pic:
                cameraSource.takePicture(myShutterCallback, myPictureCallback);
        }
    }

    CameraSource.ShutterCallback myShutterCallback = new CameraSource.ShutterCallback(){
        public void onShutter() {}
    };

    CameraSource.PictureCallback myPictureCallback = new CameraSource.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("pictureData", bytes);
            setResult(RESULT_OK, returnIntent);
            stopCamera();
            finish();
        }
    };
}
