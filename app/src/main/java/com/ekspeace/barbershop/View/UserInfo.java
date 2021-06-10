package com.ekspeace.barbershop.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ekspeace.barbershop.Constants.PopUp;
import com.ekspeace.barbershop.Constants.Utils;
import com.ekspeace.barbershop.R;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.File;

public class UserInfo extends AppCompatActivity {
    private ImageView ivBack, ivHome;
    private EditText etName, etNumber;
    private Button btnConfirm;
    private ConstraintLayout loading;
    private byte[] haircutImage, specialImage;
    private String specialInfo;
    private View layout;
    private PopUp popUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        popUp = new PopUp();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(NetworkError,new IntentFilter(Utils.connection));
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));

        InitializeWidgets();
        ClickEvents();
    }
    private void InitializeWidgets(){
        ivBack = findViewById(R.id.user_back);
        ivHome = findViewById(R.id.user_home);
        etName = findViewById(R.id.user_name);
        etNumber = findViewById(R.id.user_number);
        btnConfirm = findViewById(R.id.user_confirm);
        loading = findViewById(R.id.progress_bar_user);
        specialInfo = SpecialService.info;
        specialImage = SpecialService.inputData;
        haircutImage = InsertHairCutPicture.inputData;
    }
    private void ClickEvents(){
        ivBack.setOnClickListener(view -> finish());
        ivHome.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
        btnConfirm.setOnClickListener(view -> {
           SaveAppointment();
        });

    }
    private void SaveAppointment(){
        String service = getIntent().getStringExtra(Utils.service);
        String timeDate = getIntent().getStringExtra(Utils.timeDate);
        String barber = getIntent().getStringExtra(Utils.barber);
        String name = etName.getText().toString().trim();
        String number = etNumber.getText().toString().trim();
        if (TextUtils.isEmpty(number)) {
            etNumber.setError("Contact number is Required.");
            return;
        }

        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }
        btnConfirm.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
        if(Utils.isOnline(this)) {
            ParseObject userAppointment = new ParseObject("UserAppointment");
            userAppointment.put("userName", name);
            userAppointment.put("userNumber", number);
            userAppointment.put("userService", service);
            userAppointment.put("userTimeDate", timeDate);
            userAppointment.put("userBarber", barber);
            if(specialImage != null){ParseFile parseFile = new ParseFile(specialImage);userAppointment.put("userSpecialServiceImage", parseFile);}
            if(haircutImage != null){ParseFile parseFile = new ParseFile(haircutImage);userAppointment.put("userHaircutImage", parseFile);}
            if(specialInfo != null){if(!specialInfo.isEmpty())userAppointment.put("userSpecialServiceInfo", specialInfo);}
            userAppointment.saveInBackground(e -> {
                if (e == null) {
                    loading.setVisibility(View.GONE);
                    PopUp.Toast(UserInfo.this, layout, "Appointment has been successfully confirmed", Toast.LENGTH_SHORT);
                    startActivity(new Intent(UserInfo.this, MainActivity.class));
                } else {
                    loading.setVisibility(View.GONE);
                    btnConfirm.setVisibility(View.VISIBLE);
                    PopUp.Toast(UserInfo.this, layout, e.getMessage(), Toast.LENGTH_LONG);
                }
            });
        }else {
            loading.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.VISIBLE);
            popUp.InternetDialog(this);
        }

    }
    private final BroadcastReceiver NetworkError = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            SaveAppointment();
        }
    };
}