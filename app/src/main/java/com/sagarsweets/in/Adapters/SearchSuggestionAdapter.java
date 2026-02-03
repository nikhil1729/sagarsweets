package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sagarsweets.in.R;

import java.util.List;

public class SearchSuggestionAdapter extends ArrayAdapter<String> {

    public SearchSuggestionAdapter(@NonNull Context context, List<String> data) {
        super(context, 0, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_search_suggestion, parent, false);
        }

        TextView text = convertView.findViewById(R.id.txtSuggestion);
        Log.d("responseNikhil", "sasasa- "+getItem(position));
        text.setText(getItem(position));

        return convertView;
    }
}
