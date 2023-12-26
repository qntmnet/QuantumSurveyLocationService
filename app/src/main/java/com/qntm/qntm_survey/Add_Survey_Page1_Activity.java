package com.qntm.qntm_survey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;



public class Add_Survey_Page1_Activity extends AppCompatActivity {

    LinearLayout ll_next,ll_back;
    ImageView img_ungridlogo_actionbar_back,img_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_survey_page1);
        img_ungridlogo_actionbar_back = findViewById(R.id.img_ungridlogo_actionbar_back);
        img_add = findViewById(R.id.img_add);
        ll_next = findViewById(R.id.ll_next);
        ll_back = findViewById(R.id.ll_back);

        img_add.setVisibility(View.INVISIBLE);
        img_ungridlogo_actionbar_back.setOnClickListener(v -> onBackPressed());
        ll_back.setOnClickListener(v -> onBackPressed());
        ll_next.setOnClickListener(v -> {

            Intent intent =new Intent(Add_Survey_Page1_Activity.this,Add_Survey_Page2_Activity.class);

            startActivity(intent);
        });

    }
}