package com.example.ndg.alocasiafarming.Model;

import java.lang.reflect.Field;

public class History {
    private String id_tanam;
    private String tgl_pemupukan;
    private String waktu_pemupukan;
    private int volume_pupuka;
    private int volume_pupukb;
    private int nilai_ec;
    private int nilai_tds;
    private int volpupuk_keluar;
    private String nama_gambar;
    private String nama_tumbuhan;
    private int batas_ec;

    public int getBatas_ec() {
        return batas_ec;
    }

    public String getId_tanam() {
        return id_tanam;
    }

    public String getTgl_pemupukan() {
        return tgl_pemupukan;
    }

    public int getVolume_pupuka() {
        return volume_pupuka;
    }

    public String getWaktu() {
        return waktu_pemupukan;
    }

    public int getVolume_pupukb() {
        return volume_pupukb;
    }

    public int getNilai_ec() {
        return nilai_ec;
    }

    public int getNilai_tds() {
        return nilai_tds;
    }
    public int getVolpupuk_keluar() {
        return volpupuk_keluar;
    }

    public String getNama_gambar() {
        return nama_gambar;
    }

    public String getNama_tumbuhan() {
        return nama_tumbuhan;
    }
    public static int getResId(String nama_gambar,Class<?> c){
        try{
            Field idField= c.getDeclaredField(nama_gambar);
            return idField.getInt(idField);
        } catch (Exception e){
            e.printStackTrace();
            return -1;
        }
    }
}
