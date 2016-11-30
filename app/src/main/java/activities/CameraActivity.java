package activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.andre.informaticsquiz.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.IOException;

import interfaces.PublicConstantValues;
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
        setContentView(R.layout.activity_camera);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        cameraView = (SurfaceView)findViewById(R.id.camera_view);

        Intent receivedIntent = getIntent();
        cameraMode = receivedIntent.getIntExtra("cameraMode", 0);

        if(cameraMode == 0) { Log.e("Camera", "Error"); finish(); }

        Button btnTakePic = (Button) findViewById(R.id.btn_take_pic);

        if(cameraMode == PublicConstantValues.PROFILE_PHOTO) {
            faceDetector = new FaceDetector.Builder(this)
                    .setProminentFaceOnly(true)
                    .build();

            btnTakePic.setVisibility(View.VISIBLE);
            cameraSource = new CameraSource
                    .Builder(this, faceDetector)
                    .setRequestedPreviewSize(640, 480)
                    .setAutoFocusEnabled(true)
                    .setFacing(CameraSource.CAMERA_FACING_FRONT)
                    .build();
        } else if(cameraMode == PublicConstantValues.QRCODE_PHOTO) {
            barcodeDetector = new BarcodeDetector.Builder(this)
                            .setBarcodeFormats(Barcode.QR_CODE)
                            .build();

            btnTakePic.setVisibility(View.INVISIBLE);
            cameraSource = new CameraSource
                    .Builder(this, barcodeDetector)
                    .setRequestedPreviewSize(1280, 720)
                    .setAutoFocusEnabled(true)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .build();
        }

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ie) {
                    Log.e("Camera", ie.getMessage());
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        if(cameraMode == PublicConstantValues.PROFILE_PHOTO) {
            faceDetector.setProcessor(new Detector.Processor<Face>() {
                @Override
                public void release() {
                }

                @Override
                public void receiveDetections(Detector.Detections<Face> detections) {
                    final SparseArray<Face> faces = detections.getDetectedItems();
                }
            });
        } else if(cameraMode == PublicConstantValues.QRCODE_PHOTO) {
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
                        new MyVibrator(CameraActivity.this).vibrate(PublicConstantValues.VIBRATION_MEDIUM);
                        finish();
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.release();
        if(cameraMode == PublicConstantValues.PROFILE_PHOTO) faceDetector.release();
        else if(cameraMode == PublicConstantValues.QRCODE_PHOTO) barcodeDetector.release();
    }

    public void onButtonTakePickClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_pic:
                Toast.makeText(this, "Implementar onButtonTakePickClick()", Toast.LENGTH_LONG)
                        .show();
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
        }
    }

}
