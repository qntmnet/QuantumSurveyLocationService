package com.qntm.qntm_survey;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.POST_NOTIFICATIONS;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivityQN extends AppCompatActivity implements OnSuccessListener<AppUpdateInfo> {


    SharedPreferences sp_userdetail;
    private static AsyncHttpClient client ;
    connectionDector dector;
    String Str_Userid;
    String Str_UserName;
    String Str_Password;
    String Str_Rudder_Id;
    String Str_cloud_id;
    String Str_two_fa_token;
    String Str_twofa_status;
    String Str_debugmode;

    String Str_StunQn_Ip;
    String Str_StunQn_Port;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private AppUpdateManager appUpdateManager;
    double version, play_version;
    String MainStr_Version;
    public AlertDialog d;
    private boolean mNeedsFlexibleUpdate;
    public static final int REQUEST_CODE = 1234;
    private static final int LOCK_REQUEST_CODE = 221;
    //Application mApplication;
    String Str_Stun_Server_list_data="";
    String Str_Device_uuid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_q_n);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        client= new AsyncHttpClient();
        dector=new connectionDector(SplashActivityQN.this);
        sp_userdetail= getSharedPreferences("userdetail.txt", Context.MODE_PRIVATE);


        appUpdateManager = AppUpdateManagerFactory.create(this);

        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = Double.parseDouble(info.versionName);
        Log.d("current VersiononCreate",""+version);


     /*   Str_Device_uuid = Settings.Secure.getString(SplashActivityQN.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d("device_uuid","device_uuid main "+Str_Device_uuid);*/

       // getFCMKey();

        ImageView img_test = findViewById(R.id.img_test);
        img_test.setOnClickListener(v -> {
           // myPermissions();
        });
       // myPermissions();

    }



  /*  private void getFCMKey(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(result -> {
            if(result != null){
                PreferencesUtils.setFCMPushKey(SplashActivityQN.this, result);

                Log.d("fcmtoken", result);
                // DO your thing with your firebase token
            }
        });
    }*/

    private final ActivityResultLauncher<String[]> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permission ->{
                boolean allGranted = true;

                for (Boolean isGranted : permission.values()){
                    if (!isGranted){
                        allGranted = false;
                        break;
                    }
                }

                if (allGranted){
                    // All is granted
                    Log.d("TEST","OK");
                } else {
                    // All is not granted
                    Log.d("TEST","FAIL");
                   // myPermissions();
                    showSettingsDialog();

                }

            });

    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivityQN.this);

        // below line is the title for our alert dialog.
        builder.setTitle("Need Permissions");

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            // this method is called on click on positive button and on clicking shit button
            // we are redirecting our user from our app to the settings page of our app.
            dialog.cancel();
            // below is the intent from which we are redirecting our user.
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        });
       /* builder.setNegativeButton("Cancel", (dialog, which) -> {
            // this method is called when user click on negative button.
            myPermissions();
            dialog.cancel();

        });*/
        // below line is used to display our dialog
        builder.show();
    }
    private void myPermissions(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){

            String[] permissions = new String[]{
                    android.Manifest.permission.READ_MEDIA_IMAGES,
                    android.Manifest.permission.READ_MEDIA_AUDIO,
                    android.Manifest.permission.READ_MEDIA_VIDEO,
                    POST_NOTIFICATIONS,
                    android.Manifest.permission.CAMERA,
            };


            List<String> permissionsTORequest = new ArrayList<>();
            for (String permission : permissions){
                if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                    permissionsTORequest.add(permission);
                }
            }

            if (permissionsTORequest.isEmpty()){
                // All permissions are already granted
                Toast.makeText(this, "All permissions are already granted", Toast.LENGTH_SHORT).show();


            } else {
                String[] permissionsArray = permissionsTORequest.toArray(new String[0]);
                boolean shouldShowRationale = false;

                for (String permission : permissionsArray){
                    if (shouldShowRequestPermissionRationale(permission)){
                        shouldShowRationale = true;
                        break;
                    }
                }
                Log.d("shouldShowRationale", String.valueOf(shouldShowRationale));
                if (shouldShowRationale){
                    new AlertDialog.Builder(this)
                            .setMessage("Please allow all permissions")
                            .setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissionLauncher.launch(permissionsArray);
                                }
                            })

                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                   // myPermissions();
                                }
                            })
                            .show();

                } else {
                    requestPermissionLauncher.launch(permissionsArray);
                }


            }


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = new String[]{
                    CAMERA,
            };


            List<String> permissionsTORequest = new ArrayList<>();
            for (String permission : permissions){
                if (ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED){
                    permissionsTORequest.add(permission);
                }
            }

            if (permissionsTORequest.isEmpty()){
                // All permissions are already granted
                Toast.makeText(this, "All permissions are already granted", Toast.LENGTH_SHORT).show();


            } else {
                String[] permissionsArray = permissionsTORequest.toArray(new String[0]);
                boolean shouldShowRationale = false;

                for (String permission : permissionsArray){
                    if (shouldShowRequestPermissionRationale(permission)){
                        shouldShowRationale = true;
                        break;
                    }
                }

                if (shouldShowRationale){
                    new AlertDialog.Builder(this)
                            .setMessage("Please allow all permissions")
                            .setCancelable(false)
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissionLauncher.launch(permissionsArray);
                                }
                            })

                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();

                } else {
                    requestPermissionLauncher.launch(permissionsArray);
                }


            }


        }


    } // myPermissions end here ================
    @Override
    protected void onResume() {
        super.onResume();
       // myPermissions();
        if (checkPermission())
        {
            //16f7bfea1e0e3d86
            //16f7bfea1e0e3d86


            String androidIds = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
            Log.d("device_uuid","androidIds mains "+androidIds);
            //Toast.makeText(SplashActivityQN.this,"device_uuid : "+androidIds,Toast.LENGTH_SHORT).show();


            if(dector.checkNetwork())
            {

                mainsetData();


                //mainsetData();
                //getlogindata();
            }
            else
            {
                //Toast.makeText(SplashActivityQN.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SplashActivityQN.this, ErrorPage_Qn_Activtiy.class);
                startActivity(i);
            }

        }
        else
        {
            requestPermission();
            Log.d("checkAndRequestP","checkAndRequestPermissions");

        }


    }







    public void mainsetData()
    {

        if (checkPermission())
        {
            Log.d("permission", "granted");


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        //if (checkAndRequestPermissions()) {
                        // carry on the normal flow, as the case of  permissions  granted.
                        if(dector.checkNetwork())
                        {
                            Log.d("internet", "Available");

                            // new AutologinTask().execute();

                            Intent i = new Intent(SplashActivityQN.this, Login_Activity.class);
                            startActivity(i);


                        }

                        else
                        {
                                /*config_hrp.ShowToastMessage_No_Connection(SplashActivity.this);
                                Intent i = new Intent(SplashActivity.this, ErrorPageActivtiy.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);*/

                            Toast.makeText(SplashActivityQN.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }, 100);
        }
        else
        {
            requestPermission();
        }




    }


/*    public void getlogindata()
    {
        Log.d("call","logindata_urlmain");
        if(sp_userdetail.contains("user_name") && sp_userdetail.contains("password"))
        {


            if(sp_userdetail.contains("Str_Base_url_qn_stage_beta"))
            {
                ConfigQn.Main_URL=sp_userdetail.getString("Str_Base_url_qn_stage_beta",null);
            }

            Log.d("call","sharepreferences_data");

            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    try {
                        Str_UserName=sp_userdetail.getString("user_name",null);
                        Str_Password=sp_userdetail.getString("password",null);
                        Str_Rudder_Id=sp_userdetail.getString("rudder_id",null);

                        JSONObject Rider_reg_info_Jobj=new JSONObject();

                        try {
                            Str_Device_uuid = Settings.Secure.getString(SplashActivityQN.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                            Log.d("device_uuid",Str_Device_uuid);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }



                               *//* Rider_reg_info_Jobj.put("username",edt_username.getText().toString());
                                Rider_reg_info_Jobj.put("password",edt_password.getText().toString());
                                Rider_reg_info_Jobj.put("rudder_id",Str_Rudder_Id);*//*
                      *//*  Rider_reg_info_Jobj.put("username",Str_UserName);
                        Rider_reg_info_Jobj.put("password",Str_Password);
                        Rider_reg_info_Jobj.put("rudder_id",Str_Rudder_Id);
                        Rider_reg_info_Jobj.put("peer_identity",Str_Device_uuid);
                        Rider_reg_info_Jobj.put("is_qim","1");*//*
                        Rider_reg_info_Jobj.put("username",Str_UserName);
                        Rider_reg_info_Jobj.put("password",Str_Password);

                        Log.d("Qnlogin Jason",Rider_reg_info_Jobj.toString());



                        Set_Rider_Register_data(Rider_reg_info_Jobj);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }, 500);

        }
        else
        {
            Log.d("call","Login");
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i=new Intent(SplashActivityQN.this, Login_Test_Activity.class);
                    startActivity(i);
                }
            }, 1000);



        }



    }




    public class AutologinTask extends AsyncTask<Void,Void,String>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
				*//*runOnUiThread(new Runnable() {
					public void run() {


					}
				});
*//*
            VersionCheck_datainClass versionChecker1 = new VersionCheck_datainClass();


            try {



                MainStr_Version = versionChecker1.execute().get();
                Log.d("MainStr_Version",MainStr_Version);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        @Override
        protected String doInBackground(Void... voids) {
            //VersionChecker versionChecker = new VersionChecker();
            //VersionCheck_data versionChecker1 = new VersionCheck_data();
            try {


                //String str=versionChecker.execute().get();
                //String str=new CheckUpdate().check("com.zen.zenexim",SplashScreen.this);

                // String str = versionChecker1.execute().get();
                //MainStr_Version = versionChecker1.execute().get();

                if(MainStr_Version!=null && MainStr_Version.length()>0)
                {
                    play_version = Double.parseDouble(MainStr_Version);
                    //play_version = 2.8;
                    if(String.valueOf(play_version)!=null) {

                        Log.d("playStoreVersion", "" + play_version);


                        return  String.valueOf(play_version);
                    }
                    else
                    {
                        Log.d("playStoreVersion1", "null");
                    }
                }else
                {
                    Log.d("playStoreVersion2", "null");
                }

            } catch (Exception e) {

                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(play_version>version)
            {
                Log.d("call","version update");
                Log.d("message","its letest version");
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivityQN.this);
                builder.setTitle("Alert");
                builder.setMessage("Letest Version of QNUnGrid is Available.");
                builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    }
                });
                builder.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
            else
            {

                Log.d("call_underlollipop","Letest Version Install");
                getlogindata();

            }
        }
    }




    public class VersionCheck_datainClass extends AsyncTask<Void, Void, String> {



        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id="+"com.qn.qam" + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (org.jsoup.nodes.Element ele : element)
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (org.jsoup.nodes.Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return newVersion;

        }


        @Override

        protected void onPostExecute(String onlineVersion) {

            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (Float.valueOf((float)version) < Float.valueOf(onlineVersion)) {
                    //show anything
                }

            }

            Log.d("update", "Current version " + version + "playstore version " + onlineVersion);

        }
    }*/




    @Override
    public void onSuccess(AppUpdateInfo appUpdateInfo)
    {
        if(dector.checkNetwork())
        {
            if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // If an in-app update is already running, resume the update.
                Log.d("Call_appupdate", "updateAvailability()");
                startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                Log.d("Call_appupdate", "popupSnackbarForCompleteUpdate()");
                popupSnackbarForCompleteUpdate();
            } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                Log.d("Call_appupdate", "updateAvailability()==UPDATE_AVAILABLE");
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    Log.d("Call_appupdate", "isUpdateTypeAllowed()==IMMEDIATE");
                    startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    Log.d("Call_appupdate", "isUpdateTypeAllowed()==FLEXIBLE");
                    mNeedsFlexibleUpdate = true;
                    showFlexibleUpdateNotification();
                }
            }
            else
            {
                Log.d("Call_appupdate", "update not_available");
              /*  if (checkAndRequestPermissions())
                {*/
                if(dector.checkNetwork())
                {
                    Log.d("internet", "Available");

                    Intent i = new Intent(SplashActivityQN.this, Login_Activity.class);
                    startActivity(i);





                }
                else
                 {
                     Toast.makeText(SplashActivityQN.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    /*Toast.makeText(SplashActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SplashActivity.this,ErrorScreen.class);
                    startActivity(intent);*/


                }
                // }
            }
        }
        else
        {
            Log.d("internet", "NotAvailable");

            Toast.makeText(SplashActivityQN.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            /*Toast.makeText(SplashActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(SplashActivity.this,ErrorScreen.class);
            startActivity(intent);*/
        }

    }








    private void startUpdate(final AppUpdateInfo appUpdateInfo, final int appUpdateType) {
        final Activity activity = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                            appUpdateType,
                            activity,
                            REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.main_activity),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.maingreen));
        snackbar.show();
    }

    private void showFlexibleUpdateNotification() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.main_activity),
                        "An update is available and accessible in More.",
                        Snackbar.LENGTH_LONG);
        snackbar.show();
    }




    private  boolean checkAndRequestPermissions()
    {
        int phonestatePermission = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);
        int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int Location_fine_per = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
        int Location_corse_per = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);



        List<String> listPermissionsNeeded = new ArrayList<>();

        if (phonestatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(READ_PHONE_STATE);
        }
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (Location_fine_per != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(ACCESS_FINE_LOCATION);
        }

        if (Location_corse_per != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    private boolean checkPermission()
    {
      //  Environment.isExternalStorageManager() &&
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU){
           // int post_notification = ContextCompat.checkSelfPermission(SplashActivityQN.this, POST_NOTIFICATIONS);
            int cameraPermission = ContextCompat.checkSelfPermission(SplashActivityQN.this, CAMERA);
            int Location_fine_per = ContextCompat.checkSelfPermission(SplashActivityQN.this, ACCESS_FINE_LOCATION);
            int Location_corse_per = ContextCompat.checkSelfPermission(SplashActivityQN.this, Manifest.permission.ACCESS_COARSE_LOCATION);
           // return  ((phonestatePermission == PackageManager.PERMISSION_GRANTED)&& (Location_fine_per == PackageManager.PERMISSION_GRANTED) && (Location_corse_per == PackageManager.PERMISSION_GRANTED) && (post_notification == PackageManager.PERMISSION_GRANTED));
           return  ((Location_fine_per == PackageManager.PERMISSION_GRANTED) && (Location_corse_per == PackageManager.PERMISSION_GRANTED) && (cameraPermission == PackageManager.PERMISSION_GRANTED));
        }else{
            if (SDK_INT >= Build.VERSION_CODES.R)
            {
                //int result = ContextCompat.checkSelfPermission(Splash_Screen.this, CAMERA);
               // int phonestatePermission = ContextCompat.checkSelfPermission(SplashActivityQN.this, READ_PHONE_STATE);
                int cameraPermission = ContextCompat.checkSelfPermission(SplashActivityQN.this, CAMERA);
                int Location_fine_per = ContextCompat.checkSelfPermission(SplashActivityQN.this, ACCESS_FINE_LOCATION);
                int Location_corse_per = ContextCompat.checkSelfPermission(SplashActivityQN.this, Manifest.permission.ACCESS_COARSE_LOCATION);
               // return  ((phonestatePermission == PackageManager.PERMISSION_GRANTED)&& (Location_fine_per == PackageManager.PERMISSION_GRANTED) && (Location_corse_per == PackageManager.PERMISSION_GRANTED));
                return  ((Location_fine_per == PackageManager.PERMISSION_GRANTED) && (Location_corse_per == PackageManager.PERMISSION_GRANTED) && (cameraPermission == PackageManager.PERMISSION_GRANTED));
            } else {
              //  int phonestatePermission = ContextCompat.checkSelfPermission(SplashActivityQN.this, READ_PHONE_STATE);
                int cameraPermission = ContextCompat.checkSelfPermission(SplashActivityQN.this, CAMERA);
                int Location_fine_per = ContextCompat.checkSelfPermission(SplashActivityQN.this, ACCESS_FINE_LOCATION);
                int Location_corse_per = ContextCompat.checkSelfPermission(SplashActivityQN.this, Manifest.permission.ACCESS_COARSE_LOCATION);
              //  int storage_read = ContextCompat.checkSelfPermission(SplashActivityQN.this, READ_EXTERNAL_STORAGE);
               // int storage_write = ContextCompat.checkSelfPermission(SplashActivityQN.this, WRITE_EXTERNAL_STORAGE);
               // return phonestatePermission == PackageManager.PERMISSION_GRANTED && Location_fine_per == PackageManager.PERMISSION_GRANTED && Location_corse_per == PackageManager.PERMISSION_GRANTED && storage_read == PackageManager.PERMISSION_GRANTED && storage_write == PackageManager.PERMISSION_GRANTED;
                return  ((Location_fine_per == PackageManager.PERMISSION_GRANTED) && (Location_corse_per == PackageManager.PERMISSION_GRANTED) && (cameraPermission == PackageManager.PERMISSION_GRANTED));
            }
        }

    }


    private void requestPermission() {
        if(SDK_INT >= Build.VERSION_CODES.TIRAMISU){
         //   int post_notification = ContextCompat.checkSelfPermission(SplashActivityQN.this, POST_NOTIFICATIONS);
         //   int phonestatePermission = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);
            int cameraPermission = ContextCompat.checkSelfPermission(SplashActivityQN.this, CAMERA);
            int Location_fine_per = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
            int Location_corse_per = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            /*if(!Environment.isExternalStorageManager())
            {
                try {

                    //ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                    startActivityForResult(intent, 2296);
                } catch (Exception e) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                    startActivityForResult(intent, 2296);
                }
            }
            else*/ if(/*!(phonestatePermission == PackageManager.PERMISSION_GRANTED) &&*/ !(Location_fine_per == PackageManager.PERMISSION_GRANTED) && !(Location_corse_per == PackageManager.PERMISSION_GRANTED) && !(cameraPermission == PackageManager.PERMISSION_GRANTED))
            {

                List<String> listPermissionsNeeded = new ArrayList<>();

              /*  if (post_notification != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(POST_NOTIFICATIONS);
                }
                if (phonestatePermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(READ_PHONE_STATE);
                }*/

                if (Location_fine_per != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(ACCESS_FINE_LOCATION);
                }

                if (Location_corse_per != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
                if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(CAMERA);
                }

                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
                }
            }
            else
            {

            }
        }else{
            if (SDK_INT >= Build.VERSION_CODES.R)
            {
                //int result = ContextCompat.checkSelfPermission(SplashActivityQN.this, CAMERA);
                ///int phonestatePermission = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);
                int cameraPermission = ContextCompat.checkSelfPermission(SplashActivityQN.this, CAMERA);
                int Location_fine_per = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
                int Location_corse_per = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
               /* if(!Environment.isExternalStorageManager())
                {
                    try {

                        //ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                        startActivityForResult(intent, 2296);
                    } catch (Exception e) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                        startActivityForResult(intent, 2296);
                    }
                }*/
                 if(/*!(phonestatePermission == PackageManager.PERMISSION_GRANTED) && */!(Location_fine_per == PackageManager.PERMISSION_GRANTED) && !(Location_corse_per == PackageManager.PERMISSION_GRANTED) && !(cameraPermission == PackageManager.PERMISSION_GRANTED))
                {

                    List<String> listPermissionsNeeded = new ArrayList<>();

                   /* if (phonestatePermission != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(READ_PHONE_STATE);
                    }*/

                    if (Location_fine_per != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(ACCESS_FINE_LOCATION);
                    }

                    if (Location_corse_per != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                    }
                    if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(CAMERA);
                    }

                    if (!listPermissionsNeeded.isEmpty()) {
                        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
                    }
                }
                else
                {

                }


            }
            else
            {
               // int phonestatePermission = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);
               // int permissionStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int cameraPermission = ContextCompat.checkSelfPermission(SplashActivityQN.this, CAMERA);
                int Location_fine_per = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
                int Location_corse_per = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

                List<String> listPermissionsNeeded = new ArrayList<>();

              /*  if (phonestatePermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(READ_PHONE_STATE);
                }
                if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }*/

                if (Location_fine_per != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(ACCESS_FINE_LOCATION);
                }

                if (Location_corse_per != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }
                if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(CAMERA);
                }

                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
                }
            }
        }

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE)
        {
            if (resultCode == RESULT_OK) {
                Log.i(SplashActivityQN.class.getSimpleName(), "Update flow completed! Result code: " + resultCode);
            } else {
                Log.e(SplashActivityQN.class.getSimpleName(), "Update flow failed! Result code: " + resultCode);
            }
        }

        else if (requestCode == 2296)
        {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager())
                {

                   // int phonestatePermission = ContextCompat.checkSelfPermission(this, READ_PHONE_STATE);
                    int cameraPermission = ContextCompat.checkSelfPermission(SplashActivityQN.this, CAMERA);
                    int Location_fine_per = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION);
                    int Location_corse_per = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
                    List<String> listPermissionsNeeded = new ArrayList<>();

                    if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(CAMERA);
                    }

                    if (Location_fine_per != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(ACCESS_FINE_LOCATION);
                    }

                    if (Location_corse_per != PackageManager.PERMISSION_GRANTED) {
                        listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                    }

                    if (!listPermissionsNeeded.isEmpty()) {
                        ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
                    }


                } else {
                    Toast.makeText(this, "Please enable permission for storage access ", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions

                perms.put(CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && perms.get(ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("call", "permisiongranted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("call", "permissions not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_COARSE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
                            showDialogOK("Read Phone Permission required for this app",
                                    (dialog, which) -> {
                                        switch (which) {
                                            case DialogInterface.BUTTON_POSITIVE:
                                                checkAndRequestPermissions();
                                                break;
                                            case DialogInterface.BUTTON_NEGATIVE:
                                                // proceed with logic by disabling the related features or quit the app.
                                                break;
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }


   /* @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions

                perms.put(READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        Log.d("call","permisiongranted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    }
                    else
                    {
                        Log.d("call", "permissions not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA ) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION ) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA ))
                        {
                            showDialogOK("Read Phone Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

*/

/*    private void Set_Rider_Register_data(JSONObject jsonObject) throws UnsupportedEncodingException
    {
       *//* final Dialog dialog;


        dialog = new Dialog(SplashActivityQN.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        // Set dialog title
        // dialog.setTitle("Custom Dialog");
        dialog.show();*//*

        RequestParams params = new RequestParams();
        params.put("",jsonObject);
        // StringEntity entity = new StringEntity(params.toString());
        final ByteArrayEntity entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        //  params.put("Password",jsonpassword);
      //  final String url= ConfigQn.Main_URL+"verify_qam_user";
        final String url= ConfigQn.Temp_Main_URL+"/login";
        //final String url= "http://192.168.2.109/api/login";
        Log.d("url",url);
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MyCustomSSLFactory socketFactory = new MyCustomSSLFactory(trustStore);
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(socketFactory);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        client.setTimeout(20*1000);
        client.post(SplashActivityQN.this,url,entity,"application/json",new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("res",response.toString());

                try{
                  *//*  if(response.length()>0)
                    {

                       *//**//* dialog.dismiss();
                        dialog.cancel();*//**//*
                        if(response.getInt("responseCode")==200)
                        {

                            //Toast.makeText(SplashActivityQN.this, response.getString("responseMessage"), Toast.LENGTH_LONG).show();

                            JSONArray datarray=response.getJSONArray("data");
                            JSONObject stun_obj=response.getJSONObject("stun1");
                            Str_two_fa_token=response.getString("two_fa_token");
                            Str_twofa_status=response.getString("twofa_status");
                            Str_debugmode=response.getString("debugmode");

                            Str_StunQn_Ip=stun_obj.getString("ip");
                            Str_StunQn_Port=stun_obj.getString("port");


                            JSONArray stun_array=response.getJSONArray("stun");


                            JSONArray stun_server_data = new JSONArray();
                            for(int i=0;i<stun_array.length();i++)
                            {


                                JSONObject obj_s=stun_array.getJSONObject(i);
                                JSONObject stunsr_obj=new JSONObject();
                                stunsr_obj.put("ip",obj_s.getString("ip"));
                                stunsr_obj.put("port",obj_s.getString("port"));

                                stun_server_data.put(stunsr_obj);
                            }

                            Log.d("stun_server_data",stun_server_data.toString());
                            Str_Stun_Server_list_data=stun_server_data.toString();
                            Log.d("str_stun_server",Str_Stun_Server_list_data);





                            if(datarray.length()>0)
                            {
                                JSONObject jsonObject=datarray.getJSONObject(0);

                                Str_Userid=jsonObject.getString("user_id");
                                Str_cloud_id=jsonObject.getString("cloud_id");



                                if(Str_Userid.length()>0)
                                {

                                    String privateKey;
                                    String publickey;
                                    String ListenPort;
                                  *//**//*  KeyPair keyPair=new KeyPair();
                                    privateKey=keyPair.getPrivateKey().toBase64();
                                    publickey=keyPair.getPublicKey().toBase64();*//**//*



                                    sp_userdetail= getSharedPreferences("userdetail.txt", Context.MODE_PRIVATE);

                                    SharedPreferences.Editor editor_data= sp_userdetail.edit();
                                    editor_data.putString("user_id",Str_Userid);
                                    editor_data.putString("user_name",Str_UserName);
                                    editor_data.putString("password",Str_Password);
                                    editor_data.putString("rudder_id",Str_Rudder_Id);
                                    editor_data.putString("cloud_id",Str_cloud_id);
                                    editor_data.putString("Str_StunQn_Ip",Str_StunQn_Ip);
                                    editor_data.putString("Str_StunQn_Port",Str_StunQn_Port);
                                    editor_data.putString("two_fa_token",Str_two_fa_token);
                                    editor_data.putString("twofa_status",Str_twofa_status);
                                    editor_data.putString("Str_debugmode",Str_debugmode);
                                    editor_data.putString("Str_Stun_Server_list_data",Str_Stun_Server_list_data);

                                 *//**//*   editor_data.putString("privateKey",privateKey);
                                    editor_data.putString("publickey",publickey);*//**//*
                                    editor_data.commit();


                                    //startActivity(new Intent(SplashActivityQN.this,MainDashboard_Activity_QnUnGrid.class));
                                    startActivity(new Intent(SplashActivityQN.this, Qam_Dashboard_Activity.class));






                                }


                            }




                        }  else
                        {
                            Toast.makeText(SplashActivityQN.this, response.getString("responseMessage"), Toast.LENGTH_LONG).show();

                            SharedPreferences sp_detail=getSharedPreferences("userdetail.txt", MODE_PRIVATE);
                            SharedPreferences.Editor editor=sp_detail.edit();
                            editor.clear();
                            editor.commit();


                            finish();
                            Intent intent=new Intent(SplashActivityQN.this, Rudder_IdName_Activity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                            startActivity(intent);
                        }
                    }
                    else
                    {
                        Toast.makeText(SplashActivityQN.this, "something went wrong", Toast.LENGTH_LONG).show();
                    }*//*

                    if(response.length()>0){

                        if(response.getInt("responseCode")==200){
                            SharedPreferences.Editor editor_data= sp_userdetail.edit();
                            editor_data.putString("token",response.getString("token"));
                            editor_data.putString("user_name",Str_UserName);
                            editor_data.putString("password",Str_Password);
                          *//*  editor_data.putString("rudder_id",Str_Rudder_Id);
                            editor_data.putString("cloud_id",Str_cloud_id);
                            editor_data.putString("Str_StunQn_Ip",Str_StunQn_Ip);
                            editor_data.putString("Str_StunQn_Port",Str_StunQn_Port);
                            editor_data.putString("two_fa_token",Str_two_fa_token);
                            editor_data.putString("twofa_status",Str_twofa_status);
                            editor_data.putString("Str_debugmode",Str_debugmode);
                            editor_data.putString("Str_Stun_Server_list_data",Str_Stun_Server_list_data);*//*

                            editor_data.commit();

                            JSONObject Rider_reg_info_Jobj=new JSONObject();




                            Rider_reg_info_Jobj.put("access_token",response.getString("token"));
                            Rider_reg_info_Jobj.put("fcm",PreferencesUtils.getFCMPushKey(SplashActivityQN.this));


                            Log.d("Qnlogin Jason",Rider_reg_info_Jobj.toString());



                            Send_FCMToken_Api(Rider_reg_info_Jobj);
                        }else{
                            Toast.makeText(SplashActivityQN.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        }



                        //startActivity(new Intent(LoginActivity.this,MainDashboard_Activity_QnUnGrid.class));
                        //  startActivity(new Intent(Login_Test_Activity.this, Qam_Dashboard_Activity.class));
                    }else{
                        Toast.makeText(SplashActivityQN.this, "something went wrong", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
               *//* dialog.dismiss();
                dialog.cancel();*//*

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
              *//*  dialog.dismiss();
                dialog.cancel();*//*
                Toast.makeText(SplashActivityQN.this, "something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(SplashActivityQN.this, "something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(SplashActivityQN.this, "something went wrong", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void Send_FCMToken_Api(JSONObject jsonObject) throws UnsupportedEncodingException
    {

        RequestParams params = new RequestParams();
        params.put("",jsonObject);
        // StringEntity entity = new StringEntity(params.toString());
        final ByteArrayEntity entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        //  params.put("Password",jsonpassword);
        // final String url= ConfigQn.Main_URL+"verify_qam_user";
        final String url= ConfigQn.Temp_Main_URL+"fcm-token";
        //final String url= "http://192.168.2.109/api/fcm-token";
        Log.d("url",url);
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MyCustomSSLFactory socketFactory = new MyCustomSSLFactory(trustStore);
            socketFactory.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(socketFactory);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        client.setTimeout(20*1000);

        client.post(SplashActivityQN.this,url,entity,"application/json",new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("res",response.toString());

                try{

                    if(response.length()>0){

                        if(response.getInt("responseCode")==200){
                            SharedPreferences.Editor editor_data= sp_userdetail.edit();
                           // editor_data.putString("token",response.getString("token"));
                            editor_data.putString("user_name",Str_UserName);
                            editor_data.putString("password",Str_Password);
                          *//*  editor_data.putString("rudder_id",Str_Rudder_Id);
                            editor_data.putString("cloud_id",Str_cloud_id);
                            editor_data.putString("Str_StunQn_Ip",Str_StunQn_Ip);
                            editor_data.putString("Str_StunQn_Port",Str_StunQn_Port);
                            editor_data.putString("two_fa_token",Str_two_fa_token);
                            editor_data.putString("twofa_status",Str_twofa_status);
                            editor_data.putString("Str_debugmode",Str_debugmode);
                            editor_data.putString("Str_Stun_Server_list_data",Str_Stun_Server_list_data);*//*

                            editor_data.commit();
                        }



                        //startActivity(new Intent(LoginActivity.this,MainDashboard_Activity_QnUnGrid.class));
                        startActivity(new Intent(SplashActivityQN.this, Qam_Dashboard_Activity.class));
                    }else{
                        Toast.makeText(SplashActivityQN.this, "something went wrong", Toast.LENGTH_LONG).show();
                    }
                   *//* if(response.length()>0)
                    {

                        dialog.dismiss();
                        dialog.cancel();
                        if(response.getInt("responseCode")==200)
                        {

                            //Toast.makeText(LoginActivity.this, response.getString("responseMessage"), Toast.LENGTH_LONG).show();

                            JSONArray datarray=response.getJSONArray("data");
                            JSONObject stun_obj=response.getJSONObject("stun1");
                            Str_two_fa_token=response.getString("two_fa_token");
                            Str_twofa_status=response.getString("twofa_status");
                            Str_debugmode=response.getString("debugmode");

                            Str_StunQn_Ip=stun_obj.getString("ip");
                            Str_StunQn_Port=stun_obj.getString("port");

                            Log.d("Str_StunQn_Ip",""+Str_StunQn_Ip);
                            Log.d("Str_StunQn_Port",""+Str_StunQn_Port);


                            JSONArray stun_array=response.getJSONArray("stun");


                            JSONArray stun_server_data = new JSONArray();
                            for(int i=0;i<stun_array.length();i++)
                            {


                                JSONObject obj_s=stun_array.getJSONObject(i);
                                JSONObject stunsr_obj=new JSONObject();
                                stunsr_obj.put("ip",obj_s.getString("ip"));
                                stunsr_obj.put("port",obj_s.getString("port"));

                                stun_server_data.put(stunsr_obj);
                            }

                            Log.d("stun_server_data",stun_server_data.toString());
                            Str_Stun_Server_list_data=stun_server_data.toString();
                            Log.d("str_stun_server",Str_Stun_Server_list_data);


                            if(Str_twofa_status.equals("1"))
                            {


                                if(datarray.length()>0)
                                {
                                    JSONObject jsonObject=datarray.getJSONObject(0);

                                    Str_Userid=jsonObject.getString("user_id");
                                    Str_cloud_id=jsonObject.getString("cloud_id");

                                    if(Str_Userid.length()>0)
                                    {

                                        Intent intent=new Intent(Login_Test_Activity.this,OTP_TwoFA_Login_ActivityActivity.class);
                                        intent.putExtra("user_id",Str_Userid);
                                        intent.putExtra("user_name",Str_UserName);
                                        intent.putExtra("password",Str_Password);
                                        intent.putExtra("rudder_id",Str_Rudder_Id);
                                        intent.putExtra("cloud_id",Str_cloud_id);
                                        intent.putExtra("Str_StunQn_Ip",Str_StunQn_Ip);
                                        intent.putExtra("Str_StunQn_Port",Str_StunQn_Port);
                                        intent.putExtra("two_fa_token",Str_two_fa_token);
                                        intent.putExtra("twofa_status",Str_twofa_status);
                                        intent.putExtra("Str_debugmode",Str_debugmode);
                                        intent.putExtra("Str_Stun_Server_list_data",Str_Stun_Server_list_data);

                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        Toast.makeText(Login_Test_Activity.this,"Userid not found",Toast.LENGTH_SHORT).show();
                                    }


                                }


                            }
                            else
                            {
                                if(datarray.length()>0)
                                {
                                    JSONObject jsonObject=datarray.getJSONObject(0);

                                    Str_Userid=jsonObject.getString("user_id");
                                    Str_cloud_id=jsonObject.getString("cloud_id");

                                    if(Str_Userid.length()>0)
                                    {


                                        if(sp_userdetail.contains("privateKey") || sp_userdetail.contains("publickey") || sp_userdetail.contains("Str_Stun_main_local_port") || sp_userdetail.contains("Str_NATType") || sp_userdetail.contains("Str_local_com_port"))
                                        {

                                            SharedPreferences.Editor editor_data= sp_userdetail.edit();
                                            editor_data.putString("user_id",Str_Userid);
                                            editor_data.putString("user_name",Str_UserName);
                                            editor_data.putString("password",Str_Password);
                                            editor_data.putString("rudder_id",Str_Rudder_Id);
                                            editor_data.putString("cloud_id",Str_cloud_id);
                                            editor_data.putString("Str_StunQn_Ip",Str_StunQn_Ip);
                                            editor_data.putString("Str_StunQn_Port",Str_StunQn_Port);
                                            editor_data.putString("two_fa_token",Str_two_fa_token);
                                            editor_data.putString("twofa_status",Str_twofa_status);
                                            editor_data.putString("Str_debugmode",Str_debugmode);
                                            editor_data.putString("Str_Stun_Server_list_data",Str_Stun_Server_list_data);

                                            editor_data.commit();


                                            //startActivity(new Intent(LoginActivity.this,MainDashboard_Activity_QnUnGrid.class));
                                            startActivity(new Intent(Login_Test_Activity.this, Qam_Dashboard_Activity.class));


                                        }
                                        else
                                        {
                                            String privateKey;
                                            String publickey;
                                            String ListenPort;
                                            KeyPair keyPair=new KeyPair();
                                            privateKey=keyPair.getPrivateKey().toBase64();
                                            publickey=keyPair.getPublicKey().toBase64();





                                            SharedPreferences.Editor editor_data= sp_userdetail.edit();
                                            editor_data.putString("user_id",Str_Userid);
                                            editor_data.putString("user_name",Str_UserName);
                                            editor_data.putString("password",Str_Password);
                                            editor_data.putString("rudder_id",Str_Rudder_Id);
                                            editor_data.putString("cloud_id",Str_cloud_id);
                                            editor_data.putString("privateKey",privateKey);
                                            editor_data.putString("publickey",publickey);
                                            editor_data.putString("Str_StunQn_Ip",Str_StunQn_Ip);
                                            editor_data.putString("Str_StunQn_Port",Str_StunQn_Port);
                                            editor_data.putString("two_fa_token",Str_two_fa_token);
                                            editor_data.putString("twofa_status",Str_twofa_status);
                                            editor_data.putString("Str_debugmode",Str_debugmode);
                                            editor_data.putString("Str_Stun_Server_list_data",Str_Stun_Server_list_data);
                                            editor_data.commit();

                                            //startActivity(new Intent(LoginActivity.this,MainDashboard_Activity_QnUnGrid.class));
                                            startActivity(new Intent(Login_Test_Activity.this,Qam_Set_Certificate_Activity.class));

                                        }

                                    }


                                }

                            }






                        }  else
                        {
                            Toast.makeText(Login_Test_Activity.this, response.getString("responseMessage"), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(SplashActivityQN.this, "something went wrong", Toast.LENGTH_LONG).show();
                    }*//*


                }catch (Exception e){
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
              *//*  dialog.dismiss();
                dialog.cancel();*//*

                Toast.makeText(SplashActivityQN.this, "something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(SplashActivityQN.this, "something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Toast.makeText(SplashActivityQN.this, "something went wrong", Toast.LENGTH_LONG).show();
            }

        });

    }*/




    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}