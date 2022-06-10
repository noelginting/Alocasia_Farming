package com.example.ndg.alocasiafarming.Adapter;

import android.app.Activity;
import android.icu.text.DecimalFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ndg.alocasiafarming.Model.History;
import com.example.ndg.alocasiafarming.R;


import java.util.ArrayList;

public class TampilanHistoryAdapter extends ArrayAdapter<History> {
    public TampilanHistoryAdapter(Activity context, ArrayList<History> tampilanAdapter) {
        super(context, 0,tampilanAdapter);
    }

    @Override
    public View getView(int position, View convertView,  ViewGroup parent) {
        //cek posisi data dalam list
        History currentHistory =getItem(position);

        View listItemView = convertView;

        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item_history,parent,false);
        }
        ImageView imageTanaman = (ImageView) listItemView.findViewById(R.id.img_tanam);
        TextView textIdTanam = (TextView) listItemView.findViewById(R.id.text_idtanam);
        TextView textNama = (TextView) listItemView.findViewById(R.id.text_nama);
        TextView textTgl = (TextView) listItemView.findViewById(R.id.text_tgl);
        TextView textEc= (TextView) listItemView.findViewById(R.id.text_ec);
        TextView textTds= (TextView) listItemView.findViewById(R.id.text_tds);
        TextView textVolA= (TextView) listItemView.findViewById(R.id.text_vol_a);
        TextView textVolB= (TextView) listItemView.findViewById(R.id.text_volb);
        TextView textVolKel= (TextView) listItemView.findViewById(R.id.text_volkel);

        textIdTanam.setText(currentHistory.getId_tanam());
        textNama.setText(currentHistory.getNama_tumbuhan());
        int resId= currentHistory.getResId(currentHistory.getNama_gambar(),R.drawable.class);
        imageTanaman.setImageResource(resId);
        String tglWaktu= currentHistory.getTgl_pemupukan()+" "+currentHistory.getWaktu();
        textTgl.setText(tglWaktu);
        double semetaraEC= currentHistory.getNilai_ec()/1000;
        textEc.setText(String.format("%.1f",semetaraEC)+ " mS/cm");
        textTds.setText(String.valueOf(currentHistory.getNilai_tds())+ " PPM");
        textVolA.setText(String.valueOf(currentHistory.getVolume_pupuka())+ " ml");
        textVolB.setText(String.valueOf(currentHistory.getVolume_pupukb())+ " ml");
        textVolKel.setText(String.valueOf(currentHistory.getVolpupuk_keluar())+ " ml");

        return listItemView;
    }
}
