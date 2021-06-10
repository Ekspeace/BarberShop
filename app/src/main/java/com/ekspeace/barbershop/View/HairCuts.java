package com.ekspeace.barbershop.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ekspeace.barbershop.Adapter.HairCutAdapter;
import com.ekspeace.barbershop.Adapter.UserAppAdapter;
import com.ekspeace.barbershop.Constants.PopUp;
import com.ekspeace.barbershop.Constants.Utils;
import com.ekspeace.barbershop.Model.Barber;
import com.ekspeace.barbershop.Model.HairCut;
import com.ekspeace.barbershop.Model.UserAppointment;
import com.ekspeace.barbershop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.util.ArrayList;
import java.util.List;

public class HairCuts extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private ImageView ivBack, ivHome;
    private RecyclerView recyclerView;
    private ConstraintLayout loading;
    private List<HairCut> hairCuts;
    private Bitmap bitmapImage;
    private String haircutName;
    private PopUp popUp;
    private View layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hair_cut);
        String service = getIntent().getStringExtra(Utils.service);
        popUp = new PopUp();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(haircutNamePosReceiver,new IntentFilter("HAIRCUT_NAME"));
        localBroadcastManager.registerReceiver(NetworkError,new IntentFilter(Utils.connection));
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));

        InitialiseWidgets();
        Init();
        ClickEvents(service);
        LoadHaircuts();
    }
    private void InitialiseWidgets(){
        floatingActionButton = findViewById(R.id.hair_cut_forward_btn);
        ivBack = findViewById(R.id.hair_cut_back);
        ivHome = findViewById(R.id.hair_cut_home);
        recyclerView = findViewById(R.id.hair_cut_recyclerView);
        loading = findViewById(R.id.progress_bar_haircut);
    }
    private void Init(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        hairCuts = new ArrayList<>();
    }
    private void ClickEvents(String service){
        floatingActionButton.setOnClickListener(view -> {
            if(haircutName == null){
                PopUp.Toast(this, layout, "Please choose a haircut...", Toast.LENGTH_SHORT);
                return;
            }
            Intent intent;
            if(haircutName.contains("Own")) { intent = new Intent(this, InsertHairCutPicture.class);
            }else { intent = new Intent(this, Appointment.class); }
            String Service = service + " - " + haircutName;
            intent.putExtra(Utils.service, Service);
            startActivity(intent);

        });
        ivBack.setOnClickListener(view -> finish());
        ivHome.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
    }
    private void LoadHaircuts(){
        loading.setVisibility(View.VISIBLE);
        if(Utils.isOnline(this)) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("HairCut");
            query.findInBackground((objects, e) -> {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        String haircutName = objects.get(i).getString("Name");
                        ParseFile file = objects.get(i).getParseFile("Image");
                        assert file != null;
                        try {
                            byte[] data = file.getData();
                            bitmapImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                            loading.setVisibility(View.GONE);
                            PopUp.Toast(this, layout, e.getMessage(), Toast.LENGTH_LONG);
                        }
                        Bitmap haircutImage = bitmapImage;
                        hairCuts.add(new HairCut(haircutName, haircutImage));
                    }
                    HairCutAdapter adapter = new HairCutAdapter(this,hairCuts);
                    recyclerView.setAdapter(adapter);
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
    private BroadcastReceiver haircutNamePosReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            haircutName = intent.getStringExtra("HAIRCUT_NAME_POS");
        }
    };
    private final BroadcastReceiver NetworkError = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LoadHaircuts();
        }
    };

}