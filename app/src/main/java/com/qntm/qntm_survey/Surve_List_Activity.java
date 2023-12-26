package com.qntm.qntm_survey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qntm.qntm_survey.Adapter.Surveylist_Adapter;
import com.qntm.qntm_survey.Bean.AddLocationBean;
import com.qntm.qntm_survey.Bean.SurveyBean;

import java.util.ArrayList;
import java.util.List;

public class Surve_List_Activity extends AppCompatActivity {

    FloatingActionButton fab_add;
    List<SurveyBean> surveyList = new ArrayList<>();
    ArrayList<AddLocationBean> locationBeanArrayList = new ArrayList<>();
    ArrayList<AddLocationBean> locationBeanArrayList2 = new ArrayList<>();
    RecyclerView recyclerview;
    Surveylist_Adapter adapter;
    Uri sampleplan;
    ImageView img_ungridlogo_actionbar_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surve_list);
        fab_add=findViewById(R.id.fab_add);
        img_ungridlogo_actionbar_back=findViewById(R.id.img_ungridlogo_actionbar_back);
        recyclerview = findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

       // sampleplan = Uri.parse("content://media/external_primary/images/media/1000003377");
        surveyList.clear();
        locationBeanArrayList.clear();
        locationBeanArrayList2.clear();

        SurveyBean bean = new SurveyBean();
        bean.setSurvey_Id("1");
        bean.setSurvey_Name("Shakti 404");
        bean.setSurvey_Remarks("test");
        AddLocationBean locationBean1 = new AddLocationBean("1st Floor", R.drawable.sampleplan);
        AddLocationBean locationBean2 = new AddLocationBean("2nd Floor", R.drawable.sampleplan);
        AddLocationBean locationBean3 = new AddLocationBean("3rd Floor", R.drawable.sampleplan);
        AddLocationBean locationBean4 = new AddLocationBean("4th Floor", R.drawable.sampleplan);


        locationBeanArrayList.add(locationBean1);
        locationBeanArrayList.add(locationBean2);
        locationBeanArrayList.add(locationBean3);
        locationBeanArrayList.add(locationBean4);
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));
        locationBeanArrayList.add(new AddLocationBean("4th Floor", R.drawable.sampleplan));

        bean.setLocationBeanArrayList(locationBeanArrayList);


        SurveyBean bean2 = new SurveyBean();
        bean2.setSurvey_Id("2");
        bean2.setSurvey_Name("The Chamber");
        bean2.setSurvey_Remarks("test");
        AddLocationBean locationBean5 = new AddLocationBean("9th Floor", R.drawable.sampleplan);
        AddLocationBean locationBean6 = new AddLocationBean("6th Floor", R.drawable.sampleplan);
        //AddLocationBean locationBean3 = new AddLocationBean("3rd Floor", R.drawable.sampleplan);
        //AddLocationBean locationBean4 = new AddLocationBean("4rd Floor", R.drawable.sampleplan);


        locationBeanArrayList2.add(locationBean5);
        locationBeanArrayList2.add(locationBean6);
     //   locationBeanArrayList.add(locationBean3);
      //  locationBeanArrayList.add(locationBean4);
        bean2.setLocationBeanArrayList(locationBeanArrayList2);
        surveyList.add(bean);
        surveyList.add(bean2);

       /* Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(String.valueOf(sampleplan)));
            img_ungridlogo_actionbar_back.setImageBitmap(bitmap);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        adapter = new Surveylist_Adapter(this,surveyList);
        recyclerview.setAdapter(adapter);
        adapter.setClickListener((view, position) -> {
            if(view.getId() == R.id.ll_root){
                Intent intent = new Intent(Surve_List_Activity.this, Survey_Locationlist_Activity.class);
                intent.putExtra("locationdata",surveyList.get(position).getLocationBeanArrayList());
                startActivity(intent);
            }
        });



        fab_add.setOnClickListener(view -> {
            startActivity(new Intent(Surve_List_Activity.this, Add_Survey_Page1_Activity.class));
        });
    }
}