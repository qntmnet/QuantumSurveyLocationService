package com.qntm.qntm_survey.Bean;

import java.io.Serializable;
import java.util.ArrayList;

public class SurveyBean implements Serializable {

    String Survey_Id,Survey_Name,Survey_Remarks;
    ArrayList<AddLocationBean> locationBeanArrayList;

    public String getSurvey_Id() {
        return Survey_Id;
    }

    public void setSurvey_Id(String survey_Id) {
        Survey_Id = survey_Id;
    }

    public String getSurvey_Name() {
        return Survey_Name;
    }

    public void setSurvey_Name(String survey_Name) {
        Survey_Name = survey_Name;
    }

    public String getSurvey_Remarks() {
        return Survey_Remarks;
    }

    public void setSurvey_Remarks(String survey_Remarks) {
        Survey_Remarks = survey_Remarks;
    }

    public ArrayList<AddLocationBean> getLocationBeanArrayList() {
        return locationBeanArrayList;
    }

    public void setLocationBeanArrayList(ArrayList<AddLocationBean> locationBeanArrayList) {
        this.locationBeanArrayList = locationBeanArrayList;
    }
}
