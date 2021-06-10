package com.ekspeace.barbershop.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ekspeace.barbershop.Adapter.UserAppAdapter;
import com.ekspeace.barbershop.Constants.PopUp;
import com.ekspeace.barbershop.Constants.Utils;
import com.ekspeace.barbershop.Model.UserAppointment;
import com.ekspeace.barbershop.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserAppointmentHistory extends AppCompatActivity {
    private ImageView ivBack, ivHome;
    private RecyclerView recyclerView;
    private TextView tvNoApps;
    private View layout;
    private PopUp popUp;
    private ConstraintLayout loading;
    private List<UserAppointment> userApps;
    private LocalBroadcastManager localBroadcastManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment_history);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(receiver,new IntentFilter("RESTART"));
        InitializeWidgets();
        Init();
        ClickEvents();
        LoadAppointments();
    }
    private void InitializeWidgets(){
        ivBack = findViewById(R.id.user_appointment_back);
        ivHome = findViewById(R.id.user_appointment_home);
        recyclerView = findViewById(R.id.user_appointment_recyclerView);
        tvNoApps = findViewById(R.id.no_apps_txt);
        loading = findViewById(R.id.progress_bar_user_app);

    }
    private void ClickEvents(){
        ivBack.setOnClickListener(view -> finish());
        ivHome.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
    }
    private void Init(){
        popUp = new PopUp();
        userApps = new ArrayList<>();
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }
    private void LoadAppointments() {
        loading.setVisibility(View.VISIBLE);
        if (Utils.isOnline(this)) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("UserAppointment");
            query.findInBackground((objects, e) -> {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        String userName = objects.get(i).getString("userName");
                        String userNumber = objects.get(i).getString("userNumber");
                        String userService = objects.get(i).getString("userService");
                        String userTimeDate = objects.get(i).getString("userTimeDate");
                        String userBarber = objects.get(i).getString("userBarber");

                        // on below line we are adding data to our array list.
                        UserAppointment userAppointment = new UserAppointment(userName, userNumber, userService, userTimeDate, userBarber);
                        userApps.add(userAppointment);
                    }
                    UserAppAdapter adapter = new UserAppAdapter(this, userApps, layout, localBroadcastManager);
                    recyclerView.setAdapter(adapter);
                    if(userApps.size() == 0) {tvNoApps.setVisibility(View.VISIBLE);}
                    loading.setVisibility(View.GONE);
                } else {
                    loading.setVisibility(View.GONE);
                    PopUp.Toast(this, layout, "ERROR: " + e.getMessage(), Toast.LENGTH_LONG);
                }
            });
        }else {
            loading.setVisibility(View.GONE);
            popUp.InternetDialog(this);
        }
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            recreate();
        }
    };
}