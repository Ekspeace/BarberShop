package com.ekspeace.barbershop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.barbershop.Constants.Utils;
import com.ekspeace.barbershop.Interface.IRecyclerItemSelectedListener;
import com.ekspeace.barbershop.R;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.MyViewHolder> {

   private Context context;
   private List<TextView> textViewList;
   private LocalBroadcastManager localBroadcastManager;

    public TimeSlotAdapter(Context context) {
        this.context = context;
        textViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public TimeSlotAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotAdapter.MyViewHolder holder, int position) {
         holder.txt_time_slot.setText(Utils.convertTimeToString(position));

        if(!textViewList.contains(holder.txt_time_slot))
            textViewList.add(holder.txt_time_slot);
        holder.setRecyclerItemSelectedListener((view, pos) -> {
            for (TextView textView : textViewList) {
                if (textView.getTag() == null) {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    textView.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
                }
            }
            holder.txt_time_slot.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            Intent intent = new Intent("TIME_SLOT");
            intent.putExtra("TIME_SLOT_POS",Utils.convertTimeToString(pos));
            localBroadcastManager.sendBroadcast(intent);
        });
    }

    @Override
    public int getItemCount() {
        return Utils.TIME_SLOT_TOTAL;
    }
    public class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_time_slot;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_time_slot = itemView.findViewById(R.id.time_slot);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAdapterPosition());
        }
    }

}

