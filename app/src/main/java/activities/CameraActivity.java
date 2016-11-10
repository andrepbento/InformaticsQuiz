package activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.andre.informaticsquiz.R;

public class CameraActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getActionBar().hide();
    }
}
