package com.sagarsweets.in.ApiModel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SelectableOption {

    @SerializedName("Size")
    private List<SizeModelProductDetails> size;

    public List<SizeModelProductDetails> getSize() {
        return size;
    }
}

