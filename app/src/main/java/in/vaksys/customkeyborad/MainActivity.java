package in.vaksys.customkeyborad;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
    private static final int REQUEST_PERMISSION = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, SimpleIME.class);
        startService(intent);

        if (Build.VERSION.SDK_INT >= 23) {
            //do your check here
            requestPermission();
        }

    }


    private void requestPermission() {


        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BIND_INPUT_METHOD)
                != PackageManager.PERMISSION_GRANTED) {
            requestCameraPermission();
        } else {
            Log.e(TAG,
                    "CAMERA permission has already been granted. Displaying camera preview.");
            //  EasyImage.openCamera(MainActivity.this, EasyImageConfig.REQ_TAKE_PICTURE);
        }


//        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//            Log.e("dfjhgfh","edfkuhfu");
//            Toast.makeText(MainActivity.this, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                       REQUEST_PERMISSION);
//        } else {
//
//            Log.e("else","else");
//            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
//        }
    }

    private void requestCameraPermission() {
        Log.e(TAG, "CAMERA permission has NOT been granted. Requesting permission.");

        // BEGIN_INCLUDE(camera_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.BIND_INPUT_METHOD)) {
            Log.e(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.BIND_INPUT_METHOD,Manifest.permission.GET_TASKS},
                    REQUEST_PERMISSION);
        } else {

            Log.e(TAG,
                    "Displaying camera permission rationale to provide additional context.");
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BIND_INPUT_METHOD,Manifest.permission.GET_TASKS},
                    REQUEST_PERMISSION);
        }
        // END_INCLUDE(camera_permission_request)
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        System.out.println(grantResults.length);
        System.out.println(requestCode);
        System.out.println(grantResults[0]);
        if (requestCode == REQUEST_PERMISSION) {

            Log.e(TAG, "Received response for Camera permission request.");
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED ) {
                Log.i(TAG, "CAMERA permission has now been granted. Showing preview.");
//                Snackbar.with(CarDetailsActivity.this)
//                        .type(SnackbarType.MULTI_LINE)
//                        .text("Camera Permission has been granted. Preview can now be opened.")
//                        .duration(com.nispok.snackbar.Snackbar.SnackbarDuration.LENGTH_SHORT)
//                        .swipeToDismiss(false)
//                        .show(CarDetailsActivity.this);
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        EasyImage.openCamera(CarDetailsActivity.this, EasyImageConfig.REQ_TAKE_PICTURE);
//                    }
//                }, 1000);

            } else {
                Log.e(TAG, "CAMERA permission was NOT granted.");

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        System.out.println(grantResults.length);
//        System.out.println(requestCode);
//        System.out.println(grantResults[0]);
//
//        switch (requestCode) {
//
//
//
//            case PERMISSION_REQUEST_CODE:
//
//                final int numOfRequest = grantResults.length;
//
//
//                if (grantResults.length == 1 && grantResults[numOfRequest -1] == PackageManager.PERMISSION_GRANTED) {
//
//                    Toast.makeText(MainActivity.this, "Permission Granted, Now you can access location data.", Toast.LENGTH_LONG).show();
//
////                    Snackbar.make(view, "Permission Granted, Now you can access location data.", Snackbar.LENGTH_LONG).show();
//
//                } else {
//
//                    Toast.makeText(MainActivity.this, "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();
//
////                    Snackbar.make(view, "Permission Denied, You cannot access location data.", Snackbar.LENGTH_LONG).show();
//
//                }
//                break;
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (data != null && data.getData() != null && resultCode == RESULT_OK) {
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
//                    && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BIND_INPUT_METHOD) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BIND_INPUT_METHOD},
//                        REQUEST_PERMISSION);
//
//            }
//
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }

}

