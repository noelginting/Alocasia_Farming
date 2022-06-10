package com.example.ndg.alocasiafarming.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatatanModel
{
    @SerializedName("data")
    private List<Catatan> catatans=null;
    private Boolean error;

    public List<Catatan> getCatatans() {
        return catatans;
    }

    public Boolean getError() {
        return error;
    }
}
