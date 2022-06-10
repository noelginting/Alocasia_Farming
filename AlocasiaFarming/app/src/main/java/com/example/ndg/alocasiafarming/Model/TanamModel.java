package com.example.ndg.alocasiafarming.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TanamModel {
    @SerializedName("data")
    private List<Tanam> tanams=null;

    private Boolean error;

    public List<Tanam> getTanams() {
        return tanams;
    }

    public void setTanams(List<Tanam> tanams) {
        this.tanams = tanams;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
}
