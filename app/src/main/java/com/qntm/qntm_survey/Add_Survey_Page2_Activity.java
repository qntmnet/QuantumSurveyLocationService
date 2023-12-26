package com.qntm.qntm_survey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qntm.qntm_survey.Adapter.AddLocationPlan_Adapter;
import com.qntm.qntm_survey.Bean.AddLocationBean2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Add_Survey_Page2_Activity extends AppCompatActivity {

    ImageView img_ungridlogo_actionbar_back,img_previewimage,img_upload;
    LinearLayout ll_next,ll_back;
    Uri urigallery,uricamera;
    private String imgPath,encodedImageGallery,encodedImageCamera,gallarypath,camerapath;
    String encodedImageGallery1,encodedImageCamera1;
    private final int GALLERY_REQUEST_CODE=1;
    private final  int CAMERA_REQUEST_CODE=0;
    private String cameraFilePath;
    private int userChoosenTask=0;
    private int userChoosenTask_banner=0;
    private int userChoosenTask_background=0;
    Button btn_add_location;
    List<AddLocationBean2> locationBeanArrayList = new ArrayList<AddLocationBean2>();
    AddLocationPlan_Adapter adapter;
    RecyclerView recyclerview;
    Bitmap selectedImage_GALLERY,selectedImage_CAMERA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_survey_page2);

        img_ungridlogo_actionbar_back = findViewById(R.id.img_ungridlogo_actionbar_back);
        img_previewimage = findViewById(R.id.img_previewimage);
        img_upload = findViewById(R.id.img_upload);
        //img_add = findViewById(R.id.img_add);
        ll_next = findViewById(R.id.ll_next);
        ll_back = findViewById(R.id.ll_back);
        btn_add_location = findViewById(R.id.btn_add_location);

        //img_add.setVisibility(View.VISIBLE);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddLocationPlan_Adapter(this,locationBeanArrayList);
        recyclerview.setAdapter(adapter);

        img_ungridlogo_actionbar_back.setOnClickListener(v -> onBackPressed());
        ll_back.setOnClickListener(v -> onBackPressed());
        btn_add_location.setOnClickListener(v -> {
            if(uricamera!=null){
               // locationBeanArrayList.add(new AddLocationBean("test",selectedImage_CAMERA));
                locationBeanArrayList.add(new AddLocationBean2("test",uricamera));
            }else if(urigallery!=null){
               // locationBeanArrayList.add(new AddLocationBean("test",selectedImage_GALLERY));
                locationBeanArrayList.add(new AddLocationBean2("test",urigallery));
            }

            adapter.notifyDataSetChanged();
            img_previewimage.setImageResource(0);
            img_previewimage.setVisibility(View.GONE);

        });
        ll_next.setOnClickListener(view -> {
            Intent i=new Intent(Add_Survey_Page2_Activity.this,Surve_List_Activity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        });

        img_upload.setOnClickListener(v -> {

            BottomSheetMenu_Add_Device();
        });
    }

    private void BottomSheetMenu_Add_Device() {
        // TextView user_title,txt_user_manager_certificate,txt_user_delete,txt_user_edit;
        // CircleImageView txt_user_edit;

        View view1 = getLayoutInflater().inflate(R.layout.image_action_menu, null);

        // BottomSheetDialog dialog = new BottomSheetDialog(NewEditGuestPortalActivity.this,R.style.BottomSheetDialogTheme);

        // dialog.setContentView(view1);
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_Survey_Page2_Activity.this);
        builder.setView(view1);
        AlertDialog dialog= builder.show();
        LinearLayout liner_certificate=view1.findViewById(R.id.liner_certificate);
        LinearLayout liner_delete=view1.findViewById(R.id.liner_delete);
        CircleImageView user_icon=view1.findViewById(R.id.user_icon);
        TextView txt_user_manager_certificate=view1.findViewById(R.id.txt_user_manager_certificate);
        TextView txt_delete_text=view1.findViewById(R.id.txt_delete_text);
        TextView user_title=view1.findViewById(R.id.user_title);
        ImageView img_freez=view1.findViewById(R.id.img_freez);
        ImageView img_delete=view1.findViewById(R.id.img_delete);
       // user_icon.setImageResource(R.mipmap.ic_qn_logo);
        img_freez.setImageResource(R.drawable.ic_photo_camera);
        img_delete.setImageResource(R.drawable.ic_gallery);
        user_title.setText("Take Action");
        txt_user_manager_certificate.setText("Take Photo Camera");
        txt_delete_text.setText("Take Photo Gallery");

        liner_certificate.setOnClickListener(v -> {
            dialog.dismiss();


            captureFromCamera();
        });
        liner_delete.setOnClickListener(v -> {
            dialog.dismiss();

            pickFromGallery();
        });


    }
    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GALLERY_REQUEST_CODE);
    }
    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this,"com.qntm.qntm_surveyor.provider", createImageFile()));

            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    uricamera=null;
                    //data.getData returns the content URI for the selected Image
                    urigallery = data.getData();
                    // String filepath= RealPathUtil.getRealPathFromURI_API19(CaptivePortal_Activity.this,urigallery);
                    Log.d("gallerypath",urigallery.getPath());
                    gallarypath=getPath(Add_Survey_Page2_Activity.this,urigallery);
                    final InputStream imageStream;
                    try {
                        imageStream = getContentResolver().openInputStream(urigallery);
                        selectedImage_GALLERY = BitmapFactory.decodeStream(imageStream);
                        //selectedImage.compress(Bitmap.CompressFormat.PNG,4,null);
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(selectedImage_GALLERY, 250, 250, false);
                        //image = ConvertBitmapToString(resizedBitmap)
                        if(userChoosenTask==2){
                            encodedImageGallery = encodeImage(resizedBitmap);
                        }else if(userChoosenTask_background==2){
                            encodedImageGallery1 = encodeImage(resizedBitmap);
                        }

                        //img_logo.setImageURI(urigallery);
                        //img_logo.setRotation(90f);
                        setCapturedImage(gallarypath);

                        /*if(ismain==3){
                            bitmapArrayList.add(resizedBitmap);
                            myCustomPagerAdapter.notifyDataSetChanged();
                            if(bitmapArrayList.size()==1)
                                viewPagerIndicator.setPager(viewPager);
                        }*/
                        //Log.d("gallery",encodedImageGallery);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
                case CAMERA_REQUEST_CODE:

                    if(cameraFilePath!=null && !cameraFilePath.isEmpty()){
                        urigallery=null;
                        uricamera= Uri.parse(cameraFilePath);
                        //img_logo.setImageURI(Uri.parse(cameraFilePath));
                        // img_logo.setRotation(90f);
                        Log.d("camerapath_<10",uricamera.getPath());
                        camerapath=getPath(Add_Survey_Page2_Activity.this,uricamera);
                        final InputStream imageStream2;
                        try {
                            imageStream2 = getContentResolver().openInputStream(Uri.parse(cameraFilePath));
                             selectedImage_CAMERA = BitmapFactory.decodeStream(imageStream2);
                            Bitmap resizedBitmap = Bitmap.createScaledBitmap(selectedImage_CAMERA, 250, 250, false);
                            // Log.d("Camera",encodedImageCamera);
                            if(userChoosenTask==1){
                                encodedImageCamera = encodeImage(resizedBitmap);
                            }else if(userChoosenTask_background==1){
                                encodedImageCamera1 = encodeImage(resizedBitmap);
                            }
                           /* if(ismain==3){
                                bitmapArrayList.add(resizedBitmap);
                                myCustomPagerAdapter.notifyDataSetChanged();
                                if(bitmapArrayList.size()==1)
                                    viewPagerIndicator.setPager(viewPager);
                            }*/
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        setCapturedImage(camerapath);
                    }




                    break;
            }
    }
    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.setHasAlpha(true);
        bm.compress(Bitmap.CompressFormat.PNG, 100,baos);
        byte[] b = baos.toByteArray();

        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private boolean hasImage(@NonNull ImageView view) {
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
        }

        return hasImage;
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM),"Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Uri createImageFileuri(){
        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "name");
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/Camera/");

        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        if (uri == null) {
            Log.e("TAG", "uri is null");

        }
        Log.e("TAG", "uri=" + uri);
        return uri;
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.KITKAT;
        Log.i("URI",uri+"");
        String result = uri+"";
        // DocumentProvider
        //  if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
        if (isKitKat && (result.contains("media.documents"))) {

            String[] ary = result.split("/");
            int length = ary.length;
            String imgary = ary[length-1];
            final String[] dat = imgary.split("%3A");

            final String docId = dat[1];
            final String type = dat[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {

            } else if ("audio".equals(type)) {
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[] {
                    dat[1]
            };

            return getDataColumn(context, contentUri, selection, selectionArgs);
        }
        else
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    private void setCapturedImage(final String imagePath){

        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... params) {
                try {
                    return getRightAngleImage(imagePath);
                }catch (Throwable e){
                    e.printStackTrace();
                }
                return imagePath;
            }

            @Override
            protected void onPostExecute(String imagePath) {
                super.onPostExecute(imagePath);
                img_previewimage.setVisibility(View.VISIBLE);
                img_previewimage.setImageBitmap(decodeFile(imagePath));
               /* if(ismain==1)
                    img_logo.setImageBitmap(decodeFile(imagePath));
                else if(ismain==2)
                    mainview.setImageBitmap(decodeFile(imagePath));*/

            }
        }.execute();
    }

    private String getRightAngleImage(String photoPath) {

        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int degree = 0;

            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    degree = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                case ExifInterface.ORIENTATION_UNDEFINED:
                    degree = 0;
                    break;
                default:
                    degree = 90;
            }

            return rotateImage(degree,photoPath);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return photoPath;
    }

    private String rotateImage(int degree, String imagePath){

        if(degree<=0){
            return imagePath;
        }
        try{
            Bitmap b= BitmapFactory.decodeFile(imagePath);

            Matrix matrix = new Matrix();
            if(b.getWidth()>b.getHeight()){
                matrix.setRotate(degree);
                b = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(),
                        matrix, true);
            }

            FileOutputStream fOut = new FileOutputStream(imagePath);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            String imageType = imageName.substring(imageName.lastIndexOf(".") + 1);

            FileOutputStream out = new FileOutputStream(imagePath);
            if (imageType.equalsIgnoreCase("png")) {
                b.compress(Bitmap.CompressFormat.PNG, 100, out);
            }else if (imageType.equalsIgnoreCase("jpeg")|| imageType.equalsIgnoreCase("jpg")) {
                b.compress(Bitmap.CompressFormat.JPEG, 100, out);
            }
            fOut.flush();
            fOut.close();

            b.recycle();
        }catch (Exception e){
            e.printStackTrace();
        }
        return imagePath;
    }



    public Bitmap decodeFile(String path) {
        try {
            // Decode deal_image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }



    private Uri getUri() {
        String state = Environment.getExternalStorageState();
        if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    public Uri setImageUri() {
        Uri imgUri;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/",getString(R.string.app_name) + Calendar.getInstance().getTimeInMillis() + ".png");
            imgUri = Uri.fromFile(file);
            imgPath = file.getAbsolutePath();
        }else {
            File file = new File(getFilesDir() ,getString(R.string.app_name) + Calendar.getInstance().getTimeInMillis()+ ".png");
            imgUri = Uri.fromFile(file);
            this.imgPath = file.getAbsolutePath();
        }
        return imgUri;
    }

    public String getImagePath() {
        return imgPath;
    }

}