package com.qntm.qntm_survey.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.qntm.qntm_survey.MainActivity;
import com.qntm.qntm_survey.PointCordinate;
import com.qntm.qntm_survey.R;

import java.util.ArrayList;
import java.util.List;


public class PinView extends SubsamplingScaleImageView {

    private final Paint paint = new Paint();
    private final PointF vPin = new PointF();
    private PointF sPin;
    private Bitmap pin,redpin,greenpin,yellowpin,devicepin;
    double density;
    MainActivity activity;
    Context mcontext;
    int dbm=0;
    private Rect r;
    float pinWidht,pinHeight,devicepinWidht,devicepinHeight;
    String pointype="";
    private List<PointCordinate> pointFList = new ArrayList<>();
    public PinView(Context context) {
        super(context);

        this.mcontext = context;

        // this(context, null);
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        pointFList.clear();
        initialise(dbm,pointype);
    }

    public void setPin(PointF sPin,int dbm,int position,String pointype) {
        this.sPin = sPin;
        pointFList.add(new PointCordinate(sPin,dbm,position,pointype));
        initialise(dbm,pointype);
        invalidate();
    }
    public void RemoveLastIndex(){
        Log.d("bffSize", String.valueOf(pointFList.size()));
        if(pointFList.size()>0){
            //((MainActivity)mcontext).ll_undo.setEnabled(false);

            pointFList.remove(pointFList.size() - 1);
            initialise(dbm,pointype);
            invalidate();
            Log.d("bffSize2", String.valueOf(pointFList.size()));

        }

    }
    public List<PointCordinate> getArrayList(){
        return pointFList;
    }

    private void initialise(int dbm,String pointype) {

         density = getResources().getDisplayMetrics().density;
        Log.d("density",""+density);
        r = new Rect();
        if (density >= 4.0) {
            //"xxxhdpi";640dpi
            float densityDpi = getResources().getDisplayMetrics().densityDpi;
            Log.d("densityDpi",""+densityDpi);
            if(pointype.equals("pin"))
                PinSetup(densityDpi,4000f,dbm);
            else if(pointype.equals("device"))
                DevicePinSetup(densityDpi,3300f);

        }
        else if (density >= 3.0) {
            //xxhdpi 480dpi
            float densityDpi = getResources().getDisplayMetrics().densityDpi;
            Log.d("densityDpi",""+densityDpi);
          //  pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.green);
            if(pointype.equals("pin"))
                PinSetup(densityDpi,3000f,dbm);
            else if(pointype.equals("device"))
                DevicePinSetup(densityDpi,2300f);
        }
        else if (density >= 2.0) {
            //xhdpi 320dpi
            float densityDpi = getResources().getDisplayMetrics().densityDpi;
            Log.d("densityDpi",""+densityDpi);
           // pin = BitmapFactory.decodeResource(this.getResources(), R.drawable.green);
            if(pointype.equals("pin"))
                PinSetup(densityDpi,2000f,dbm);
            else if(pointype.equals("device"))
                DevicePinSetup(densityDpi,1300f);


        }
        else if (density >= 1.5 && density < 2.0) {
            //hdpi 240dpi
        }
        else if (density >= 1.0 && density < 1.5) {
            //mdpi 160dpi
        }

    }

    public float getPinWidth(){
        return  pinWidht;
    }
    public float getPinHeight(){ return  pinHeight; }

    public float getDevicePinWidth(){
        return  devicepinWidht;
    }
    public float getDeviceHeight(){ return  devicepinHeight; }
    private void DevicePinSetup(float densityDpi,float divided){
        devicepin = BitmapFactory.decodeResource(this.getResources(), R.drawable.accesspoint);
        float w = (densityDpi/divided) * (devicepin.getWidth());
        float h = (densityDpi/divided) * (devicepin.getHeight());
        Log.d("devicepinWidht",""+w);
        Log.d("devicepinHeight",""+h);
        devicepinWidht = w;
        devicepinHeight = h;
        devicepin = Bitmap.createScaledBitmap(devicepin, (int)w, (int)h, true);
    }
    private void PinSetup(float densityDpi,float divided,int dbm){

        Log.d("testdbm", String.valueOf(dbm));
        if (dbm >= -55) {
            greenpin = BitmapFactory.decodeResource(this.getResources(), R.drawable.green);
            float w = (densityDpi/divided) * (greenpin.getWidth());
            float h = (densityDpi/divided) * (greenpin.getHeight());
            Log.d("okw_green",""+w);
            Log.d("okw_green",""+h);
            pinWidht = w;
            pinHeight = h;
            greenpin = Bitmap.createScaledBitmap(greenpin, (int)w, (int)h, true);
        } else if (dbm < -55 && dbm >= -60) {
            greenpin = BitmapFactory.decodeResource(this.getResources(), R.drawable.yellow);
            float w = (densityDpi/divided) * (greenpin.getWidth());
            float h = (densityDpi/divided) * (greenpin.getHeight());
            Log.d("okw_yellow",""+w);
            Log.d("okw_yellow",""+h);
            pinWidht = w;
            pinHeight = h;
            greenpin = Bitmap.createScaledBitmap(greenpin, (int)w, (int)h, true);
        } else if (dbm < -60 && dbm >= -70) {
            yellowpin = BitmapFactory.decodeResource(this.getResources(), R.drawable.yellow);
            float w = (densityDpi/divided) * (yellowpin.getWidth());
            float h = (densityDpi/divided) * (yellowpin.getHeight());
            Log.d("okw",""+w);
            Log.d("okw",""+h);
            pinWidht = w;
            pinHeight = h;
            yellowpin = Bitmap.createScaledBitmap(yellowpin, (int)w, (int)h, true);
        } else if (dbm < -70 && dbm >= -80) {
            redpin = BitmapFactory.decodeResource(this.getResources(), R.drawable.red);
            float w = (densityDpi/divided) * (redpin.getWidth());
            float h = (densityDpi/divided) * (redpin.getHeight());
            Log.d("okw",""+w);
            Log.d("okw",""+h);
            pinWidht = w;
            pinHeight = h;
            redpin = Bitmap.createScaledBitmap(redpin, (int)w, (int)h, true);
        } else {
            redpin = BitmapFactory.decodeResource(this.getResources(), R.drawable.red);
            float w = (densityDpi/divided) * (redpin.getWidth());
            float h = (densityDpi/divided) * (redpin.getHeight());
            Log.d("okw",""+w);
            Log.d("okw",""+h);
            pinWidht = w;
            pinHeight = h;
            redpin = Bitmap.createScaledBitmap(redpin, (int)w, (int)h, true);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        Log.d("size",""+pointFList.size());
        if (density >= 4.0) {
            //"xxxhdpi";640dpi

        }
        else if (density >= 3.0) {
            //xxhdpi 480dpi
            for (PointCordinate pointCordinate: pointFList) {
                paint.setAntiAlias(true);
                if(pointCordinate.pointype.equals("pin")){
                    if (pointCordinate.dbm >= -55) {
                        pin = greenpin;
                    } else if (pointCordinate.dbm < -55 && pointCordinate.dbm >= -60) {
                        pin=greenpin;
                    } else if (pointCordinate.dbm < -60 && pointCordinate.dbm >= -70) {
                        pin=yellowpin;
                    } else if (pointCordinate.dbm < -70 && pointCordinate.dbm >= -80) {
                        pin=redpin;
                    } else {
                        pin=redpin;

                    }
                }else{
                    pin = devicepin;
                }

                if (pointCordinate.pointF != null && pin != null) {
                    sourceToViewCoord(pointCordinate.pointF, vPin);
                    float vX = vPin.x - (pin.getWidth()/2);
                    float vY = vPin.y - pin.getHeight();
                   /* ColorFilter filter = new PorterDuffColorFilter(ContextCompat.getColor(mcontext,R.color.wifired), PorterDuff.Mode.SRC_IN);
                    paint.setColorFilter(filter);*/

                   // r.set(getWidth()/2, getHeight()/2, getWidth()/2 + 200, getHeight()/2 + 200);
                    canvas.drawBitmap(pin, vX+10f, vY, paint);
                   // canvas.drawBitmap(pin, null, r, paint);

                    if(pointCordinate.pointype.equals("pin")){
                        Paint paint = new Paint();
                        Paint.FontMetrics fm = new Paint.FontMetrics();
                        paint.setColor(getResources().getColor(R.color.black_transparent));
                        paint.getFontMetrics(fm);
                        int margin = 55;
               /* canvas.drawRect(vX - 5, vY + (fm.top + 70),
                        (vX+10f) + paint.measureText("text") + margin, (vY+90f)- fm.bottom
                                + 15, paint);*/

                        RectF rectF = new RectF(vX-5f , vY + (fm.top + 70),
                                (vX+20f) + paint.measureText(String.valueOf(pointCordinate.dbm)+" dbm") + margin, (vY+120f)- fm.bottom
                                + 10);

                        canvas.drawRoundRect(rectF, 25,25,paint);
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(30);
                        canvas.drawText(String.valueOf(pointCordinate.dbm)+" dbm", vX+10f, vY+100f, paint);
                    }

                }
            }
        }
        else if (density >= 2.0) {
            //xhdpi 320dpi
            for (PointCordinate pointCordinate: pointFList) {
                paint.setAntiAlias(true);
                if(pointCordinate.pointype.equals("pin")){
                    if (pointCordinate.dbm >= -55) {
                        pin = greenpin;
                    } else if (pointCordinate.dbm < -55 && pointCordinate.dbm >= -60) {
                        pin=greenpin;
                    } else if (pointCordinate.dbm < -60 && pointCordinate.dbm >= -70) {
                        pin=yellowpin;
                    } else if (pointCordinate.dbm < -70 && pointCordinate.dbm >= -80) {
                        pin=redpin;
                    } else {
                        pin=redpin;

                    }
                }else{
                    pin = devicepin;
                }
                if (pointCordinate.pointF != null && pin != null) {
                    sourceToViewCoord(pointCordinate.pointF, vPin);
                    float vX = vPin.x - (pin.getWidth()/2);
                    float vY = vPin.y - pin.getHeight();
                 /*   Log.d("vX", String.valueOf(vX));
                    Log.d("vY", String.valueOf(vY));
                    Log.d("left", String.valueOf(getWidth()/2));
                    Log.d("top", String.valueOf(getHeight()/2));
                    Log.d("right", String.valueOf(getWidth()/2 + 200));
                    Log.d("bottom", String.valueOf(getWidth()/2 + 200));
                    r.set((int) vX+10, (int) vY, (int) vX , (int) vY );

                    canvas.drawBitmap(pin, null, r, paint);*/
                   // canvas.dr
                   canvas.drawBitmap(pin, vX+10f, vY, paint);

                    if(pointCordinate.pointype.equals("pin")){
                        Paint paint = new Paint();
                        Paint.FontMetrics fm = new Paint.FontMetrics();
                        paint.setColor(getResources().getColor(R.color.black_transparent));
                        paint.getFontMetrics(fm);
                        int margin = 40;
               /* canvas.drawRect(vX - 5, vY + (fm.top + 70),
                        (vX+10f) + paint.measureText("text") + margin, (vY+90f)- fm.bottom
                                + 15, paint);*/

                        RectF rectF = new RectF(vX-5f , vY + (fm.top + 50),
                                (vX+5f) + paint.measureText(String.valueOf(pointCordinate.dbm)+" dbm") + margin, (vY+65f)- fm.bottom
                                + 15);
                        canvas.drawRoundRect(rectF, 15,15,paint);
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(20);
                        canvas.drawText(String.valueOf(pointCordinate.dbm)+" dbm", vX+10f, vY+65f, paint);
                    }


                }
            }
        }
        else if (density >= 1.5 && density < 2.0) {
            //hdpi 240dpi
        }
        else if (density >= 1.0 && density < 1.5) {
            //mdpi 160dpi
        }



    }



   /* public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        // Invalidate() is inside the case statements because there are many
        // other types of motion events passed into this listener,
        // and we don't want to invalidate the view for those.
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("click","click");
                //touchStart(x, y);
                // No need to invalidate because we are not drawing anything.
                break;
            case MotionEvent.ACTION_MOVE:
                //touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                //touchUp();
                break;
            default:
                // do nothing
        }
        return true;
    }*/

}
