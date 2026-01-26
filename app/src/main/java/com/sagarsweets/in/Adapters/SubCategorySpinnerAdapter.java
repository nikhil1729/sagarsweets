package com.sagarsweets.in.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sagarsweets.in.ApiModel.CategoryModel;

import java.util.List;

public class SubCategorySpinnerAdapter extends ArrayAdapter<CategoryModel> {

    public SubCategorySpinnerAdapter(Context context, List<CategoryModel> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        CategoryModel model = getItem(position);
        textView.setText(model.getName());

        return convertView;
    }
}

