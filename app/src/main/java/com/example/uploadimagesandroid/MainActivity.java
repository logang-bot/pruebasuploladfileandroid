package com.example.uploadimagesandroid;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.PathUtils;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.uploadimagesandroid.interfaces.RetrofitClient;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private int PICK_IMAGE = 100;
    private Button upload, pick;
    private ImageView img;
    private Uri imagenUri;
    private MainActivity root;
    private String mediaPath, postPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upload = findViewById(R.id.uploadimage);
        pick = findViewById(R.id.pickimage);
        img = findViewById(R.id.imagen);

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGalery();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //CONVERT
                //name
                RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), "nombre1");
                //email
                RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), "email1");
                //password
                RequestBody password = RequestBody.create(MediaType.parse("multipart/form-data"), "password1");
                //sex
                RequestBody sex = RequestBody.create(MediaType.parse("multipart/form-data"), "sex1");
                //address
                RequestBody address = RequestBody.create(MediaType.parse("multipart/form-data"), "address1");


                //img
                File file = new File(postPath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                MultipartBody.Part body = MultipartBody.Part.createFormData("img", file.getName(), requestFile);
                //CONVERT

                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "no permission", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 225);
                } else {
                    Call<RequestBody> call = RetrofitClient
                            .getInstance()
                            .getApi().uploadImage(name,email,password,sex,address,body);

                    call.enqueue(new Callback<RequestBody>() {
                        @Override
                        public void onResponse(Call<RequestBody> call, Response<RequestBody> response) {
                            Toast.makeText(getApplicationContext(), "imagen subida", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<RequestBody> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                    Log.i("Mensaje", "Se tiene permiso para leer!");
                }
            }
        });

    }
    public void openGalery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE){
            if(resultCode == RESULT_OK){
                ClipData clipdata = data.getClipData();
                if(clipdata == null){
                    imagenUri = data.getData();

                /*Uri selectedImage = imagenUri;
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(imagenUri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                // Set the Image in ImageView for Previewing the Media
                //imageView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();

                postPath = mediaPath;*/
                    Uri uri = data.getData();
                    File file = new File(uri.getPath());//create path from uri
                    final String[] split = file.getPath().split(":");//split the path.
                    //String filePath = split[1];//assign it to a string(your choice).
                    postPath = file.getPath().substring(4);
                    Toast.makeText(getApplicationContext(), postPath, Toast.LENGTH_LONG).show();
                }
                img.setImageURI(imagenUri);
            }
            else
                Toast.makeText(getApplicationContext(), "cancelado", Toast.LENGTH_LONG).show();
        }

        /*if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            //Toast.makeText(this, "---", Toast.LENGTH_SHORT).show();
            //one
            if(clipdata == null){
                imagenUri = data.getData();

                /*Uri selectedImage = imagenUri;
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(imagenUri, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                // Set the Image in ImageView for Previewing the Media
                //imageView.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                cursor.close();

                postPath = mediaPath;
                Uri uri = data.getData();
                File file = new File(uri.getPath());//create path from uri
                final String[] split = file.getPath().split(":");//split the path.
                //String filePath = split[1];//assign it to a string(your choice).
                postPath = file.getPath().substring(4);
                Toast.makeText(getApplicationContext(), postPath, Toast.LENGTH_LONG).show();
            }
        }
        img.setImageURI(imagenUri);*/
    }
}