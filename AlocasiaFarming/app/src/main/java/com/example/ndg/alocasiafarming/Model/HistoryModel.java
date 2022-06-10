package com.example.ndg.alocasiafarming.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryModel {
    @SerializedName("data")
    private List<History> histories=null;
    private Boolean error;

    public List<History> getHistories() {
        return histories;
    }

    public Boolean getError() {
        return error;
    }
}
