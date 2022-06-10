package com.example.ndg.alocasiafarming.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PupukModel {
    @SerializedName("data")
    private List<Pupuk> pupuk=null;
    private Boolean error;

    public List<Pupuk> getPupuk() {
        return pupuk;
    }

    public Boolean getError() {
        return error;
    }
}
