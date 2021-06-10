package com.ekspeace.barbershop.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekspeace.barbershop.Constants.Utils;
import com.ekspeace.barbershop.Model.Barber;
import com.ekspeace.barbershop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class InsertHairCutPicture extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private Button btnUpload;
    private TextView tvImage;
    private ImageView ivBack, ivHome, ivImageUpload;
    public static byte[] inputData;
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_hair_cut_picture);
        String service = getIntent().getStringExtra(Utils.service);
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    EXTERNAL_STORAGE_REQ_CODE);
        }

        InitialiseWidgets();
        ClickEvents(service);
    }
    private void InitialiseWidgets() {
        floatingActionButton = findViewById(R.id.User_hair_cut_fb);
        btnUpload = findViewById(R.id.User_hair_cut_upload);
        ivBack = findViewById(R.id.User_hair_cut_back);
        ivHome = findViewById(R.id.User_hair_cut_home);
        ivImageUpload = findViewById(R.id.User_hair_cut_image);
        tvImage = findViewById(R.id.User_image_here_text);
    }
    private void ClickEvents(String service) {
        ivBack.setOnClickListener(view -> finish());
        ivHome.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));

        btnUpload.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        floatingActionButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, Appointment.class);
            if(inputData != null){
                intent.putExtra(Utils.service, service);
                startActivity(intent);
            }else {
                Toast.makeText(this, "Please add an image...", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            Uri selectedImage = data.getData();
            InputStream iStream = null;
            try {
                iStream = getContentResolver().openInputStream(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap myBitmap = null;
            try {
                myBitmap = getBitmapFromUri(selectedImage);
                inputData = getBytes(iStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ivImageUpload.setImageBitmap(myBitmap);
            tvImage.setVisibility(View.GONE);
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor = this.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}