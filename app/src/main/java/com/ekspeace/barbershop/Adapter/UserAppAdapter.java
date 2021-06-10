package com.ekspeace.barbershop.Adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.barbershop.Constants.PopUp;
import com.ekspeace.barbershop.Constants.Utils;
import com.ekspeace.barbershop.Interface.IRecyclerItemSelectedListener;
import com.ekspeace.barbershop.Model.UserAppointment;
import com.ekspeace.barbershop.R;

import java.util.List;

public class UserAppAdapter  extends RecyclerView.Adapter<UserAppAdapter.MyViewHolder> {
    private Context context;
    private List<UserAppointment> userAppointments;
    private View layout;
    private int Position;
    public UserAppAdapter(Context context, List<UserAppointment> userAppointments, View layout, LocalBroadcastManager localBroadcastManager){
        this.context =context;
        this.userAppointments = userAppointments;
        this.layout = layout;
        localBroadcastManager.registerReceiver(NetworkError,new IntentFilter(Utils.connection));
    }

    @NonNull
    @Override
    public UserAppAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_recycler_user_appointment,parent, false);
        return new UserAppAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAppAdapter.MyViewHolder holder, int position) {
        UserAppointment userAppointment = userAppointments.get(position);
        holder.userName.setText(userAppointment.getName());
        holder.userNumber.setText(userAppointment.getNumber());
        holder.userService.setText(userAppointment.getService());
        holder.userTimeDate.setText(userAppointment.getTimeDate());
        holder.userBarber.setText(userAppointment.getBarber());

       holder.userDeleteImage.setOnClickListener(view -> {
           Position = holder.getLayoutPosition();
           PopUp.DeleteDialog(context,"Delete Appointment", "Are you sure, you want to delete this appointment ?", userAppointments, Position, layout);
       });
    }

    @Override
    public int getItemCount() {
        return userAppointments.size();
    }
    private final BroadcastReceiver NetworkError = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context1, Intent intent) {
            PopUp.DeleteDialog(context,"Delete Appointment", "Are you sure, you want to delete this appointment ?", userAppointments, Position, layout);
        }
    };
    public class MyViewHolder  extends RecyclerView.ViewHolder {
        private TextView userName, userNumber, userService, userBarber, userTimeDate;
        private ImageView userDeleteImage;
        private IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_tv);
            userNumber = itemView.findViewById(R.id.user_number_tv);
            userService = itemView.findViewById(R.id.user_service_tv);
            userBarber = itemView.findViewById(R.id.user_barber_tv);
            userTimeDate = itemView.findViewById(R.id.user_time_date_tv);
            userDeleteImage = itemView.findViewById(R.id.user_delete_iv);
           // itemView.setOnClickListener(this);
        }

      /*  @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAbsoluteAdapterPosition());
        }*/
    }
}
