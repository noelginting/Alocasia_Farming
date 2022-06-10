package com.example.ndg.alocasiafarming.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TumbuhanModel {
    @SerializedName("data")
    private List<Tumbuhan> tumbuhan=null;
    private Boolean error;

    public List<Tumbuhan> getTumbuhan() {
        return tumbuhan;
    }

    public Boolean getError() {
        return error;
    }
}

