package com.ekspeace.barbershop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ekspeace.barbershop.Constants.Utils;
import com.ekspeace.barbershop.Interface.IRecyclerItemSelectedListener;
import com.ekspeace.barbershop.Model.Barber;
import com.ekspeace.barbershop.Model.HairCut;
import com.ekspeace.barbershop.R;

import java.util.ArrayList;
import java.util.List;

public class HairCutAdapter extends RecyclerView.Adapter<HairCutAdapter.MyViewHolder> {
    private Context context;
    private List<HairCut> hairCutList;
    private List<View> viewList;
    private LocalBroadcastManager localBroadcastManager;

    public HairCutAdapter(Context context, List<HairCut> hairCutList) {
        this.context = context;
        this.hairCutList = hairCutList;
        viewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public HairCutAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_recycler_hair_cut,parent, false);
        return new HairCutAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HairCutAdapter.MyViewHolder holder, int position) {
       HairCut hairCut = hairCutList.get(position);
       holder.hairCutName.setText(hairCut.getHairCutName());
       holder.hairCutImage.setImageBitmap(hairCut.getHairCutImage());

        if(!viewList.contains(holder.hairCutView))
            viewList.add(holder.hairCutView);
       holder.setRecyclerItemSelectedListener((view, pos) -> {
           for ( View v : viewList) {
               if (v.getTag() == null) {
                   v.setBackground(context.getResources().getDrawable(R.drawable.border_view));
               }
           }
           holder.hairCutView.setBackground(context.getResources().getDrawable(R.drawable.border_view_selected));
           Intent intent = new Intent("HAIRCUT_NAME");
           intent.putExtra("HAIRCUT_NAME_POS", hairCut.getHairCutName());
           localBroadcastManager.sendBroadcast(intent);
       });
    }

    @Override
    public int getItemCount() {
        return hairCutList.size();
    }
    public class MyViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView hairCutName;
        private ImageView hairCutImage;
        private View hairCutView;
        private IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            hairCutName = itemView.findViewById(R.id.haircut_name);
            hairCutImage = itemView.findViewById(R.id.haircut_image);
            hairCutView = itemView.findViewById(R.id.haircut_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemSelectedListener(v, getAbsoluteAdapterPosition());
        }
    }
}
