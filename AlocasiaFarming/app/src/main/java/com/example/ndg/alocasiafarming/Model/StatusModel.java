package com.example.ndg.alocasiafarming.Model;

public class StatusModel {
    private Boolean error;
    private String id_tanam;
    private String status;
    private String tgl_selesai;

    public String getTgl_selesai() {
        return tgl_selesai;
    }

    public Boolean getError() {
        return error;
    }

    public String getId_tanam() {
        return id_tanam;
    }

    public String getStatus() {
        return status;
    }
}
