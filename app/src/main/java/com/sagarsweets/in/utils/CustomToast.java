package com.sagarsweets.in.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.sagarsweets.in.R;

public class CustomToast {

    public static final int SUCCESS = 1;
    public static final int ERROR = 2;
    public static final int WARNING = 3;

    public static void show(
            Context context,
            String message,
            int type
    ) {

        LayoutInflater inflater =
                LayoutInflater.from(context);

        View view =
                inflater.inflate(
                        R.layout.layout_custom_toast,
                        null
                );

        CardView card =
                view.findViewById(
                        R.id.toastCard
                );

        TextView txt =
                view.findViewById(
                        R.id.txtToast
                );

        txt.setText(message);

        switch (type){

            case SUCCESS:
                card.setCardBackgroundColor(0xFF10B981);
                break;

            case ERROR:
                card.setCardBackgroundColor(0xFFF43F5E);
                break;

            case WARNING:
                card.setCardBackgroundColor(0xFFF59E0B);
                txt.setTextColor(0xFF000000); // black text
                break;
        }
        card.setRadius(24f);
        card.setCardElevation(12f);
        Toast toast =
                new Toast(context);

        toast.setView(view);

        toast.setGravity(
                Gravity.BOTTOM,
                0,
                180
        );

        toast.setDuration(
                Toast.LENGTH_LONG
        ); // auto hide

        toast.show();
    }

    public static void success(
            Context context,
            String message
    ){
        show(
                context,
                message,
                SUCCESS
        );
    }

    public static void error(
            Context context,
            String message
    ){
        show(
                context,
                message,
                ERROR
        );
    }

    public static void warning(
            Context context,
            String message
    ){
        show(
                context,
                message,
                WARNING
        );
    }

}