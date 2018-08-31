package com.example.lenovo.login;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.lenovo.login.Ci_GetFilePath.getFilePath;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Ci_MainActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    private final String tag = "VideoServer";

    //Database sqlite
    Ci_DataHelper dbHelper;
    //View
    Button gallery, start, capture,btnStop;;
    SurfaceView surfaceView;
    //camera
    Camera camera;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    //record video
    private MediaRecorder recorder;
    private CamcorderProfile camcorderProfile;
    //namavideo variable
    private String namaVideo;
    //checking variable
    boolean recording = false;
    boolean usecamera = true;
    boolean previewRunning = false;
    //permission ask
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    Boolean isSDPresent;
    //format date
    SimpleDateFormat simpleDateFormat;
    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ci_activity_main);
        askingForPermission();
        initDatabaseSqlLite();
        initView();
        initListener();
        surfaceViewCameraOnPictureTaken();
        initCamera();
        initDateFormat();
        initComs();
        initRecorder();

    }

    private void initCamera() {
        if (usecamera) {
            try{
                camera = Camera.open();
            }catch(RuntimeException e){
                Log.d(tag, "init_camera: " + e);
                return;
            }
            Camera.Parameters param;
            param = camera.getParameters();
            param.setPreviewFrameRate(20);
            param.setPreviewSize(176, 144);
            camera.setParameters(param);
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewRunning = true;
                setCameraDisplayOrientation(Ci_MainActivity.this, 1, camera);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void askingForPermission() {
        permissions.add(CAMERA);
        permissions.add(READ_EXTERNAL_STORAGE);
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(RECORD_AUDIO);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }
    private void initDatabaseSqlLite() {
        dbHelper = new Ci_DataHelper(this);
    }
    private void initView() {
        gallery = (Button) findViewById(R.id.gallery);
        capture = (Button) findViewById(R.id.capture);
        surfaceView = (SurfaceView)findViewById(R.id.surfaceView1);
        start = (Button)findViewById(R.id.btn_start);//for start record
        btnStop = (Button) findViewById(R.id.btn_stop_rec);//for end record
    }
    private void initListener() {
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ci_MainActivity.this, Ci_GalleryActivity.class);
                startActivity(intent);
            }
        });
        start.setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View arg0) {
                start_record();
            }
        });
        capture.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(camera!=null)
                    captureImage();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Ci_MainActivity.this, "Stop Record", Toast.LENGTH_SHORT).show();
                Log.d("RecordingVAL",String.valueOf(recording));
                if (recording) {
                    recorder.stop();
                    if (usecamera) {
                        try {
                            camera.reconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    recording = false;
                    Log.d("namaVideo",String.valueOf(namaVideo));
                    saveVideoNameToDatabase(namaVideo);
                    Toast.makeText(Ci_MainActivity.this, "Saved to db", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void surfaceViewCameraOnPictureTaken() {
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        rawCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("Log", "onPictureTaken - raw");
            }
        };

        /** Handles data for jpeg picture */
        shutterCallback = new Camera.ShutterCallback() {
            public void onShutter() {
                Log.i("Log", "onShutter'd");
            }
        };
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                try {
                    saveImage(setImageRotateBeforeSave(data));//rotate dlu sebelum disave pake function setImageRotateBeforeSave()
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (Exception e) {
                    Log.d("Error", e.toString());
                    e.printStackTrace();
                }
                Log.d("Log", "onPictureTaken - jpeg");
            }
        };
    }
    private void initDateFormat() {
        simpleDateFormat = new SimpleDateFormat("ddMMyyyyhhmmss");
        timeStamp = simpleDateFormat.format(new Date());
    }
    private void initComs() {
        camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        isSDPresent = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    private void captureImage() {
        // TODO Auto-generated method stub
        camera.takePicture(shutterCallback, rawCallback, jpegCallback);//OnPictureTaken(), loot at this function surfaceViewCameraOnPictureTaken
        Toast.makeText(this, "Captured", Toast.LENGTH_SHORT).show();
    }
    private void start_record()
    {
        if (!recording) {
            Toast.makeText(Ci_MainActivity.this, "Start Record", Toast.LENGTH_SHORT).show();
            recording = true;
            try {
                namaVideo = "XYZAppVideo" + "" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()) +".mp4";
                initRecorder();
                prepareRecorder(namaVideo);
                recorder.prepare();// Let's prepareRecorder so we can record again
                recorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (usecamera) {
            try{
                camera = Camera.open();
            }catch(RuntimeException e){
                Log.d(tag, "init_camera: " + e);
                return;
            }
            Camera.Parameters param;
            param = camera.getParameters();
            param.setPreviewFrameRate(20);
            param.setPreviewSize(176, 144);
            camera.setParameters(param);
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewRunning = true;
                setCameraDisplayOrientation(Ci_MainActivity.this, 1, camera);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3) {
        // TODO Auto-generated method stub
        if (!recording && usecamera) {
            if (previewRunning) {
                camera.stopPreview();
            }

            try {
                Camera.Parameters p = camera.getParameters();

                p.setPreviewSize(camcorderProfile.videoFrameWidth,
                        camcorderProfile.videoFrameHeight);
                p.setPreviewFrameRate(camcorderProfile.videoFrameRate);

                camera.setParameters(p);

                camera.setPreviewDisplay(holder);
                camera.startPreview();
                previewRunning = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void initRecorder(){
        recorder = new MediaRecorder();
        recorder.setPreviewDisplay(surfaceHolder.getSurface());
        if (usecamera) {
            camera.unlock();
            recorder.setCamera(camera);
        }
        recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        recorder.setOrientationHint(90);
        recorder.setProfile(camcorderProfile);
    }
    private void prepareRecorder(String namaVideo) {
        try {
            File videoPath = new File(Environment.getExternalStoragePublicDirectory("DCIM").getAbsolutePath(),"AppsVideo");
            if(!videoPath.exists()) videoPath.mkdir();
            recorder.setOutputFile(videoPath +"/" + namaVideo);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    private void saveVideoNameToDatabase(String namaVideo){
        try {
            Log.d("namaVid", namaVideo);
            dbHelper.insertData(namaVideo);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        Log.d("RecordOnSUrfaceDes",String.valueOf(recording));
        if (recording) {
            recorder.stop();
            recording = false;

        }
        recorder.release();
        if (usecamera) {
            previewRunning = false;
            camera.release();
        }
    }
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<String>();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (String perms : permissionsToRequest) {
                    if (hasPermission(perms)) {

                    } else {

                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                                                //Log.d("API123", "permisionrejected " + permissionsRejected.size());

                                                requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }

                }

                break;
        }

    }
    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    //saveimage from raw byte
    public void saveImage(byte[] data) {
        String imageName = String.valueOf( System.currentTimeMillis())+".jpg";//image name
        //save image ke folder
        try {
            FileOutputStream outStream  = new FileOutputStream(getFilePath(imageName));
            outStream.write(data);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //save image name ke db
        dbHelper.insertData(imageName);
    }
    //saveimage with compress to jpeg
    public void saveImage(Bitmap bitmap) {
        String imageName = String.valueOf( System.currentTimeMillis())+".jpg";//image name
        try {
            FileOutputStream outStream  = new FileOutputStream(getFilePath(imageName));
            bitmap.compress(Bitmap.CompressFormat.JPEG,40,outStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //save image name ke db
        dbHelper.insertData(imageName);
    }
    //setcamerarotation
    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, Camera camera) {
        Camera.CameraInfo info =
                new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    //function untuk rotate imagenya
    public Bitmap setImageRotateBeforeSave(byte[] data){
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Notice that width and height are reversed
            Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
            int w = scaled.getWidth();
            int h = scaled.getHeight();
            // Setting post rotate to 90
            Matrix mtx = new Matrix();
            mtx.postRotate(90);
            // Rotating Bitmap
            bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
        }else{// LANDSCAPE MODE
            //No need to reverse width and height
            Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth,screenHeight , true);
            bm=scaled;
        }
        return bm;
    }

}
