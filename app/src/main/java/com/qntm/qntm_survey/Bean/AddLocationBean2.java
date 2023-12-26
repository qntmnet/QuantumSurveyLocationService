package com.qntm.qntm_survey.Bean;

import android.net.Uri;

import java.io.Serializable;

public class AddLocationBean2  implements Serializable {
    public String LocationName;
    public transient  Uri PlanImage;

    public AddLocationBean2(String locationName, Uri planImage) {
        LocationName = locationName;
        PlanImage = planImage;
    }
}
