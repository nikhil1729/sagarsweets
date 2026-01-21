package com.sagarsweets.in.ApiModel;

import java.util.List;

public class ReviewResponse {
    private boolean status;
    private List<ReviewModel> data;
    private String message;

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public List<ReviewModel> getData() {
        return data;
    }
}

