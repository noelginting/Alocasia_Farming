package com.example.ndg.alocasiafarming.Admin;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ndg.alocasiafarming.Adapter.PenanamanAdapter;
import com.example.ndg.alocasiafarming.MainActivity;
import com.example.ndg.alocasiafarming.Model.Penanaman;
import com.example.ndg.alocasiafarming.Model.PenanamanModel;
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

public class PenanamanFragment extends Fragment implements PenanamanAdapter.customButtonListener3 {
    private ListView listView;
    private LinearLayout linearBawah;
    private Button tambah;
    private Button simpan;
    private Button batal;
    private TextView keterangan;
    private JsonApi mJsonApi;
    private ArrayList<String> dataNamaTumbuhan = new ArrayList<String>();
    private ArrayList<String> dataIdTumbuhan= new ArrayList<>();
    private Spinner spinner;
    private EditText text_IdTanam;
    private EditText text_Tgl;
    private int aksi;
    private DatePickerDialog datePickerDialog;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.penanaman_fragment,container,false);
        listView= (ListView) view.findViewById(R.id.listview1);
        linearBawah=(LinearLayout) view.findViewById(R.id.bawah);
        tambah=(Button) view.findViewById(R.id.btn_tambah3);
        simpan=(Button) view.findViewById(R.id.btn_simpan3);
        batal=(Button) view.findViewById(R.id.btn_batal3);
        keterangan=(TextView) view.findViewById(R.id.keterangan3);
        spinner=(Spinner) view.findViewById(R.id.id_tanam_spiner);
        text_IdTanam=(EditText) view.findViewById(R.id.text_idtanam);
        text_Tgl=(EditText) view.findViewById(R.id.text_tgl);
        mJsonApi = ApiClient.getClient().create(JsonApi.class);
        getData();

        text_Tgl.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(getActivity(),R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text

                                text_Tgl.setText(year + "-"
                                        + (monthOfYear+1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBawah.setVisibility(LinearLayout.VISIBLE);
                tambah.setVisibility(Button.GONE);
                keterangan.setText("Tambah Data");
                getIdTumbuhan("");
                aksi =1;
            }
        });
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexx= spinner.getSelectedItemPosition();
                if(indexx==0){
                    ((TextView)spinner.getSelectedView()).setError("Pilih terlebih dahulu");
                }
                String idtanam= text_IdTanam.getText().toString();
                if(TextUtils.isEmpty(idtanam)){
                    text_IdTanam.setError("Tidak Boleh Kosong");
                    return;
                }
                String tanggal = text_Tgl.getText().toString();
                if (TextUtils.isEmpty(tanggal)){
                    text_Tgl.setError("Tidak Boleh Kosong");
                }
                linearBawah.setVisibility(LinearLayout.GONE);
                tambah.setVisibility(Button.VISIBLE);
                postTanaman(indexx,idtanam,tanggal);
                text_IdTanam.setText("");
                text_Tgl.setText("");
                spinner.setSelection(0);
            }
        });
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearBawah.setVisibility(LinearLayout.GONE);
                tambah.setVisibility(Button.VISIBLE);
                text_IdTanam.setText("");
                text_Tgl.setText("");
                spinner.setSelection(0);
            }
        });
        return view;
    }
    private void getData(){
        Call<PenanamanModel> call = mJsonApi.getDataPenanaman(3);
        call.enqueue(new Callback<PenanamanModel>() {
            @Override
            public void onResponse(Call<PenanamanModel> call, Response<PenanamanModel> response) {
                if(!response.isSuccessful()){
                    Log.e("getData","error");
                    return;
                }
                PenanamanModel penanamanModel=response.body();
                ArrayList<Penanaman> data = new ArrayList<>(penanamanModel.getPenanaman());
                PenanamanAdapter adapter = new PenanamanAdapter(getActivity(),data);
                adapter.setCustomButtonListner3(PenanamanFragment.this);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<PenanamanModel> call, Throwable t) {
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
    private void postTanaman(int idtumbuhan,String idtanam,String tanggal){
            Call<ResponseBody> call=mJsonApi.createPostPenanaman(aksi,idtanam,idtumbuhan,tanggal);
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
    private void deleteTanam(String idtanam){
        Call<ResponseBody> call = mJsonApi.deletePenanaman(aksi,idtanam);
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
    @Override
    public void onButtonClickListener(int position, Penanaman value, int tmbl) {
        if(tmbl==1) {
            linearBawah.setVisibility(LinearLayout.VISIBLE);
            keterangan.setText("Update Data");
            tambah.setVisibility(Button.GONE);
            text_IdTanam.setText(value.getId_tanam());
            text_Tgl.setText(value.getTgl_penanaman());
            text_IdTanam.setFocusable(false);
            getIdTumbuhan(value.getNama_tumbuhan());
            aksi =2;
        }
        if(tmbl==2){
            aksi=3;
            deleteTanam(value.getId_tanam());
        }
    }
}
