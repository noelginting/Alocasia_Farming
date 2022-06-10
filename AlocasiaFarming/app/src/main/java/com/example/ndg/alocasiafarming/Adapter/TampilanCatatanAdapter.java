package com.example.ndg.alocasiafarming.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ndg.alocasiafarming.Model.Catatan;
import com.example.ndg.alocasiafarming.R;

import java.util.ArrayList;

public class TampilanCatatanAdapter extends ArrayAdapter<Catatan> {
    public TampilanCatatanAdapter(Activity context, ArrayList<Catatan> catatanAdapter){
        super(context,0,catatanAdapter);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        Catatan currentCatatan=getItem(position);
        View listItemView = convertView;

        if(listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item_catatan,parent,false);
        }
        TextView textIdTanam=(TextView)listItemView.findViewById(R.id.idtanam);
        TextView textTglTanam=(TextView)listItemView.findViewById(R.id.tgltanam);
        TextView textCatatan=(TextView)listItemView.findViewById(R.id.text_catatan);
        TextView textTindakan=(TextView)listItemView.findViewById(R.id.text_tindakan);

        textIdTanam.setText(currentCatatan.getId_tanam());
        textTglTanam.setText(currentCatatan.getTanggal());
        textCatatan.setText(currentCatatan.getCatatan());
        textTindakan.setText(currentCatatan.getTindakan());
        return listItemView;
    }
}
