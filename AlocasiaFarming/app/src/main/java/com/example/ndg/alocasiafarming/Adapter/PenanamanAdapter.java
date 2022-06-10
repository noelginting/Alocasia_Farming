package com.example.ndg.alocasiafarming.Adapter;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ndg.alocasiafarming.Model.Penanaman;
import com.example.ndg.alocasiafarming.R;

import java.util.ArrayList;

public class PenanamanAdapter extends ArrayAdapter<Penanaman> {
    customButtonListener3 customListener3;

    public interface customButtonListener3{
        public void onButtonClickListener(int position, Penanaman value, int tmbl);
    }
    public void setCustomButtonListner3(customButtonListener3 listener3){
        this.customListener3=listener3;
    }
    private static class ViewHolder{
        TextView text1;
        TextView text2;
        TextView text3;
        ImageView image1;
        ImageView image2;
    }
    public PenanamanAdapter(Activity context, ArrayList<Penanaman> penanaman){
        super(context,0,penanaman);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Penanaman penanamanWord = getItem(position);

        ViewHolder listItemView ;
        final View result;

        if(convertView==null){
            listItemView= new ViewHolder();
            LayoutInflater inflater=LayoutInflater.from(getContext());
            convertView= inflater.inflate(R.layout.row_admin3,parent,false);

            listItemView.text1= (TextView) convertView.findViewById(R.id.text1);
            listItemView.text2= (TextView) convertView.findViewById(R.id.text2);
            listItemView.text3= (TextView) convertView.findViewById(R.id.text3);
            listItemView.image1 = (ImageView) convertView.findViewById(R.id.update3);
            listItemView.image2 = (ImageView) convertView.findViewById(R.id.delete3);
            result=convertView;
            convertView.setTag(listItemView);
        } else {
            listItemView= (ViewHolder) convertView.getTag();
            result=convertView;
        }

        listItemView.text1.setText(String.valueOf(penanamanWord.getId_tumbuhan()));
        listItemView.text2.setText(penanamanWord.getId_tanam());
        listItemView.text3.setText(penanamanWord.getTgl_penanaman());
        final Penanaman temp = getItem(position);
        listItemView.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customListener3!=null){
                    customListener3.onButtonClickListener(position,temp,1);
                }
            }
        });
        listItemView.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customListener3!=null){
                    customListener3.onButtonClickListener(position,temp,2);
                }
            }
        });
        return convertView;
    }
}
