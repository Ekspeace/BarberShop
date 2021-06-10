package com.ekspeace.barbershop.Constants;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ekspeace.barbershop.Model.UserAppointment;
import com.ekspeace.barbershop.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class PopUp extends AppCompatActivity {

    static TextView tvTitle, tvDesc;
    static ImageView imIcon;
    static Button btnClose, btnConfirm;
    static LinearLayout linearLayout;
    static CardView cardView;
    private LocalBroadcastManager localBroadcastManager;


    public static void Toast(Context context, View layout, String message, int duration)
    {

        tvDesc = layout.findViewById(R.id.toast_message);
        linearLayout = layout.findViewById(R.id.custom_toast_container);

        tvDesc.setText(message);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 40);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

    public void InternetDialog(Context context) {
        Dialog alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.pop_up);
        tvTitle = alertDialog.findViewById(R.id.dialog_title);
        tvDesc = alertDialog.findViewById(R.id.dialog_desc);
        imIcon = alertDialog.findViewById(R.id.dialog_icon);
        cardView = alertDialog.findViewById(R.id.pop_up_cardView);
        btnClose = alertDialog.findViewById(R.id.dialog_close);
        btnConfirm = alertDialog.findViewById(R.id.dialog_confirm);
        localBroadcastManager = LocalBroadcastManager.getInstance(context);

        tvTitle.setText("Connection");
        tvDesc.setText("Please check your internet connectivity and try again");
        imIcon.setImageResource(R.drawable.wifi_off);

        btnClose.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.VISIBLE);
        btnConfirm.setText("Try Again");
        btnClose.setText("Close");

        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }

      btnConfirm.setOnClickListener(v -> {
          alertDialog.cancel();
          alertDialog.dismiss();
          Intent intent = new Intent(Utils.connection);
          localBroadcastManager.sendBroadcast(intent);

      });
        btnClose.setOnClickListener(view -> alertDialog.dismiss());


        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        if(!((Activity) context).isFinishing())
        {
            alertDialog.show();
        }


    }
    public static void DeleteDialog(Context context, String title, String message, List<UserAppointment> List, int Position, View layout) {
        Dialog alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.dialog);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(context);

        TextView tvTitle = alertDialog.findViewById(R.id.dialog_title);
        TextView tvDesc = alertDialog.findViewById(R.id.dialog_desc);
        TextView btnClose = alertDialog.findViewById(R.id.no_button);
        TextView btnConfirm = alertDialog.findViewById(R.id.yes_button);

        tvTitle.setText(title);
        tvDesc.setText(message);
        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }

        btnClose.setOnClickListener(view -> {
            alertDialog.dismiss();
        });
        btnConfirm.setOnClickListener(v -> {
            if (Utils.isOnline(context)) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("UserAppointment");
                query.whereEqualTo("userNumber", List.get(Position).getNumber());
                query.findInBackground((objects, e) -> {
                    if (e == null) {
                        objects.get(0).deleteInBackground(e1 -> {
                            if (e1 == null) {
                                Toast(context, layout, "Task deleted successfully", Toast.LENGTH_SHORT);
                                alertDialog.dismiss();
                                Intent intent = new Intent("RESTART");
                                localBroadcastManager.sendBroadcast(intent);
                            } else {
                                Toast(context, layout, "Error: ".concat(e.getMessage()), Toast.LENGTH_LONG);
                            }
                        });
                    } else {
                        Toast(context, layout, "Error: ".concat(e.getMessage()), Toast.LENGTH_LONG);
                    }
                });
            }else {
                alertDialog.dismiss();
                PopUp popUp = new PopUp();
                popUp.InternetDialog(context);
            }
        });

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }
    public static void AboutDialog(Context context){
        Dialog alertDialog = new Dialog(context);
        alertDialog.setContentView(R.layout.pop_up);
        tvTitle = alertDialog.findViewById(R.id.dialog_title);
        tvDesc = alertDialog.findViewById(R.id.dialog_desc);
        imIcon = alertDialog.findViewById(R.id.dialog_icon);
        cardView = alertDialog.findViewById(R.id.pop_up_cardView);
        btnClose = alertDialog.findViewById(R.id.dialog_about_close);

        btnClose.setVisibility(View.VISIBLE);
        tvTitle.setText("About Us");
        tvDesc.setText("Barber Shop");
        imIcon.setImageResource(R.drawable.info);
        btnClose.setText("Close");

        if (alertDialog.isShowing()) {
            alertDialog.cancel();
        }

        btnClose.setOnClickListener(view -> alertDialog.dismiss());

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        if(!((Activity) context).isFinishing())
        {
            alertDialog.show();
        }
    }
}
