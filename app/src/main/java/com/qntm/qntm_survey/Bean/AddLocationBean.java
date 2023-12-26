package com.qntm.qntm_survey.Bean;

import java.io.Serializable;

public class AddLocationBean implements Serializable {

    public String LocationName;
    int PlanImage;

    public AddLocationBean(String locationName, int planImage) {
        LocationName = locationName;
        PlanImage = planImage;
    }


}
