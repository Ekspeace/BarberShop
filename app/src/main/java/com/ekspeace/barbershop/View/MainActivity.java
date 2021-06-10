package com.ekspeace.barbershop.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.ekspeace.barbershop.Constants.PopUp;
import com.ekspeace.barbershop.Constants.Utils;
import com.ekspeace.barbershop.R;

public class MainActivity extends AppCompatActivity {
    private CardView cvBeardTrim, cvHairTrim, cvHairCut, cvHairStyle, cvSpecialService, cvShaving;
    private ImageView ivUserAppointment, ivLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeWidgets();
        ClickEvents();
    }
    private void InitializeWidgets(){
        cvBeardTrim = findViewById(R.id.beard_trim);
        cvHairCut = findViewById(R.id.hair_cut);
        cvHairStyle = findViewById(R.id.hair_styling);
        cvHairTrim = findViewById(R.id.hair_trim);
        cvSpecialService = findViewById(R.id.special_service);
        cvShaving = findViewById(R.id.shaving);
        ivLogo = findViewById(R.id.logo);

        ivUserAppointment = findViewById(R.id.user_appointment);
    }
    private void ClickEvents(){
        cvBeardTrim.setOnClickListener((view) -> {
            Intent intent = new Intent(this, Appointment.class);
            intent.putExtra(Utils.service, "Beard Trimming");
            startActivity(intent);
        });
        cvHairTrim.setOnClickListener((view) -> {
            Intent intent = new Intent(this, Appointment.class);
            intent.putExtra(Utils.service, "Hair Trimming");
            startActivity(intent);
        });
        cvHairCut.setOnClickListener((view) -> {
            Intent intent = new Intent(this, HairCuts.class);
            intent.putExtra(Utils.service, "Hair Cut");
            startActivity(intent);
        });
        cvHairStyle.setOnClickListener((view) -> {
            Intent intent = new Intent(this, Appointment.class);
            intent.putExtra(Utils.service, "Hair Styling");
            startActivity(intent);
        });
        cvSpecialService.setOnClickListener((view) -> {
            Intent intent = new Intent(this, SpecialService.class);
            intent.putExtra(Utils.service, "Special Service");
            startActivity(intent);
        });
        cvShaving.setOnClickListener((view) -> {
            Intent intent = new Intent(this, Appointment.class);
            intent.putExtra(Utils.service, "Beard Trimming");
            startActivity(intent);
        });

        ivUserAppointment.setOnClickListener((view -> {
            startActivity(new Intent(this, UserAppointmentHistory.class));
        }));

        ivLogo.setOnClickListener(view -> PopUp.AboutDialog(this));

    }
}