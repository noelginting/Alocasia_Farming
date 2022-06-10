package com.example.ndg.alocasiafarming.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PenanamanModel {
    @SerializedName("data")
    private List<Penanaman> penanaman=null;
    private Boolean error;

    public List<Penanaman> getPenanaman() {
        return penanaman;
    }

    public Boolean getError() {
        return error;
    }
}
