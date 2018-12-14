package com.uas.fahmiko.vpnuser;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.uas.fahmiko.vpnuser.helper.Preference;
import com.uas.fahmiko.vpnuser.model.GetUser;
import com.uas.fahmiko.vpnuser.rest.ApiClient;
import com.uas.fahmiko.vpnuser.rest.ApiInterface;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditUser extends AppCompatActivity {
    Button update;
    EditText id,name,user,pass;
    FloatingActionButton upload;
    ImageView mImageView;
    File mFileURI;
    Preference p;
    String imagePath = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        initComponents();

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
//                final Intent galleryIntent = new Intent();
//                galleryIntent.setType("image/*");
//                galleryIntent.setAction(Intent.ACTION_PICK);
//                Intent intentChoose = Intent.createChooser(
//                        galleryIntent,
//                        "Pilih foto untuk di-upload");
//                startActivityForResult(intentChoose, 1);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);

                MultipartBody.Part body = null;
                if (!imagePath.isEmpty()){
                    // Buat file dari image yang dipilih
                    File file = new File(imagePath);

                    // Buat RequestBody instance dari file
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

                    // MultipartBody.Part digunakan untuk mendapatkan nama file
                    body = MultipartBody.Part.createFormData("photo", file.getName(),
                            requestFile);
                }
                RequestBody reqName_user = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (name.getText().toString().isEmpty())?"":name.getText().toString());
                RequestBody reqLocation = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (user.getText().toString().isEmpty())?"":user.getText().toString());
                RequestBody reqAcc_remaining = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (pass.getText().toString().isEmpty())?"":pass.getText().toString());

                Call<GetUser> mUserCall;
                RequestBody reqId_user = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (id.getText().toString().isEmpty())?"":id.getText().toString());
                RequestBody reqAction = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        "update");
                mUserCall = mApiInterface.putUser(body, reqId_user,reqName_user,
                        reqLocation, reqAcc_remaining, reqAction );

                mUserCall.enqueue(new Callback<GetUser>() {
                    @Override
                    public void onResponse(Call<GetUser> call, Response<GetUser> response) {
//                      Log.d("Insert Retrofit",response.body().getMessage());
                        if (response.body().getStatus().equals("failed")) {
                            Toast.makeText(getApplicationContext(), "Data Gagal di Update", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "UPDATE Success", Toast.LENGTH_SHORT).show();
                            p.logout();
                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetUser> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "UPDATE Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

//            if (!isDeviceSupportCamera()) {
//                Toast.makeText(getApplicationContext(),  "Camera di device anda tidak tersedia",  Toast.LENGTH_LONG).show();
//                finish();
//            }
    }

    public void initComponents(){
        p = new Preference(getApplicationContext());
        id = findViewById(R.id.ed_id);
        name = findViewById(R.id.ed_name);
        user = findViewById(R.id.ed_username);
        pass = findViewById(R.id.ed_pass);
        mImageView = findViewById(R.id.img_user);
        update = findViewById(R.id.btn_update);
        id.setText(p.getUserPreferece().getId_user());
        name.setText(p.getUserPreferece().getName());
        user.setText(p.getUserPreferece().getUsername());
        pass.setText(p.getUserPreferece().getPassword());
        Glide.with(getApplicationContext()).load(ApiClient.BASE_URL+"uploads/users/"+p.getUserPreferece().getPhoto()).into(mImageView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().show();
        upload = findViewById(R.id.btn_upload);
    }

//    private void captureImage() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            mFileURI = getMediaFileName();
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFileURI));
//            startActivityForResult(takePictureIntent, 100);
//        }
//    }
//
//    private boolean isDeviceSupportCamera() {
//        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    private static File getMediaFileName() {
//        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraDemo");
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.e("CameraDemo", "Gagal membuat directory" + "CameraDemo");
//            }
//            return null;
//        }
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        File mediaFile = null;
//        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
//        return mediaFile;
//    }
    String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        File storageDir = Environment.getExternalStorageDirectory();
        File image = File.createTempFile(
                "example",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode ==10){
            if (requestCode == 1 && resultCode == RESULT_OK) {
                //Bundle extras = data.getExtras();
                //Bitmap imageBitmap = (Bitmap) extras.get("data");
                //mImageView.setImageBitmap(imageBitmap);
                galleryAddPic();
            }
            if (data==null){
                Toast.makeText(getApplicationContext(), "Foto gagal di-load", Toast.LENGTH_LONG).show();
            }
        }
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imagePath =cursor.getString(columnIndex);

            Picasso.with(getApplicationContext()).load(new File(imagePath)).fit().into(mImageView);
//                Glide.with(mContext).load(new File(imagePath)).into(mImageView);
            cursor.close();
        }else{
            Toast.makeText(getApplicationContext(), "Foto gagal di-load", Toast.LENGTH_LONG).show();
        }
    }
}