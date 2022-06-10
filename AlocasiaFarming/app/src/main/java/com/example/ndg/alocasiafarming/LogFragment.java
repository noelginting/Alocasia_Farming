package com.example.ndg.alocasiafarming;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.ndg.alocasiafarming.Adapter.TampilanCatatanAdapter;
import com.example.ndg.alocasiafarming.Model.Catatan;
import com.example.ndg.alocasiafarming.Model.CatatanModel;
import com.example.ndg.alocasiafarming.Model.History;
import com.example.ndg.alocasiafarming.Model.HistoryModel;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogFragment extends Fragment implements View.OnClickListener{
    private Spinner spinnerLog;
    private JsonApi mJsonApi;
    private Button buttonOke;
    private ListView listView;
    private int id = 0;
    private int offset=0;
    private String tgl;
    private String jam;


    private ImageView icKiri;
    private ImageView icKanan;
    private ImageView icBack;
    private ImageView ofsetKiri;
    private ImageView ofsetKanan;
    private ArrayList<String> dataIdTanam = new ArrayList<String>();
    private ColumnChartData data;
    private ColumnChartView columnChartView;
    private ColumnChartView columnChartView1;
    private ViewFlipper viewFlipper;
    private LinearLayout cardGrafik;
    private LinearLayout logCatatan;

    private ArrayList<Integer> dataNilaiChart = new ArrayList<Integer>();
    private ArrayList<String> nilaiX = new ArrayList<String>();
    private ArrayList<Integer> nilaiY = new ArrayList<Integer>();
    private ArrayList<Integer> batas = new ArrayList<Integer>();
    private ArrayList<Integer> nilaiEc = new ArrayList<Integer>();
    private ArrayList<String> dataWaktuChart= new ArrayList<>();

    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_log_fragment,container,false);
        mJsonApi = ApiClient.getClient().create(JsonApi.class);

        spinnerLog= view.findViewById(R.id.id_tanam_spiner);
        buttonOke=(Button) view.findViewById(R.id.oklog);
        listView=(ListView) view.findViewById(R.id.list_view);
        columnChartView=view.findViewById(R.id.chart);
        columnChartView1=view.findViewById(R.id.chart2);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.viewfliper);
        icKiri= (ImageView) view.findViewById(R.id.ic_kiri);
        icKanan= (ImageView) view.findViewById(R.id.ic_kanan);
        icBack=(ImageView) view.findViewById(R.id.ic_kiri_id);
        ofsetKiri=(ImageView) view.findViewById(R.id.ic_kirioffset);
        ofsetKanan=(ImageView) view.findViewById(R.id.ic_kananoffset);
        logCatatan=(LinearLayout) view.findViewById(R.id.logcatatan);
        cardGrafik=(LinearLayout) view.findViewById(R.id.cardgrafik);

        getNilaiSpiner();

        icKiri.setOnClickListener(this);
        icKanan.setOnClickListener(this);
        columnChartView.setOnValueTouchListener(new ValueTouchListener());
        columnChartView1.setOnValueTouchListener(new ValueTouchListener());
        buttonOke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCatatan(spinnerLog.getSelectedItem().toString());
                getDataChart(1,spinnerLog.getSelectedItem().toString());
                getDataChart(2,spinnerLog.getSelectedItem().toString());
                logCatatan.setVisibility(LinearLayout.VISIBLE);
                cardGrafik.setVisibility(LinearLayout.VISIBLE);
            }
        });

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
                getDataChart(1,spinnerLog.getSelectedItem().toString());
                getDataChart(2,spinnerLog.getSelectedItem().toString());
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
                getDataChart(1,spinnerLog.getSelectedItem().toString());
                getDataChart(2,spinnerLog.getSelectedItem().toString());
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
                getDataChart(1,spinnerLog.getSelectedItem().toString());
                getDataChart(2,spinnerLog.getSelectedItem().toString());
            }
        });
        return view;
    }

    private void getNilaiSpiner() {
        Call<TanamModel> call = mJsonApi.getPenanaman(2);
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
                    spinnerLog.setBackgroundResource(R.drawable.spiner_border);
                    spinnerLog.setAdapter(dataAdapter);
                }
            }

            @Override
            public void onFailure(Call<TanamModel> call, Throwable t) {
                Toast.makeText(getActivity(), "Coba cek kembali koneksi internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getCatatan(String id_tanam){
        Call<CatatanModel> call = mJsonApi.getCatatan(id_tanam);
        call.enqueue(new Callback<CatatanModel>() {
            @Override
            public void onResponse(Call<CatatanModel> call, Response<CatatanModel> response) {
                CatatanModel catatanModel = response.body();
                if (!catatanModel.getError()){
                    ArrayList<Catatan> data= new ArrayList<>(catatanModel.getCatatans());
                    TampilanCatatanAdapter catatanAdapter= new TampilanCatatanAdapter(getActivity(),data);
                    listView.setAdapter(catatanAdapter);
                }
            }

            @Override
            public void onFailure(Call<CatatanModel> call, Throwable t) {
                Log.e("getStatus","failed get json");
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
                    ArrayList<com.example.ndg.alocasiafarming.Model.History> datanilai = new ArrayList<>(historyModel.getHistories());
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

        @Override
    public void onClick(View v) {
            if(v==icKanan){
                viewFlipper.showNext();
            }
            else if (v==icKiri){
                viewFlipper.showPrevious();
            }
    }
    private class ValueTouchListener implements ColumnChartOnValueSelectListener {
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
                getDataChart(1,spinnerLog.getSelectedItem().toString());
                getDataChart(2,spinnerLog.getSelectedItem().toString());
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
