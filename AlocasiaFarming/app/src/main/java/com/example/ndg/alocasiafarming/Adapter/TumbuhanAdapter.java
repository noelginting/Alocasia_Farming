package com.example.ndg.alocasiafarming.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ndg.alocasiafarming.Model.Tumbuhan;
import com.example.ndg.alocasiafarming.R;

import java.util.ArrayList;

public class TumbuhanAdapter extends ArrayAdapter<Tumbuhan>  {
    customButtonListener customButtonListener;

    public interface customButtonListener{
        public void onButtonClickListener(int positon,Tumbuhan value,int tmbl);
    }
    public void setCustomButtonListener(customButtonListener listener){
        this.customButtonListener= listener;
    }
    private LinearLayout linearBawah;
    private Button tambah;
    private static class ViewHolder{
        TextView text1;
        TextView text2;
        TextView text3;
        ImageView image1;
        ImageView image2;
    }
    public TumbuhanAdapter(Activity context, ArrayList<Tumbuhan> tumbuhans){
        super(context,0,tumbuhans);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       Tumbuhan tumbuhanWord = getItem(position);

        ViewHolder listItemView ;
        final View result;

        if(convertView==null){
            listItemView= new ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView= inflater.inflate(R.layout.row_admin1,parent,false);

           listItemView.text1= (TextView) convertView.findViewById(R.id.text1);
            listItemView.text2= (TextView) convertView.findViewById(R.id.text2);
            listItemView.text3= (TextView) convertView.findViewById(R.id.text3);
            listItemView.image1 = (ImageView) convertView.findViewById(R.id.update1);
            listItemView.image2 = (ImageView) convertView.findViewById(R.id.delete1);
            result=convertView;
            convertView.setTag(listItemView);
        } else {
            listItemView= (ViewHolder) convertView.getTag();
            result=convertView;
        }

        listItemView.text1.setText(String.valueOf(tumbuhanWord.getId_tumbuhan()));
        listItemView.text2.setText(tumbuhanWord.getNama_tumbuhan());
        listItemView.text3.setText(tumbuhanWord.getNama_gambar());
        final Tumbuhan temp = getItem(position);
        listItemView.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customButtonListener!=null){
                    customButtonListener.onButtonClickListener(position,temp,1);
                }
            }
        });
        listItemView.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customButtonListener!=null){
                    customButtonListener.onButtonClickListener(position,temp,2);
                }
            }
        });
        return convertView;
    }
}
