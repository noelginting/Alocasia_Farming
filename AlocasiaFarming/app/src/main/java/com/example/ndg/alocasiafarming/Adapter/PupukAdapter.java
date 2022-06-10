package com.example.ndg.alocasiafarming.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ndg.alocasiafarming.Model.Pupuk;
import com.example.ndg.alocasiafarming.R;

import java.util.ArrayList;

public class PupukAdapter extends ArrayAdapter<Pupuk> {
    customButtonListener2 costomListener2;

    public interface customButtonListener2{
        public void onButtonClickListener(int position, Pupuk value, int tmbl);
    }

    public void setCustomButtonListener2(customButtonListener2 listener2){
        this.costomListener2=listener2;
    }

    private static class ViewHolder {
        TextView text1;
        TextView text2;
        TextView text3;
        TextView text4;
        ImageView image1;
        ImageView image2;

    }

    public PupukAdapter(Activity context, ArrayList<Pupuk> pupuk) {
        super(context, 0, pupuk);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Pupuk pupukWord = getItem(position);

        ViewHolder listItemView;
        final View result;
        if (convertView == null) {
            listItemView = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_admin2, parent, false);
            listItemView.text1 = (TextView) convertView.findViewById(R.id.text1);
            listItemView.text2 = (TextView) convertView.findViewById(R.id.text2);
            listItemView.text3 = (TextView) convertView.findViewById(R.id.text3);
            listItemView.text4 = (TextView) convertView.findViewById(R.id.text4);
            listItemView.image1 = (ImageView) convertView.findViewById(R.id.update2);
            listItemView.image2 = (ImageView) convertView.findViewById(R.id.delete2);
            result = convertView;
            convertView.setTag(listItemView);
        } else {
            listItemView = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        listItemView.text1.setText(String.valueOf(pupukWord.getId_pupuk()));
        listItemView.text2.setText(pupukWord.getNama_tumbuhan());
        listItemView.text3.setText(String.valueOf(pupukWord.getUmur_tumbuhan()));
        listItemView.text4.setText(String.valueOf(pupukWord.getNilai_ec()));

        final Pupuk temp = getItem(position);
        listItemView.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (costomListener2!=null){
                    costomListener2.onButtonClickListener(position,temp,1);
                }
            }
        });
        listItemView.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (costomListener2!=null){
                    costomListener2.onButtonClickListener(position,temp,2);
                }
            }
        });

        return convertView;
    }
}
