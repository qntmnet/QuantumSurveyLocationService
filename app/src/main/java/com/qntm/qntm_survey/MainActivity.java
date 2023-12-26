package com.qntm.qntm_survey;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.qntm.qntm_survey.view.PinView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView txt_tool_network_name,txt_tool_bssid_name,txt_tool_strength_name,txt_tool_Channel,txt_tool_frequency;
    boolean mStopHandler = false;
    Handler handler = new Handler();
    WifiManager wifiManager;
    Runnable runnable;
    int dbm;
    private List<PointCordinate> pointFList = new ArrayList<>();
    public LinearLayout ll_undo;
    PinView imageView;
    int finalHeight, finalWidth;
    LinearLayout setting_layout;
    ImageView img_ungridlogo_actionbar_back;
    int position=-1,dbm_index,dbm_value;
    SharedPreferences sp_userdetail;
    MediaPlayer mPlayer;
    boolean IsPlayed_Once=false;
    TextView txt_error_notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp_userdetail= getSharedPreferences("userdetail.txt", Context.MODE_PRIVATE);
        dbm_index = sp_userdetail.getInt("dbm_index",2);
        dbm_value = sp_userdetail.getInt("dbm_value",-65);
        mPlayer = MediaPlayer.create(this, R.raw.beep);
        txt_tool_network_name = findViewById(R.id.txt_tool_network_name);
        txt_tool_bssid_name = findViewById(R.id.txt_tool_bssid_name);
        txt_tool_strength_name = findViewById(R.id.txt_tool_strength_name);
        txt_tool_Channel = findViewById(R.id.txt_tool_Channel);
        txt_tool_frequency = findViewById(R.id.txt_tool_frequency);
        txt_error_notes = findViewById(R.id.txt_error_notes);

        ll_undo = findViewById(R.id.ll_undo);
        setting_layout = findViewById(R.id.setting_layout);
        img_ungridlogo_actionbar_back = findViewById(R.id.img_ungridlogo_actionbar_back);

        Log.d("dbm_value", String.valueOf(dbm_value));

        img_ungridlogo_actionbar_back.setOnClickListener(v -> {

            Log.d("testok","done");

        });
        pointFList.clear();
        pointFList.add(new PointCordinate(new PointF(351, 325),-65,0,"pin"));
        pointFList.add(new PointCordinate(new PointF(345, 151),-38,1,"pin"));
        pointFList.add(new PointCordinate(new PointF(120, 136),-71,2,"pin"));

        if(pointFList.size()>0){
            position = pointFList.size()-1;
        }else{
            position=-1;
        }
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.d("linkspeed", "" + wifiInfo.getLinkSpeed()+" "+WifiInfo.LINK_SPEED_UNITS);
        Log.d("linkspeed", "" + wifiInfo.getFrequency());

         imageView = findViewById(R.id.image);
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                   // Toast.makeText(getApplicationContext(), "Single tap: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();

                    float x = ((int)sCoord.x);
                    float y = ((int)sCoord.y);
                    Log.d("bitmaptouch","Single tap: " + x + ", " + y);
                    getIndex(x,y);
                  /*  if (x >= 351 && x < 351 + 51.20 && y >= (325-30) && y < (325-30) + 51.20) {
                        //Bitmap touched
                        Log.d("bitmaptouch","done");
                    }*/
                    //Log.d("bitmaptouch", String.valueOf(imageView.Contain((int) x, (int) y)));


                } else {
                   // Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    position=position+1;
                    Log.d("position", String.valueOf(position));
                    imageView.setPin(new PointF(sCoord.x, sCoord.y),dbm,position,"pin");
                    Log.d("PointF","Long press: " + ((int)sCoord.x) + ", " + ((int)sCoord.y)+", "+dbm);
                   // Toast.makeText(getApplicationContext(), "Long press: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
                } else {
                  //  Toast.makeText(getApplicationContext(), "Long press: Image not ready", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (imageView.isReady()) {
                   // PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    position=position+1;
                    Log.d("position", String.valueOf(position));
                    imageView.setPin(new PointF(sCoord.x, sCoord.y),dbm,position,"device");
                   // Toast.makeText(getApplicationContext(), "Double tap: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(getApplicationContext(), "Double tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        imageView.setImage(ImageSource.resource(R.drawable.sampleplan));
        //imageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);
        //imageView.setScaleX(1f);
       /* Glide.with(this)
                .asBitmap()
                .load("https://online.visual-paradigm.com/repository/images/e5728e49-09ce-4c95-b83c-482deee24386/floor-plan-design/sample-floorplan.png")
                .error(R.drawable.green)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImage(ImageSource.bitmap(resource));
                        imageView.buildDrawingCache();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });*/

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //imageView.onTouchEvent(motionEvent);
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
        ViewTreeObserver vto = imageView.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                finalHeight = imageView.getMeasuredHeight();
                finalWidth = imageView.getMeasuredWidth();
                //tv.setText("Height: " + finalHeight + " Width: " + finalWidth);
                Log.d("height", String.valueOf(finalHeight));
                Log.d("height", String.valueOf(finalWidth));
                return true;
            }
        });
        ll_undo.setOnClickListener(v -> {
            if(imageView.getArrayList().size()>0){
                if(imageView.getArrayList().size()==1){
                    Log.d("testok","done");
                    ll_undo.setEnabled(false);
                    ll_undo.setClickable(false);
                }
                imageView.RemoveLastIndex();
            }

        });

         runnable = new Runnable() {
            @Override
            public void run() {
                // do your stuff - don't create a new runnable here!
                if (!mStopHandler) {
                    // WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (wifiManager.isWifiEnabled()) {
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

                        if (wifiInfo != null) {
                             dbm = wifiInfo.getRssi();
                            Log.d("dbm", "" + dbm);
                            Log.d("IsPlayed_Once", "" + IsPlayed_Once);

                            txt_tool_strength_name.setText("" + dbm);

                            txt_tool_network_name.setText(wifiInfo.getSSID());
                            txt_tool_bssid_name.setText(wifiInfo.getBSSID());
                            String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
                           // txt_tool_ip_name.setText(ip);


                            int freq = wifiInfo.getFrequency();
                            String networkChannel = String.valueOf(convertFrequencyToChannel(freq));

                            String  info_frequency = freq + "MHz";
                            String info_network_channel = networkChannel;
                            txt_tool_Channel.setText(info_network_channel);
                            txt_tool_frequency.setText(info_frequency);

                            if (dbm >= -40) {
                                Log.d("check","check");
                                txt_tool_strength_name.setTextColor(getResources().getColor(R.color.wifigreen));
                                txt_tool_strength_name.setText(dbm +" dbm");
                                if(dbm_value >= dbm){
                                    if(!IsPlayed_Once){
                                        mPlayer.start();
                                        IsPlayed_Once=true;
                                    }
                                    txt_error_notes.setVisibility(View.VISIBLE);
                                }else{
                                    txt_error_notes.setVisibility(View.GONE);
                                }

                                // img_singnal.setImageResource(R.drawable.ic_signal_wifi_4_bar_black_48dp);
                                //  img_singnal.setColorFilter(ContextCompat.getColor(ToolsActivity.this, R.color.wifigreen), android.graphics.PorterDuff.Mode.SRC_IN);
                            } else if (dbm < -40 && dbm >= -60) {
                                txt_tool_strength_name.setTextColor(getResources().getColor(R.color.wifigreen));
                                txt_tool_strength_name.setText(dbm +" dbm");
                                if(dbm_value >= dbm){
                                    if(!IsPlayed_Once){
                                        mPlayer.start();
                                        IsPlayed_Once=true;
                                    }
                                    txt_error_notes.setVisibility(View.VISIBLE);
                                }else{
                                    txt_error_notes.setVisibility(View.GONE);
                                }
                                //  img_singnal.setImageResource(R.drawable.ic_signal_wifi_3_bar_black_48dp);
                                // img_singnal.setColorFilter(ContextCompat.getColor(ToolsActivity.this, R.color.wifigreen), android.graphics.PorterDuff.Mode.SRC_IN);
                            } else if (dbm < -60 && dbm >= -70) {
                                txt_tool_strength_name.setTextColor(getResources().getColor(R.color.wifiyellow));
                                txt_tool_strength_name.setText(dbm +" dbm");
                                if(dbm_value >= dbm){
                                    if(!IsPlayed_Once){
                                        mPlayer.start();
                                        IsPlayed_Once=true;
                                    }
                                    txt_error_notes.setVisibility(View.VISIBLE);
                                }else{
                                    txt_error_notes.setVisibility(View.GONE);
                                }
                                //  img_singnal.setImageResource(R.drawable.ic_signal_wifi_2_bar_black_48dp);
                                //  img_singnal.setColorFilter(ContextCompat.getColor(ToolsActivity.this, R.color.wifiyellow), android.graphics.PorterDuff.Mode.SRC_IN);
                            } else if (dbm < -70 && dbm >= -80) {
                                txt_tool_strength_name.setTextColor(getResources().getColor(R.color.wifiyellow));
                                txt_tool_strength_name.setText(dbm +" dbm");
                                if(dbm_value >= dbm){
                                    if(!IsPlayed_Once){
                                        mPlayer.start();
                                        IsPlayed_Once=true;
                                    }
                                    txt_error_notes.setVisibility(View.VISIBLE);
                                }else{
                                    txt_error_notes.setVisibility(View.GONE);
                                }
                                // img_singnal.setImageResource(R.drawable.ic_signal_wifi_1_bar_black_48dp);
                                // img_singnal.setColorFilter(ContextCompat.getColor(ToolsActivity.this, R.color.wifiyellow), android.graphics.PorterDuff.Mode.SRC_IN);
                            } else {
                                txt_tool_strength_name.setTextColor(getResources().getColor(R.color.wifired));
                                txt_tool_strength_name.setText("" + dbm);
                                //  img_singnal.setImageResource(R.drawable.ic_signal_wifi_0_bar_black_48dp);
                                // img_singnal.setColorFilter(ContextCompat.getColor(ToolsActivity.this, R.color.wifired), android.graphics.PorterDuff.Mode.SRC_IN);
                            }
                        }
                    }
                    handler.postDelayed(this, 2000);
                }
            }
        };

// start it with:
        handler.post(runnable);


        for(int i=0;i<pointFList.size();i++){

            imageView.setPin(new PointF(pointFList.get(i).pointF.x, pointFList.get(i).pointF.y), pointFList.get(i).dbm,i,pointFList.get(i).pointype);
        }

        setting_layout.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SettingActivity.class));
        });
    }

   /* private boolean isWithinViewBounds(int xPoint, int yPoint) {
        int[] l = new int[2];
        imageView.getLocationOnScreen(l);
        int x = l[0];
        int y = l[1];
        int w = imageView.getMeasuredWidth();
        int h = imageView.getMeasuredHeight();;
        return !(xPoint < x || xPoint > x + w || yPoint < y || yPoint > y + h);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isWithinViewBounds((int) ev.getRawX(), (int) ev.getRawY())) {
            Log.d("outside","Click");
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }*/


    private  void getIndex(float x, float y){
//100,327
        Log.d("size_getIndex", String.valueOf(imageView.getArrayList().size()));
        if(imageView.getArrayList().size()>0){
//490,196,51.20,541.20
            //498,183,184,290
            //205.0, 272.0,188,286
            for(int i=0;i<imageView.getArrayList().size();i++){
                PointCordinate cordinate = imageView.getArrayList().get(i);

                if(cordinate.pointype.equals("pin")){
                    Log.d("Data","index "+i+"--> x="+x+",y="+y+",pointX="+cordinate.pointF.x+",pointY="+cordinate.pointF.y+",PinWidth="+imageView.getPinWidth()+",PinHeight="+imageView.getPinHeight());
                    Log.d("condition1", String.valueOf(x >= cordinate.pointF.x));
                    Log.d("condition2", String.valueOf(x < cordinate.pointF.x + imageView.getPinWidth()));
                    Log.d("condition3", String.valueOf(y >= cordinate.pointF.y-25));
                    Log.d("condition4", String.valueOf((y) < (cordinate.pointF.y + imageView.getPinHeight())));
                    if (x >= cordinate.pointF.x && x < cordinate.pointF.x + imageView.getPinWidth() && y >= (cordinate.pointF.y-25) && y < (cordinate.pointF.y + imageView.getPinHeight())) {
                        //Bitmap touched
                        Log.d("bitmaptouch", String.valueOf(cordinate.position));
                    }
                }else{
                    Log.d("Data","index "+i+"--> x="+x+",y="+y+",pointX="+cordinate.pointF.x+",pointY="+cordinate.pointF.y+",PinWidth="+imageView.getDevicePinWidth()+",PinHeight="+imageView.getDeviceHeight());
                    Log.d("condition1", String.valueOf(x >= cordinate.pointF.x));
                    Log.d("condition2", String.valueOf(x < cordinate.pointF.x + imageView.getDevicePinWidth()));
                    Log.d("condition3", String.valueOf(y >= cordinate.pointF.y-40));
                    Log.d("condition4", String.valueOf((y) < (cordinate.pointF.y + imageView.getDeviceHeight())));
                    if (x >= cordinate.pointF.x && x < cordinate.pointF.x + imageView.getDevicePinWidth() && y >= (cordinate.pointF.y-50) && y < (cordinate.pointF.y + imageView.getDeviceHeight())) {
                        //Bitmap touched
                        Log.d("bitmaptouch_device", String.valueOf(cordinate.position));
                    }
                }
            }
        }
       /* if (x >= 351 && x < 351 + 51.20 && y >= (325-30) && y < (325-30) + 51.20) {
            //Bitmap touched
            Log.d("bitmaptouch","done");
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(Wifichangereceiver);
        mStopHandler=true;


     /*   try{
            File file = new File(Environment.getExternalStorageDirectory()+"/vcards.vcf");
            List<VCard> vcards = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vcards = Ezvcard.parse(file.toPath()).all();
            }
            for (VCard vcard : vcards){
                System.out.println("Name: " + vcard.getFormattedName().getValue());
                System.out.println("Telephone numbers:");
                for (Telephone tel : vcard.getTelephoneNumbers()){
                    System.out.println(tel.getTypes() + ": " + tel.getText());
                }
            }
        }catch(Exception e){e.printStackTrace();}*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        dbm_value = sp_userdetail.getInt("dbm_value",-65);
        Log.d("dbm_value", String.valueOf(dbm_value));
        IsPlayed_Once = sp_userdetail.getBoolean("IsPlayed_Once",true);
        mStopHandler=false;
        handler.post(runnable);
        registerReceiver(Wifichangereceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        if(isLocationEnabled(MainActivity.this))
        {
            if (wifiManager.isWifiEnabled()) {
             //   ll_devices.setVisibility(View.VISIBLE);
               // ll_devices_title.setVisibility(View.VISIBLE);
                //  ll_netinfo.setVisibility(View.VISIBLE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) {
                    txt_tool_network_name.setText(wifiInfo.getSSID());
                    txt_tool_bssid_name.setText(wifiInfo.getBSSID());
                    String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
                   // txt_tool_ip_name.setText(ip);
                    int freq = wifiInfo.getFrequency();
                    String networkChannel = String.valueOf(convertFrequencyToChannel(freq));

                    String  info_frequency = freq + "MHz";
                    String info_network_channel = networkChannel;
                    txt_tool_Channel.setText(info_network_channel);
                    txt_tool_frequency.setText(info_frequency);
                }
            } else {
                Log.i("Wi-Fi network state", "off");
               /* ll_devices.setVisibility(View.GONE);
                ll_devices_title.setVisibility(View.GONE);
                txt_error_notes.setVisibility(View.VISIBLE);*/
                //   ll_netinfo.setVisibility(View.INVISIBLE);
            }
        }
        else {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(this);
            builder.setTitle("Enable Loaction Service");
            builder.setMessage("Enable Location To Find Available Wi-Fi");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
                }
            });
            builder.setNegativeButton("CLOSE",(dialog, which) -> {
                /*ll_devices.setVisibility(View.GONE);
                ll_devices_title.setVisibility(View.GONE);
                txt_error_notes.setVisibility(View.VISIBLE);*/
                //  ll_netinfo.setVisibility(View.INVISIBLE);
            });
            builder.setCancelable(false);
            builder.show();

        }
    }
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }
    private int convertFrequencyToChannel(int freq) {
        if (freq == 2484) { // 2.4GHz Channel 14
            return 14;
        } else if (freq < 2484) { // 2.4GHz (802.11b/g/n/ax)
            return (freq - 2407) / 5;
        } else if (freq >= 4910 && freq <= 4980) { // 4.9GHz (802.11j)
            return (freq - 4000) / 5;
        } else if (freq < 5925) { // 5GHz (802.11a/h/j/n/ac/ax) - 5.9 GHz (802.11p)
            return (freq - 5000) / 5;
        } else if (freq == 5935) {
            return 2;
        } else if (freq <= 45000) { // 6 GHz+ (802.11ax and 802.11be)
            return (freq - 5950) / 5;
        } else if (freq >= 58320 && freq <= 70200) { // 60GHz (802.11ad/ay)
            return (freq - 56160) / 2160;
        } else {
            return -1;
        }
    }
    public BroadcastReceiver Wifichangereceiver=new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN);
            Log.d("extraWifiState",""+extraWifiState);

            switch (extraWifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    // txt_message.setVisibility(View.VISIBLE);
                    // ll_netinfo.setVisibility(View.GONE);
                  //  txt_error_notes.setVisibility(View.VISIBLE);
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    //txt_message.setVisibility(View.VISIBLE);
                    // ll_netinfo.setVisibility(View.GONE);

                   // txt_error_notes.setVisibility(View.VISIBLE);
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    //Toast.makeText(context,"Wifi enabled",Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                            Log.d("wifiInfo", wifiInfo.toString());
                            Log.d("SSID", wifiInfo.getSSID());

                            if (wifiInfo.getSSID() != null) {
                                String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
                              //  txt_tool_ip_name.setText(ip);
                                txt_tool_network_name.setText(wifiInfo.getSSID());
                                txt_tool_bssid_name.setText(wifiInfo.getBSSID());
                                int freq = wifiInfo.getFrequency();
                                String networkChannel = String.valueOf(convertFrequencyToChannel(freq));

                                String  info_frequency = freq + "MHz";
                                String info_network_channel = networkChannel;
                                txt_tool_Channel.setText(info_network_channel);
                                txt_tool_frequency.setText(info_frequency);
                              //  txt_error_notes.setVisibility(View.GONE);
                                //   ll_netinfo.setVisibility(View.VISIBLE);
                             /*   if(!isLocationEnabled(Qam_Dashboard_Activity.this))
                                {
                                    txt_error_notes.setVisibility(View.VISIBLE);
                                    // ll_netinfo.setVisibility(View.GONE);
                                }*/

                            }
                        }
                    }, 1200);

                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    //Toast.makeText(context,"Wifi enabled", Toast.LENGTH_LONG).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                            Log.d("wifiInfo", wifiInfo.toString());
                            Log.d("SSID", wifiInfo.getSSID());

                            if (wifiInfo.getSSID() != null) {
                                String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
                              //  txt_tool_ip_name.setText(ip);
                                txt_tool_network_name.setText(wifiInfo.getSSID());
                                txt_tool_bssid_name.setText(wifiInfo.getBSSID());
                                int freq = wifiInfo.getFrequency();
                                String networkChannel = String.valueOf(convertFrequencyToChannel(freq));

                                String  info_frequency = freq + "MHz";
                                String info_network_channel = networkChannel;
                                txt_tool_Channel.setText(info_network_channel);
                                txt_tool_frequency.setText(info_frequency);
                                //  txt_message.setVisibility(View.GONE);
                                //  ll_netinfo.setVisibility(View.VISIBLE);
                               /* if(!isLocationEnabled(Qam_Dashboard_Activity.this))
                                {
                                    txt_error_notes.setVisibility(View.VISIBLE);
                                    //  ll_netinfo.setVisibility(View.GONE);
                                }*/
                            }
                        }
                    }, 1200);

                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    //  WifiState.setText("WIFI STATE UNKNOWN");
                    break;
            }
        }
    };
}