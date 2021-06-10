package com.ekspeace.barbershop.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ekspeace.barbershop.Adapter.TimeSlotAdapter;
import com.ekspeace.barbershop.Constants.PopUp;
import com.ekspeace.barbershop.Constants.SpacesItemDecoration;
import com.ekspeace.barbershop.Constants.Utils;
import com.ekspeace.barbershop.Model.Barber;
import com.ekspeace.barbershop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Appointment extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CalendarView calendarView;
    private FloatingActionButton floatingActionButton;
    private ImageView ivBack, ivHome;
    private String selectedDate, selectedTime;
    private View layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        String service = getIntent().getStringExtra(Utils.service);

        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(timeSlotPosReceiver,new IntentFilter("TIME_SLOT"));
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));

    InitialiseWidgets();
    Init();
    ClickEvents(service);
    }

    private void InitialiseWidgets(){
        recyclerView = findViewById(R.id.appointment_recycler);
        calendarView = findViewById(R.id.calendarView);
        floatingActionButton = findViewById(R.id.appointment_forward_btn);
        ivBack = findViewById(R.id.appointment_back);
        ivHome = findViewById(R.id.appointment_home);
    }
    private void Init() {
        calendarView.setMinDate(calendarView.getDate());
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpacesItemDecoration(8));
        TimeSlotAdapter adapter = new TimeSlotAdapter(this);
        recyclerView.setAdapter(adapter);

        calendarView.setOnDateChangeListener((calendarView, i, i1, i2) -> {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM");
            Calendar calendar = Calendar.getInstance();
            calendar.set(i, i1, i2);
            selectedDate = simpleDateFormat.format(calendar.getTime());
        });
    }
    private void ClickEvents(String service){
        ivBack.setOnClickListener(view -> finish());
        ivHome.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));

        floatingActionButton.setOnClickListener(view -> {
            if(selectedDate == null || selectedTime == null){
                PopUp.Toast(this, layout, "Please select date and time...", Toast.LENGTH_SHORT);
                return;
            }
            else if(selectedDate.isEmpty() || selectedTime.isEmpty()){
                PopUp.Toast(this, layout, "Please select date and time...", Toast.LENGTH_SHORT);
                return;
            }
            String time = selectedTime + " - " + selectedDate;
           Intent intent = new Intent(this, Barbers.class);
           intent.putExtra(Utils.service, service);
           intent.putExtra(Utils.timeDate, time);
           startActivity(intent);
        });
    }
    private BroadcastReceiver timeSlotPosReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                selectedTime = intent.getStringExtra("TIME_SLOT_POS");
            }
    };

}