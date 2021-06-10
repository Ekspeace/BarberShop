package com.ekspeace.barbershop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.barbershop.Interface.IRecyclerItemSelectedListener;
import com.ekspeace.barbershop.Model.Barber;
import com.ekspeace.barbershop.R;

import java.util.ArrayList;
import java.util.List;

public class BarberViewAdapter extends RecyclerView.Adapter<BarberViewAdapter.MyViewHolder> {
    private Context context;
    private List<Barber> barberList;
    private List<View> viewList;
    private LocalBroadcastManager localBroadcastManager;

    public BarberViewAdapter(Context context, List<Barber> barberList){
        this.context =context;
        this.barberList = barberList;
        viewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public BarberViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_recycleview_barbers,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BarberViewAdapter.MyViewHolder holder, int position) {
        Barber barber = barberList.get(position);
       holder.barberName.setText(barber.getName());
       holder.barberDescription.setText(barber.getDescription());
       holder.barberImage.setImageBitmap(barber.getImage());
       holder.barberRating.setRating(barber.getRating());

        if(!viewList.contains(holder.barberView))
            viewList.add(holder.barberView);
       holder.setRecyclerItemSelectedListener((view, pos) -> {
           for ( View v : viewList) {
               if (v.getTag() == null) {
                   v.setBackground(context.getResources().getDrawable(R.drawable.border_view));
               }
           }
           holder.barberView.setBackground(context.getResources().getDrawable(R.drawable.border_view_selected));
           Intent intent = new Intent("BARBER_NAME");
           intent.putExtra("BARBER_NAME_POS", barber.getName());
           localBroadcastManager.sendBroadcast(intent);
       });
    }

    @Override
    public int getItemCount() {
        return barberList.size();
    }
    public class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView barberName, barberDescription;
        private ImageView barberImage;
        private RatingBar barberRating;
        private View barberView;
        private IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            barberName = itemView.findViewById(R.id.barber_name);
            barberDescription = itemView.findViewById(R.id.barber_description);
            barberImage = itemView.findViewById(R.id.barber_image);
            barberView = itemView.findViewById(R.id.barber_view);
            barberRating = itemView.findViewById(R.id.rating_bar);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAbsoluteAdapterPosition());
        }
    }
}
