package com.qntm.qntm_survey;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.qntm.qntm_survey.Adapter.LocationList_Adapter;
import com.qntm.qntm_survey.Bean.AddLocationBean;

import java.util.ArrayList;

public class Survey_Locationlist_Activity extends AppCompatActivity {

    ArrayList<AddLocationBean> locationBeanArrayList = new ArrayList<>();
    RecyclerView recyclerview;
    LocationList_Adapter adapter;
    ImageView img_ungridlogo_actionbar_back;
    ExtendedFloatingActionButton fab_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_locationlist);
        img_ungridlogo_actionbar_back =  findViewById(R.id.img_ungridlogo_actionbar_back);
        fab_add =  findViewById(R.id.fab_add);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        img_ungridlogo_actionbar_back.setOnClickListener(v -> onBackPressed());

        locationBeanArrayList = (ArrayList<AddLocationBean>) getIntent().getSerializableExtra("locationdata");
       // Log.d("test", String.valueOf(locationBeanArrayList.get(0).PlanImage));
        if(locationBeanArrayList.size()>0){
            adapter = new LocationList_Adapter(this,locationBeanArrayList);
            recyclerview.setAdapter(adapter);
            adapter.setClickListener((view, position) -> {
                if(view.getId() == R.id.ll_root){
                    Intent intent = new Intent(Survey_Locationlist_Activity.this, MainActivity.class);

                    startActivity(intent);
                }
            });
        }

        fab_add.setOnClickListener(view -> {
            Intent i=new Intent(Survey_Locationlist_Activity.this,Add_Plan_Activity.class);

            startActivity(i);

        });
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // if the recycler view is scrolled
                // above shrink the FAB
                if (dy > 10 && fab_add.isExtended()) {
                    fab_add.shrink();
                }

                // if the recycler view is scrolled
                // above extend the FAB
                if (dy < -10 && !fab_add.isExtended()) {
                    fab_add.extend();
                }

                // of the recycler view is at the first
                // item always extend the FAB
                if (!recyclerView.canScrollVertically(-1)) {
                    fab_add.extend();
                }
            }
        });

    }
}