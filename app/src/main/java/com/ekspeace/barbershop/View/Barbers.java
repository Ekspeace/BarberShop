package com.ekspeace.barbershop.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ekspeace.barbershop.Adapter.BarberViewAdapter;
import com.ekspeace.barbershop.Adapter.HairCutAdapter;
import com.ekspeace.barbershop.Adapter.TimeSlotAdapter;
import com.ekspeace.barbershop.Constants.PopUp;
import com.ekspeace.barbershop.Constants.SpacesItemDecoration;
import com.ekspeace.barbershop.Constants.Utils;
import com.ekspeace.barbershop.Model.Barber;
import com.ekspeace.barbershop.Model.HairCut;
import com.ekspeace.barbershop.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Barbers extends AppCompatActivity {
    private FloatingActionButton floatingActionButton;
    private ImageView ivBack, ivHome;
    private RecyclerView recyclerView;
    private ConstraintLayout loading;
    private List<Barber> barbers;
    private Bitmap bitmapImage;
    private String barberName;
    private PopUp popUp;
    private View layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbers);
        String service = getIntent().getStringExtra(Utils.service);
        String timeDate = getIntent().getStringExtra(Utils.timeDate);
        popUp = new PopUp();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(barberNamePosReceiver,new IntentFilter("BARBER_NAME"));
        localBroadcastManager.registerReceiver(NetworkError,new IntentFilter(Utils.connection));
        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, findViewById(R.id.custom_toast_container));

        InitializeWidgets();
        Init();
        ClickEvents(service, timeDate);
        LoadBarbers();

    }
    private void InitializeWidgets(){
        floatingActionButton = findViewById(R.id.barber_fb);
        ivBack = findViewById(R.id.barber_back);
        ivHome = findViewById(R.id.barber_home);
        recyclerView = findViewById(R.id.barber_recyclerView);
        loading = findViewById(R.id.progress_barber);
    }
    private void Init(){
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        barbers = new ArrayList<>();
    }
    private void ClickEvents(String service, String timeDate){
        floatingActionButton.setOnClickListener(view -> {
            if(barberName == null){
                PopUp.Toast(this, layout, "Please choose a barber...", Toast.LENGTH_SHORT);
                return;
            }
            Intent intent = new Intent(this, UserInfo.class);
            intent.putExtra(Utils.service, service);
            intent.putExtra(Utils.timeDate, timeDate);
            intent.putExtra(Utils.barber, barberName);
            startActivity(intent);
        });
        ivBack.setOnClickListener(view -> finish());
        ivHome.setOnClickListener(view -> startActivity(new Intent(this, MainActivity.class)));
    }
    private void LoadBarbers(){
        loading.setVisibility(View.VISIBLE);
        if(Utils.isOnline(this)) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Barber");
            query.findInBackground((objects, e) -> {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        String barberName = objects.get(i).getString("Name");
                        String barberDescription = objects.get(i).getString("Description");
                        float barberRating = objects.get(i).getLong("Rating");
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
                        Bitmap barberImage = bitmapImage;
                        barbers.add(new Barber(barberName, barberDescription, barberImage, barberRating));
                    }
                    BarberViewAdapter adapter = new BarberViewAdapter(this,barbers);
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
    private BroadcastReceiver barberNamePosReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            barberName = intent.getStringExtra("BARBER_NAME_POS");
        }
    };
    private final BroadcastReceiver NetworkError = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LoadBarbers();
        }
    };

}