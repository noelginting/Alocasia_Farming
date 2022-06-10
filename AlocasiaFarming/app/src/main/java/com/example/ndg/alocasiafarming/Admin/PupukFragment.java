package com.example.ndg.alocasiafarming.Admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ndg.alocasiafarming.Adapter.PupukAdapter;
import com.example.ndg.alocasiafarming.Model.Pupuk;
import com.example.ndg.alocasiafarming.Model.PupukModel;
import com.example.ndg.alocasiafarming.Model.Tumbuhan;
import com.example.ndg.alocasiafarming.Model.TumbuhanModel;
import com.example.ndg.alocasiafarming.R;
import com.example.ndg.alocasiafarming.Rest.ApiClient;
import com.example.ndg.alocasiafarming.Rest.JsonApi;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PupukFragment extends Fragment implements PupukAdapter.customButtonListener2{
    private ListView listView;
    private LinearLayout linearBawah;
    private Button tambah;
    private Button simpan;
    private Button batal;
    private TextView keterangan;
    private JsonApi mJsonApi;
    private ArrayList<String> dataNamaTumbuhan = new ArrayList<String>();
    private ArrayList<String> dataIdTumbuhan = new ArrayList<String>();
    private Spinner spinner;
    private EditText textUmur;
    private EditText textEc;
    private EditText pupukHiden;
    private int aksi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pupuk_fragment,container,false);
        listView=(ListView) view.findViewById(R.id.listview1);
        linearBawah=(LinearLayout) view.findViewById(R.id.bawah);
        tambah=(Button) view.findViewById(R.id.btn_tambah2);
        simpan=(Button) view.findViewById(R.id.btn_simpan2);
        batal=(Button) view.findViewById(R.id.btn_batal2);
        keterangan=(TextView) view.findViewById(R.id.keterangan2);
        spinner=(Spinner) view.findViewById(R.id.id_tanam_spiner);
        textUmur=(EditText) view.findViewById(R.id.text_umur);
        textEc=(EditText) view.findViewById(R.id.text_ec);
        pupukHiden=(EditText) view.findViewById(R.id.pupuk_hidden);
        getData();
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBawah.setVisibility(LinearLayout.VISIBLE);
                tambah.setVisibility(Button.GONE);
                keterangan.setText("Tambah Data");
                aksi=1;
                getIdTumbuhan("");
            }
        });
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexx = spinner.getSelectedItemPosition();
                if (indexx==0){
                    ((TextView)spinner.getSelectedView()).setError("Pilih telebih dahulu");
                    return;
                }
                String  umur = textUmur.getText().toString();
                if(TextUtils.isEmpty(umur)){
                    textUmur.setError("Tidak Boleh Kosong");
                    return;
                }
                String ec = textEc.getText().toString();
                if(TextUtils.isEmpty(ec)){
                    textEc.setError("Tidak Boleh Kosong");
                    return;
                }
                postData(indexx,umur,ec);
                textUmur.setText("");
                textEc.setText("");
                pupukHiden.setText("");
                spinner.setSelection(0);
                linearBawah.setVisibility(LinearLayout.GONE);
                tambah.setVisibility(Button.VISIBLE);
            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBawah.setVisibility(LinearLayout.GONE);
                tambah.setVisibility(Button.VISIBLE);
                textUmur.setText("");
                textEc.setText("");
                pupukHiden.setText("");
                spinner.setSelection(0);
            }
        });
        return view;
    }
    private void getData(){
        mJsonApi= ApiClient.getClient().create(JsonApi.class);
        Call<PupukModel> call= mJsonApi.getDataPupuk(2);
        call.enqueue(new Callback<PupukModel>() {
            @Override
            public void onResponse(Call<PupukModel> call, Response<PupukModel> response) {
                if(!response.isSuccessful()){
                    Log.e("getData","error");
                    return;
                }
                PupukModel pupukModel= response.body();
                ArrayList<Pupuk> data = new ArrayList<>(pupukModel.getPupuk());
                PupukAdapter adapter = new PupukAdapter(getActivity(),data);
                adapter.setCustomButtonListener2(PupukFragment.this);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PupukModel> call, Throwable t) {
                Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getIdTumbuhan(final String nama){
        Call<TumbuhanModel> call = mJsonApi.getDataTumbuhan(1);
        call.enqueue(new Callback<TumbuhanModel>() {
            @Override
            public void onResponse(Call<TumbuhanModel> call, Response<TumbuhanModel> response) {
                if(!response.isSuccessful()){
                    Log.e("getData","error");
                    return;
                }
                TumbuhanModel tumbuhanModel=response.body();
                ArrayList<Tumbuhan> data = new ArrayList<>(tumbuhanModel.getTumbuhan());
                dataNamaTumbuhan.clear();
                dataIdTumbuhan.clear();
                dataNamaTumbuhan.add("Nama Tumbuhan");
                dataIdTumbuhan.add("Id");
                for (Tumbuhan tumbuhan:data){
                    dataNamaTumbuhan.add(tumbuhan.getNama_tumbuhan());
                    dataIdTumbuhan.add(String.valueOf(tumbuhan.getId_tumbuhan()));
                }
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dataNamaTumbuhan);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner.setBackgroundResource(R.drawable.spiner_border);
                spinner.setAdapter(dataAdapter);
                if (!nama.equals("")){
                    spinner.setSelection(dataNamaTumbuhan.indexOf(nama));
                }
            }

            @Override
            public void onFailure(Call<TumbuhanModel> call, Throwable t) {
                Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void postData(int index, String umur, String  ec){
        Integer idpupuk ;
        idpupuk=null;
            int idtumbuhan= Integer.parseInt(dataIdTumbuhan.get(index));
            int nilai_umur = Integer.parseInt(umur);
            int nilai_ec = Integer.parseInt(ec);
            if(aksi==2){
                idpupuk= Integer.parseInt(pupukHiden.getText().toString());
                Log.i("coba",""+idpupuk);
            }
            Call<ResponseBody> call = mJsonApi.createPostPupuk(aksi,idpupuk,nilai_umur,idtumbuhan,nilai_ec);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(getContext(),"Berhasil",Toast.LENGTH_SHORT).show();
                    getData();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("getData","error");
                }
            });

    }
    private void deletePupuk(int id_pupuk){
        Call<ResponseBody> call= mJsonApi.deletePupuk(aksi,id_pupuk);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getContext(),"Berhasil dihapus",Toast.LENGTH_SHORT).show();
                getData();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("deleteTumbuhan","error");
            }
        });
    }

    @Override
    public void onButtonClickListener(int position, Pupuk value, int tmbl) {
        if(tmbl==1) {
            linearBawah.setVisibility(LinearLayout.VISIBLE);
            keterangan.setText("Update Data");
            tambah.setVisibility(Button.GONE);
            getIdTumbuhan(value.getNama_tumbuhan());
            textEc.setText(String.valueOf(value.getNilai_ec()));
            textUmur.setText(String.valueOf(value.getUmur_tumbuhan()));
            pupukHiden.setText(String.valueOf(value.getId_pupuk()));
            aksi=2;
        }
        if(tmbl==2){
            aksi=3;
            Log.i("coba",""+value.getId_pupuk());
            deletePupuk(value.getId_pupuk());
        }
    }
}
