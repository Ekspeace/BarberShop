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
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekspeace.barbershop.Constants.PopUp;
import com.ekspeace.barbershop.Constants.Utils;
import com.ekspeace.barbershop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class SpecialService extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private Button btnUpload;
    private TextView tvImage;
    private EditText etAdditionalInfo;
    private ImageView ivBack, ivHome, ivImageUpload;
    public static byte[] inputData;
    public static String info;
    private View layout;
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_service);
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));
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
        floatingActionButton = findViewById(R.id.special_service_fb);
        btnUpload = findViewById(R.id.special_service_upload);
        ivBack = findViewById(R.id.special_service_back);
        ivHome = findViewById(R.id.special_service_home);
        ivImageUpload = findViewById(R.id.special_service_image);
        etAdditionalInfo = findViewById(R.id.special_service_additional_information);
        tvImage = findViewById(R.id.special_service_image_here_text);
    }
    private void ClickEvents(String service) {
        ivBack.setOnClickListener(view -> finish());
        ivHome.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));

        btnUpload.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        floatingActionButton.setOnClickListener(view -> {
            info = etAdditionalInfo.getText().toString().trim();
            if (info == null && inputData ==  null){
                PopUp.Toast(this, layout, "Please add one of them ....", Toast.LENGTH_SHORT);
                return;
            }else if(info.isEmpty() && inputData ==  null){
                PopUp.Toast(this, layout, "Please add one of them ....", Toast.LENGTH_SHORT);
                return;
            }
            Intent intent = new Intent(this, Appointment.class);
            intent.putExtra(Utils.service, service);
            startActivity(intent);
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