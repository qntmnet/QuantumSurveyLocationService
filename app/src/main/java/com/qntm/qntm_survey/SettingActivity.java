package com.qntm.qntm_survey;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    String[] listItems;
    TextView tv_rssi_threshold;
    LinearLayout ll_layout1;
    SharedPreferences sp_userdetail;
    ImageView img_ungridlogo_actionbar_back;
    int index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp_userdetail= getSharedPreferences("userdetail.txt", Context.MODE_PRIVATE);
        tv_rssi_threshold=findViewById(R.id.tv_rssi_threshold);
        img_ungridlogo_actionbar_back=findViewById(R.id.img_ungridlogo_actionbar_back);
        ll_layout1=findViewById(R.id.ll_layout1);
        listItems = getResources().getStringArray(R.array.shopping_item);


        img_ungridlogo_actionbar_back.setOnClickListener(v -> onBackPressed());
        if(sp_userdetail.contains("dbm_index")) {
            index = sp_userdetail.getInt("dbm_index",-1);

        }else{
            String[] str_dbm = listItems[2].split(" ");
            SharedPreferences.Editor editor_data= sp_userdetail.edit();
            editor_data.putInt("dbm_index",2);
            editor_data.putInt("dbm_value",Integer.parseInt(str_dbm[0]));
            index=2;
            editor_data.apply();
        }

            tv_rssi_threshold.setText(listItems[index]);
            ll_layout1.setOnClickListener(v -> {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(SettingActivity.this);
            mBuilder.setTitle("Set Threshold");
            mBuilder.setSingleChoiceItems(listItems, index, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    tv_rssi_threshold.setText(listItems[i]);
                    String[] str_dbm = listItems[i].split(" ");
                    SharedPreferences.Editor editor_data= sp_userdetail.edit();
                    editor_data.putInt("dbm_index",i);
                    editor_data.putInt("dbm_value",Integer.parseInt(str_dbm[0]));
                    editor_data.putBoolean("IsPlayed_Once",false);
                    editor_data.apply();
                    dialogInterface.dismiss();
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        });
    }
}