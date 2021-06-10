package com.ekspeace.barbershop.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.ekspeace.barbershop.R;

public class WelcomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome_screen);

        Button btn_services = findViewById(R.id.check_service_btn);

        btn_services.setOnClickListener((view) ->{startActivity(new Intent(WelcomeScreen.this, MainActivity.class));});
    }
}