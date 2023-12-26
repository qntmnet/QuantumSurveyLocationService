package com.qntm.qntm_survey;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;
import com.loopj.android.http.RequestParams;
import com.qntm.qntm_survey.util.MyCustomSSLFactory;
import com.qntm.qntm_survey.util.PreferencesUtils;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.KeyStore;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class Login_Activity extends AppCompatActivity {

    Toolbar toolbar;
    EditText edt_username,edt_password;
    Button btn_login,btn_userid_next;
    LinearLayout ll_userid_layout,ll_password_login_layout,ll_username_layout;
    TextView txt_welcome_user,txt_not_you;
    private static AsyncHttpClient client ;
    connectionDector dector;
    String Str_Userid;
    private boolean backPressedToExitOnce = false;
    String Str_StunQn_Ip;
    String Str_StunQn_Port;
    String Str_two_fa_token;
    String Str_twofa_status;
    String Str_debugmode;
    String Str_UserName;
    String Str_Password;
    String Str_Rudder_Id;
    String Str_cloud_id;
    String Str_Stun_Server_list_data="";


    SharedPreferences sp_userdetail;
    ImageView img_login_actionbar_back;
    String Str_Device_uuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



       /* toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/



        sp_userdetail= getSharedPreferences("userdetail.txt", Context.MODE_PRIVATE);


        client= new AsyncHttpClient();
        dector=new connectionDector(Login_Activity.this);
        edt_username=findViewById(R.id.edt_username);
        edt_password=findViewById(R.id.edt_password);
        btn_login=findViewById(R.id.btn_login);
        btn_userid_next=findViewById(R.id.btn_userid_next);

        ll_userid_layout=findViewById(R.id.ll_userid_layout);
        ll_password_login_layout=findViewById(R.id.ll_password_login_layout);
        ll_username_layout=findViewById(R.id.ll_username_layout);

       // img_login_actionbar_back=findViewById(R.id.img_login_actionbar_back);

        txt_welcome_user=findViewById(R.id.txt_welcome_user);
        txt_not_you=findViewById(R.id.txt_not_you);

        ll_userid_layout.setVisibility(View.VISIBLE);
        ll_password_login_layout.setVisibility(View.GONE);
        ll_username_layout.setVisibility(View.GONE);


        Str_Rudder_Id=getIntent().getStringExtra("Rudder_id");





       // getFCMKey();
        txt_not_you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                txt_welcome_user.setText("");
                edt_password.setText("");
                edt_username.setText("");
                ll_userid_layout.setVisibility(View.VISIBLE);
                ll_password_login_layout.setVisibility(View.GONE);
                ll_username_layout.setVisibility(View.GONE);
            }
        });
        btn_userid_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edt_username.getText().toString().trim().length()>0)
                {
                    txt_welcome_user.setText("WelCome "+edt_username.getText().toString());

                    ll_password_login_layout.setVisibility(View.VISIBLE);
                    ll_username_layout.setVisibility(View.VISIBLE);
                    ll_userid_layout.setVisibility(View.GONE);
                    hideKeybaord(v);

                }
                else
                {
                    Toast.makeText(Login_Activity.this,"Please Enter Userid",Toast.LENGTH_SHORT).show();

                }



            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dector.checkNetwork())
                {

                    if(edt_username.getText().toString().trim().length()>0)
                    {

                        if(edt_password.getText().toString().trim().length()>0)
                        {

                            try {



                                 Str_UserName=edt_username.getText().toString();
                                 Str_Password=edt_password.getText().toString();
                                 try {
                                     Str_Device_uuid = Settings.Secure.getString(Login_Activity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
                                     Log.d("device_uuid",Str_Device_uuid);
                                 }
                                 catch (Exception e)
                                 {
                                     e.printStackTrace();
                                 }


                                //{"password": "12345678", "rudder_id": "zenexim1", "username": "qnmatrix_android1"}


                                JSONObject Rider_reg_info_Jobj=new JSONObject();



                            /*    Rider_reg_info_Jobj.put("username",edt_username.getText().toString());
                                Rider_reg_info_Jobj.put("password",edt_password.getText().toString());
                                Rider_reg_info_Jobj.put("rudder_id",Str_Rudder_Id);
                                Rider_reg_info_Jobj.put("peer_identity",Str_Device_uuid);
                                Rider_reg_info_Jobj.put("is_qim","1");*/
                                Rider_reg_info_Jobj.put("username",edt_username.getText().toString());
                                Rider_reg_info_Jobj.put("password",edt_password.getText().toString());
                              /*  Rider_reg_info_Jobj.put("username","qnmatrix_android1");
                                Rider_reg_info_Jobj.put("password","12345678");
                                Rider_reg_info_Jobj.put("rudder_id","zenexim1");
                                Str_UserName="qnmatrix_android1";
                                Str_Password="12345678";
                                Str_Rudder_Id="zenexim1";*/

                                Log.d("Qnlogin Jason",Rider_reg_info_Jobj.toString());



                               // Set_Rider_Register_data(Rider_reg_info_Jobj);

                                startActivity(new Intent(Login_Activity.this, Surve_List_Activity.class));
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }



                        }
                        else
                        {
                            Toast.makeText(Login_Activity.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        Toast.makeText(Login_Activity.this,"Please Enter Userid",Toast.LENGTH_SHORT).show();
                    }







                }
                else
                    {
                    Toast.makeText(Login_Activity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
   /* private void getFCMKey(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(result -> {
            if(result != null){
                PreferencesUtils.setFCMPushKey(Login_Activity.this, result);

                Log.d("fcmtoken", result);
                // DO your thing with your firebase token
            }
        });
    }*/
    private void Set_Rider_Register_data(JSONObject jsonObject) throws UnsupportedEncodingException
    {
        final Dialog dialog;


        dialog = new Dialog(Login_Activity.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        // Set dialog title
        // dialog.setTitle("Custom Dialog");
        dialog.show();

        RequestParams params = new RequestParams();
        params.put("",jsonObject);
        // StringEntity entity = new StringEntity(params.toString());
        final ByteArrayEntity entity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        //  params.put("Password",jsonpassword);
       // final String url= ConfigQn.Main_URL+"verify_qam_user";
        final String url= "http://136.233.160.205:8080/api/login";
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

        client.post(Login_Activity.this,url,entity,"application/json",new JsonHttpResponseHandler()
        {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("res",response.toString());

                try{

                    if(response.length()>0){
                        dialog.dismiss();
                        dialog.cancel();
                        if(response.getInt("responseCode")==200){
                            SharedPreferences.Editor editor_data= sp_userdetail.edit();
                            editor_data.putString("token",response.getString("token"));
                            editor_data.putString("user_name",Str_UserName);
                            editor_data.putString("password",Str_Password);
                          /*  editor_data.putString("rudder_id",Str_Rudder_Id);
                            editor_data.putString("cloud_id",Str_cloud_id);
                            editor_data.putString("Str_StunQn_Ip",Str_StunQn_Ip);
                            editor_data.putString("Str_StunQn_Port",Str_StunQn_Port);
                            editor_data.putString("two_fa_token",Str_two_fa_token);
                            editor_data.putString("twofa_status",Str_twofa_status);
                            editor_data.putString("Str_debugmode",Str_debugmode);
                            editor_data.putString("Str_Stun_Server_list_data",Str_Stun_Server_list_data);*/

                            editor_data.commit();

                            JSONObject Rider_reg_info_Jobj=new JSONObject();




                            Rider_reg_info_Jobj.put("access_token",response.getString("token"));
                            Rider_reg_info_Jobj.put("fcm", PreferencesUtils.getFCMPushKey(Login_Activity.this));


                            Log.d("Qnlogin Jason",Rider_reg_info_Jobj.toString());



                            Send_FCMToken_Api(Rider_reg_info_Jobj);
                        }else{
                            Toast.makeText(Login_Activity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                        }



                        //startActivity(new Intent(LoginActivity.this,MainDashboard_Activity_QnUnGrid.class));
                      //  startActivity(new Intent(Login_Activity.this, Qam_Dashboard_Activity.class));
                    }else{
                        Toast.makeText(Login_Activity.this, "something went wrong", Toast.LENGTH_LONG).show();
                    }
                   /* if(response.length()>0)
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

                                        Intent intent=new Intent(Login_Activity.this,OTP_TwoFA_Login_ActivityActivity.class);
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
                                        Toast.makeText(Login_Activity.this,"Userid not found",Toast.LENGTH_SHORT).show();
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
                                            startActivity(new Intent(Login_Activity.this, Qam_Dashboard_Activity.class));


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
                                            startActivity(new Intent(Login_Activity.this,Qam_Set_Certificate_Activity.class));

                                        }

                                    }


                                }

                            }






                        }  else
                        {
                            Toast.makeText(Login_Activity.this, response.getString("responseMessage"), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(Login_Activity.this, "something went wrong", Toast.LENGTH_LONG).show();
                    }*/


                }catch (Exception e){
                    e.printStackTrace();
                }
                dialog.dismiss();
                dialog.cancel();

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
              /*  dialog.dismiss();
                dialog.cancel();*/
                dialog.dismiss();
                dialog.cancel();
                Toast.makeText(Login_Activity.this, "something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                dialog.dismiss();
                dialog.cancel();
                Toast.makeText(Login_Activity.this, "something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                dialog.dismiss();
                dialog.cancel();
                Toast.makeText(Login_Activity.this, "something went wrong", Toast.LENGTH_LONG).show();
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

        client.post(Login_Activity.this,url,entity,"application/json",new JsonHttpResponseHandler()
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
                          /*  editor_data.putString("rudder_id",Str_Rudder_Id);
                            editor_data.putString("cloud_id",Str_cloud_id);
                            editor_data.putString("Str_StunQn_Ip",Str_StunQn_Ip);
                            editor_data.putString("Str_StunQn_Port",Str_StunQn_Port);
                            editor_data.putString("two_fa_token",Str_two_fa_token);
                            editor_data.putString("twofa_status",Str_twofa_status);
                            editor_data.putString("Str_debugmode",Str_debugmode);
                            editor_data.putString("Str_Stun_Server_list_data",Str_Stun_Server_list_data);*/

                            editor_data.commit();
                        }



                        //startActivity(new Intent(LoginActivity.this,MainDashboard_Activity_QnUnGrid.class));
                        startActivity(new Intent(Login_Activity.this, MainActivity.class));
                    }else{
                        Toast.makeText(Login_Activity.this, "something went wrong", Toast.LENGTH_LONG).show();
                    }
                   /* if(response.length()>0)
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

                                        Intent intent=new Intent(Login_Activity.this,OTP_TwoFA_Login_ActivityActivity.class);
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
                                        Toast.makeText(Login_Activity.this,"Userid not found",Toast.LENGTH_SHORT).show();
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
                                            startActivity(new Intent(Login_Activity.this, Qam_Dashboard_Activity.class));


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
                                            startActivity(new Intent(Login_Activity.this,Qam_Set_Certificate_Activity.class));

                                        }

                                    }


                                }

                            }






                        }  else
                        {
                            Toast.makeText(Login_Activity.this, response.getString("responseMessage"), Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(Login_Activity.this, "something went wrong", Toast.LENGTH_LONG).show();
                    }*/


                }catch (Exception e){
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
              /*  dialog.dismiss();
                dialog.cancel();*/

                Toast.makeText(Login_Activity.this, "something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

                Toast.makeText(Login_Activity.this, "something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Toast.makeText(Login_Activity.this, "something went wrong", Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    public void onBackPressed() {
        //mStopHandler=true;
        if (backPressedToExitOnce) {
            //super.onBackPressed();
            moveTaskToBack(false);
            //mStopHandler=true;
              /*  this.finish();
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);*/

            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
        else
        {
            this.backPressedToExitOnce = true;
            Toast.makeText(Login_Activity.this, "Press again to exit", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;


                }
            }, 3000);
        }
        //super.onBackPressed();

    }

}