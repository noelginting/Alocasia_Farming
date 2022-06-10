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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ndg.alocasiafarming.Adapter.PenanamanAdapter;
import com.example.ndg.alocasiafarming.Adapter.TumbuhanAdapter;
import com.example.ndg.alocasiafarming.Model.Penanaman;
import com.example.ndg.alocasiafarming.Model.PenanamanModel;
import com.example.ndg.alocasiafarming.Model.Tumbuhan;
import com.example.ndg.alocasiafarming.Model.TumbuhanModel;
import com.example.ndg.alocasiafarming.R;
import com.example.ndg.alocasiafarming.Rest.ApiClient;
import com.example.ndg.alocasiafarming.Rest.JsonApi;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TumbuhanFragment extends Fragment implements TumbuhanAdapter.customButtonListener {
    private ListView listView;
    private LinearLayout linearBawah;
    private Button tambah;
    private Button simpan;
    private Button batal;
    private TextView keterangan;
    private EditText namaTumbuhan;
    private EditText namaGambar;
    private EditText hiddenId;
    private int aksi;
    private JsonApi mJsonApi;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tumbuhan_fragment,container,false);
        listView=(ListView) view.findViewById(R.id.listview1);
        linearBawah=(LinearLayout) view.findViewById(R.id.bawah);
        tambah=(Button) view.findViewById(R.id.btn_tambah);
        simpan=(Button) view.findViewById(R.id.btn_simpan);
        batal=(Button) view.findViewById(R.id.btn_batal);
        keterangan=(TextView) view.findViewById(R.id.keterangan);
        namaTumbuhan=(EditText) view.findViewById(R.id.nama_tumbuhan);
        namaGambar=(EditText)view.findViewById(R.id.gambar_tumbuhan);
        hiddenId=(EditText) view.findViewById(R.id.hidden_id);
        mJsonApi = ApiClient.getClient().create(JsonApi.class);
        getData();
        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBawah.setVisibility(LinearLayout.VISIBLE);
                tambah.setVisibility(Button.GONE);
                aksi=1;
                keterangan.setText("Tambah Data");
            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBawah.setVisibility(LinearLayout.GONE);
                tambah.setVisibility(Button.VISIBLE);
                namaTumbuhan.setText("");
                namaGambar.setText("");
                hiddenId.setText("");
            }
        });
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama_tumbuhan= namaTumbuhan.getText().toString();
                if(TextUtils.isEmpty(nama_tumbuhan)){
                    namaTumbuhan.setError("Tidak Boleh Kosong");
                    return;
                }
                String nama_gambar=namaGambar.getText().toString();
                if (TextUtils.isEmpty(nama_gambar)){
                    namaGambar.setError("Tidak Boleh Kosong");
                    return;
                }
                postTumbuhan(nama_tumbuhan,nama_gambar);
                getData();
                namaTumbuhan.setText("");
                namaGambar.setText("");
                hiddenId.setText("");
                linearBawah.setVisibility(LinearLayout.GONE);
                tambah.setVisibility(Button.VISIBLE);
            }
        });
        return view;
    }
    private void getData(){
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
                TumbuhanAdapter adapter = new TumbuhanAdapter(getActivity(),data);
                adapter.setCustomButtonListener(TumbuhanFragment.this);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<TumbuhanModel> call, Throwable t) {
                Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void postTumbuhan(String nama_tumbuhan, String nama_gambar){
        Integer id_tumbuhan= null;
        if(aksi==2){
        id_tumbuhan = Integer.parseInt(hiddenId.getText().toString());}
        Call<ResponseBody> call = mJsonApi.createPostTumbuhan(aksi,id_tumbuhan,nama_tumbuhan,nama_gambar);
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
    private void deletTumbuhan(int idtumbuhan){
        Call<ResponseBody> call = mJsonApi.deleteTumbuhan(aksi,idtumbuhan);
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
    public void onButtonClickListener(int positon, Tumbuhan value,int tmbl) {
        if(tmbl==1) {
            linearBawah.setVisibility(LinearLayout.VISIBLE);
            keterangan.setText("Update Data");
            tambah.setVisibility(Button.GONE);
            namaTumbuhan.setText(value.getNama_tumbuhan());
            namaGambar.setText(value.getNama_gambar());
            hiddenId.setText(String.valueOf(value.getId_tumbuhan()));
            aksi=2;
        }
        if(tmbl==2){
            aksi=3;
            deletTumbuhan(value.getId_tumbuhan());
        }
    }
}
