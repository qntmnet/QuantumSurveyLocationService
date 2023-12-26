package com.qntm.qntm_survey;

import android.graphics.PointF;

public class PointCordinate {
    public  PointF pointF;
    public  int dbm;
    public int position;
    public String pointype;
    public PointCordinate(PointF pointF, int dbm,int position,String pointype) {
        this.pointF = pointF;
        this.dbm = dbm;
        this.position =position;
        this.pointype = pointype;
    }


}
