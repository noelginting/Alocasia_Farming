package com.example.ndg.alocasiafarming;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.ndg.alocasiafarming.Adapter.TampilanHistoryAdapter;
import com.example.ndg.alocasiafarming.Model.History;
import com.example.ndg.alocasiafarming.Model.HistoryModel;
import com.example.ndg.alocasiafarming.Model.StatusModel;
import com.example.ndg.alocasiafarming.Model.Tanam;
import com.example.ndg.alocasiafarming.Model.TanamModel;
import com.example.ndg.alocasiafarming.Rest.ApiClient;
import com.example.ndg.alocasiafarming.Rest.JsonApi;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private JsonApi mJsonApi;
    private Spinner spinnerTanam;
    private Switch btnSwitch;
    private ArrayList<String> dataIdTanam = new ArrayList<>();
    private ArrayList<Integer> dataNilaiChart = new ArrayList<>();
    private ArrayList<String> nilaiX = new ArrayList<>();
    private ArrayList<Integer> nilaiY = new ArrayList<>();
    private ArrayList<Integer> batas = new ArrayList<>();
    private ArrayList<Integer> nilaiEc = new ArrayList<>();
    private ArrayList<String> dataWaktuChart= new ArrayList<>();
    private Boolean kirimmqtt=false;
    private ImageView icKiri;
    private ImageView icKanan;
    private ImageView icBack;
    private Button btn_Simpan;
    private Button btn_Catatan;
    private Button btn_SimpanCttn;
    private Button btn_Batal;
    private ViewFlipper viewFlipper;
    private ImageView ofsetKiri;
    private ImageView ofsetKanan;
    private ListView listView;
    private LinearLayout formBtn;
    private LinearLayout catatan;
    private ColumnChartView columnChartView;
    private ColumnChartView columnChartView1;
    private EditText text_Catatan;
    private EditText text_Penanganan;
    private ColumnChartData data;
    private int id = 0;
    private int offset=0;
    private String tgl;
    private String jam;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.home_fragment, container, false);

        mJsonApi = ApiClient.getClient().create(JsonApi.class);
        spinnerTanam = view.findViewById(R.id.id_tanam_spiner);
        btnSwitch = view.findViewById(R.id.simpleSwitch);
        columnChartView=view.findViewById(R.id.chart);
        columnChartView1=view.findViewById(R.id.chart2);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewfliper);
        icKiri= (ImageView) view.findViewById(R.id.ic_kiri);
        icKanan= (ImageView) view.findViewById(R.id.ic_kanan);
        icBack=(ImageView) view.findViewById(R.id.ic_kiri_id);
        ofsetKiri=(ImageView) view.findViewById(R.id.ic_kirioffset);
        ofsetKanan=(ImageView) view.findViewById(R.id.ic_kananoffset);
        listView= (ListView) view.findViewById(R.id.list_view);
        btn_Catatan=(Button) view.findViewById(R.id.btn_catatan);
        btn_Simpan=(Button) view.findViewById(R.id.btn_selesai);
        btn_SimpanCttn=(Button) view.findViewById(R.id.selesaicttn);
        btn_Batal=(Button) view.findViewById(R.id.btn_batal);
        formBtn=(LinearLayout) view.findViewById(R.id.formbtn);
        catatan=(LinearLayout) view.findViewById(R.id.catatan);
        text_Penanganan=(EditText) view.findViewById(R.id.text_penanganan);
        text_Catatan=(EditText) view.findViewById(R.id.text_catatan);
        getNilaiSpiner();
        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int indexx = spinnerTanam.getSelectedItemPosition();
                if(indexx==0){
                    ((TextView)spinnerTanam.getSelectedView()).setError("Pilih telebih dahulu");
                    return;
                }
                if (isChecked) {
                        kirimMqtt(spinnerTanam.getSelectedItem().toString(), "on");
                        getDataNow(spinnerTanam.getSelectedItem().toString());
                        getDataChart(1,spinnerTanam.getSelectedItem().toString());
                        getDataChart(2,spinnerTanam.getSelectedItem().toString());
                        Toast.makeText(getActivity(), "On " + spinnerTanam.getSelectedItem(), Toast.LENGTH_SHORT).show();

                } else {
                    kirimMqtt(spinnerTanam.getSelectedItem().toString(),"off");
                    Toast.makeText(getActivity(), "Off", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_Catatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexx = spinnerTanam.getSelectedItemPosition();
                if(indexx==0){
                    ((TextView)spinnerTanam.getSelectedView()).setError("Pilih telebih dahulu");
                    return;
                }
                formBtn.setVisibility(LinearLayout.GONE);
                catatan.setVisibility(LinearLayout.VISIBLE);
            }
        });
        btn_Batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text_Catatan.setText("");
                text_Penanganan.setText("");
                formBtn.setVisibility(LinearLayout.VISIBLE);
                catatan.setVisibility(LinearLayout.GONE);
            }
        });
        btn_SimpanCttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idtanam= spinnerTanam.getSelectedItem().toString();
                String textcttn= text_Catatan.getText().toString();
                if(TextUtils.isEmpty(textcttn)){
                    text_Catatan.setError("Tidak Boleh Kosong");
                    return;
                }
                String textpenanganan=text_Penanganan.getText().toString();
                if(textpenanganan.isEmpty()){
                    textpenanganan=null;
                }
                postCatatan(idtanam,textcttn,textpenanganan);
                text_Catatan.setText("");
                text_Penanganan.setText("");
                formBtn.setVisibility(LinearLayout.VISIBLE);
                catatan.setVisibility(LinearLayout.GONE);
            }
        });
        btn_Simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int indexx = spinnerTanam.getSelectedItemPosition();
                if(indexx==0){
                    ((TextView)spinnerTanam.getSelectedView()).setError("Pilih telebih dahulu");
                    return;
                }
                postSelesai();
                btnSwitch.setChecked(false);
                getNilaiSpiner();
                spinnerTanam.setSelection(0);
                getDataNow(spinnerTanam.getSelectedItem().toString());
                getDataChart(1,spinnerTanam.getSelectedItem().toString());
                getDataChart(2,spinnerTanam.getSelectedItem().toString());
            }
        });

        icKiri.setOnClickListener(this);
        icKanan.setOnClickListener(this);
        columnChartView.setOnValueTouchListener(new ValueTouchListener());
        columnChartView1.setOnValueTouchListener(new ValueTouchListener());
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=id-1;
                offset=0;
                if(id<1){
                    icBack.setVisibility(View.GONE);
                }
                if(id!=2){
                    ofsetKanan.setVisibility(View.VISIBLE);
                    ofsetKiri.setVisibility(View.VISIBLE);
                }
                Log.i("idkakacoba",""+id);
                getDataChart(1,spinnerTanam.getSelectedItem().toString());
                getDataChart(2,spinnerTanam.getSelectedItem().toString());
            }
        });
        ofsetKanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    offset=offset-1;
                    if(offset<=0){
                        offset=0;
                    }
                Log.i("ofsetkaka",""+offset);
                getDataChart(1,spinnerTanam.getSelectedItem().toString());
                getDataChart(2,spinnerTanam.getSelectedItem().toString());
            }
        });
        ofsetKiri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offset=offset+1;
                if(id==1){
                    if (offset>=4){
                        offset=4;
                    }
                }
                Log.i("ofsetkaka",""+offset);
                getDataChart(1,spinnerTanam.getSelectedItem().toString());
                getDataChart(2,spinnerTanam.getSelectedItem().toString());
            }
        });
        return view;

    }
    private void getNilaiSpiner() {
        Call<TanamModel> call = mJsonApi.getPenanaman(1);
        call.enqueue(new Callback<TanamModel>() {
            @Override
            public void onResponse(Call<TanamModel> call, Response<TanamModel> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Coba cek kembali koneksi internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                TanamModel tanamModels = response.body();
                if (!tanamModels.getError()) {
                    ArrayList<Tanam> data = new ArrayList<>(tanamModels.getTanams());
                    dataIdTanam.clear();
                    dataIdTanam.add("Pilih Id Tanam");
                    for (Tanam tanam : data) {
                        dataIdTanam.add(tanam.getId_tanam());
                    }
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dataIdTanam);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    spinnerTanam.setBackgroundResource(R.drawable.spiner_border);
                    spinnerTanam.setAdapter(dataAdapter);
                    getStatus(dataIdTanam);
                }
            }

            @Override
            public void onFailure(Call<TanamModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Coba cek kembali koneksi internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void kirimMqtt(String nilai, String status) {
        Call<ResponseBody> call = mJsonApi.createPost(nilai, status);
        Log.v("status",""+nilai + status);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                kirimmqtt = true;
                Log.v("Kirim Mqtt ", "" + response.code());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                kirimmqtt = false;
                Log.v("Kirim Mqtt ", "" + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==icKanan){
            viewFlipper.showNext();
        }
        else if (v==icKiri){
            viewFlipper.showPrevious();
        }
    }

    private void getStatus(final ArrayList dataIdTanam){
        Call<StatusModel> call = mJsonApi.getStatus(1);
        call.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {
                if(!response.isSuccessful()){
                    Log.e("getStatus","error");
                    return;
                }
                StatusModel statusModel = response.body();
                if(!statusModel.getError()){
                    if (statusModel.getStatus().equals("on")){
                        btnSwitch.setChecked(true);
                        } else if(statusModel.getStatus().equals("off")){
                        btnSwitch.setChecked(false);
                    }
                    Log.i("status",""+statusModel.getStatus());
                    Log.i("status",""+dataIdTanam.indexOf(statusModel.getId_tanam()));
                    if(statusModel.getId_tanam().equals("idtanam")){
                        spinnerTanam.setSelection(0);
                        btnSwitch.setChecked(false);
                        Toast.makeText(getContext(),"Alat belum menyala",Toast.LENGTH_SHORT).show();
                    } else{
                    spinnerTanam.setSelection(dataIdTanam.indexOf(statusModel.getId_tanam()));
                    getDataNow(spinnerTanam.getSelectedItem().toString());
                    getDataChart(1,spinnerTanam.getSelectedItem().toString());
                    getDataChart(2,spinnerTanam.getSelectedItem().toString());}

                }

            }
            @Override
            public void onFailure(Call<StatusModel> call, Throwable t) {
               Log.e("status","failed get json");
            }
        });
    }
    private void getDataNow(final String idtanam){
        Call<HistoryModel> call = mJsonApi.getHistory(0,idtanam);
        call.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                if(!response.isSuccessful()){
                    Log.e("getDataNow","cek koneksi kembali");
                    return;
                }
                HistoryModel historyModel = response.body();
                if(!historyModel.getError()){
                    ArrayList<History> data= new ArrayList<>(historyModel.getHistories());
                    TampilanHistoryAdapter adapter = new TampilanHistoryAdapter(getActivity(),data);
                    listView.setAdapter(adapter);
                    Log.i("nilai spinner",""+idtanam);
                }
            }

            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {
                Log.e("error",""+ t.getMessage());
            }
        });
    }
    private void postCatatan(String id_tanam,String catatan, String penanganan){
        Call<ResponseBody> call = mJsonApi.createPostCatatan(id_tanam,catatan,penanganan);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getContext(),"Berhasil",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error",""+ t.getMessage());
            }
        });

    }
    private void postSelesai(){
        Call<ResponseBody> call=mJsonApi.createSelesai(spinnerTanam.getSelectedItem().toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getContext(),"Penanaman" + spinnerTanam.getSelectedItem().toString() + "selesai",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("error",""+ t.getMessage());
            }
        });

    }
    private void getDataChart(final int tipe, String idtanam){
        Call<HistoryModel> call = null;
        if(id==0) {
            call = mJsonApi.getChart(id, offset, idtanam);
        } else if(id==1){
            call=mJsonApi.getChartJam(id,offset,idtanam,tgl);
        } else {
            call=mJsonApi.getChartMenit(id,offset,idtanam,tgl,jam);
        }
        call.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                if (!response.isSuccessful()){
                    Log.e("getDataChart","cek koneksi kembali");
                    return;
                }
                    HistoryModel historyModel = response.body();
                    if(!historyModel.getError()){
                        ArrayList<History> datanilai = new ArrayList<>(historyModel.getHistories());
                        dataNilaiChart.clear();
                        dataWaktuChart.clear();
                        nilaiEc.clear();
                        for (History history : datanilai) {
                            nilaiEc.add(history.getBatas_ec());
                            if (tipe==1){
                                dataNilaiChart.add(history.getNilai_ec());
                            } else if(tipe==2){
                                dataNilaiChart.add(history.getNilai_tds());
                            }
                            if(id==0){
                                dataWaktuChart.add(history.getTgl_pemupukan());
                            } else{
                                dataWaktuChart.add(history.getWaktu());
                            }
                            Log.i("datakaka",""+history.getTgl_pemupukan());
                        }
                        nilaiX.clear();
                        nilaiY.clear();
                        batas.clear();
                        Log.i("cobalengt",""+dataNilaiChart.size());
                        for (Integer angka=dataNilaiChart.size()-1;angka>=0;angka--){
                                nilaiX.add(dataWaktuChart.get(angka));
                                nilaiY.add(dataNilaiChart.get(angka));
                                if(tipe==1){
                                batas.add(nilaiEc.get(angka));}

                        }
                        int numSubcolumns = 1;
                        int numColumns = nilaiX.size();
                        // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
                        List<Column> columns = new ArrayList<Column>();
                        List<SubcolumnValue> values;
                        List<AxisValue> axisValues=new ArrayList<>();
                        axisValues.clear();
                        for (int i = 0; i < numColumns; i++) {

                            values = new ArrayList<SubcolumnValue>();
                            values.clear();
                            for (int j = 0; j < numSubcolumns; j++) {
                                if(tipe==2){
                                values.add(new SubcolumnValue(nilaiY.get(i), ChartUtils.pickColor()));}
                                else{
                                    if(nilaiY.get(i).equals(batas.get(i))){
                                        values.add(new SubcolumnValue(nilaiY.get(i), Color.parseColor("#94B447")));
                                    } else if(nilaiY.get(i)< batas.get(i)){
                                        values.add(new SubcolumnValue(nilaiY.get(i), Color.parseColor("#ff7f50")));
                                    } else if (nilaiY.get(i)> batas.get(i)){
                                        values.add(new SubcolumnValue(nilaiY.get(i), Color.parseColor("#e6e200")));
                                    }
                                }
                            }
                            axisValues.add(new AxisValue(i).setLabel(nilaiX.get(i)));

                            Column column = new Column(values);
                            column.setHasLabels(true);
                            columns.add(column);
                        }

                        data = new ColumnChartData(columns);

                        if (hasAxes) {
                            Axis axisX = new Axis();
                            Axis axisY = new Axis().setHasLines(false);
                            if (hasAxesNames) {
                                if(id==0){
                                    axisX.setName("Tanggal");
                                }
                                else if(id==1){
                                    axisX.setName("Jam");
                                }
                                else if(id==2){
                                    axisX.setName("Menit");
                                }

                                if (tipe==1) {
                                    axisY.setName("EC (μS/cm)");
                                }
                                else if(tipe==2){
                                    axisY.setName("TDS (PPM)");
                                }
                            }
                            axisX.setTextSize(10);
                            axisX.setValues(axisValues);
                            data.setAxisXBottom(axisX);
                            data.setAxisYLeft(axisY);
                        } else {
                            data.setAxisXBottom(null);
                            data.setAxisYLeft(null);
                        }
                        if (tipe==1) {
                            columnChartView.setColumnChartData(data);
                        }
                        if(tipe==2) {
                            columnChartView1.setColumnChartData(data);

                        }
                    }

            }

            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {
                Log.e("error",""+t.getMessage());
            }
        });
    }
    private class ValueTouchListener implements ColumnChartOnValueSelectListener{
        @Override
        public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {
            id=id+1;
            offset=0;
            if(id<=2) {
                if (id == 2) {
                    ofsetKanan.setVisibility(View.GONE);
                    ofsetKiri.setVisibility(View.GONE);
                    icBack.setVisibility(View.VISIBLE);
                    jam = nilaiX.get(columnIndex);

                } else if (id == 0) {
                    icBack.setVisibility(View.GONE);
                } else if (id == 1) {
                    icBack.setVisibility(View.VISIBLE);
                    tgl = nilaiX.get(columnIndex);
                }
                getDataChart(1,spinnerTanam.getSelectedItem().toString());
                getDataChart(2,spinnerTanam.getSelectedItem().toString());
                Log.i("cobaid",""+id);
            } else{
                id=2;
                Toast.makeText(getActivity(),"Terakhir",Toast.LENGTH_SHORT).show();
            }
//            Log.i("idkakacoba",""+id);
//            Toast.makeText(getActivity(),""+columnIndex,Toast.LENGTH_SHORT).show();
//            Log.i("idkakacoba",""+tgl);
        }

        @Override
        public void onValueDeselected() {

        }
    }
}
