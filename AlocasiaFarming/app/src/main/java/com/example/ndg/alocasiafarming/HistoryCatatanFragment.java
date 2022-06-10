package com.example.ndg.alocasiafarming;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ndg.alocasiafarming.Adapter.TampilanCatatanAdapter;
import com.example.ndg.alocasiafarming.Model.Catatan;
import com.example.ndg.alocasiafarming.Model.CatatanModel;
import com.example.ndg.alocasiafarming.Model.StatusModel;
import com.example.ndg.alocasiafarming.Rest.ApiClient;
import com.example.ndg.alocasiafarming.Rest.JsonApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryCatatanFragment extends Fragment {
    private JsonApi mJsonApi;
    private ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_history_catatan, container, false);

        mJsonApi= ApiClient.getClient().create(JsonApi.class);
        listView=(ListView) view.findViewById(R.id.list_view);
        getStatus();
        return  view;
    }
    private void getStatus(){
        Call<StatusModel> call =mJsonApi.getStatus(1);
        call.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {
                if (!response.isSuccessful()){
                    Log.e("getStatus","error");
                    return;
                }
                StatusModel statusModel = response.body();
                if(!statusModel.getError()){
                    getCatatan(statusModel.getId_tanam());
                }
            }

            @Override
            public void onFailure(Call<StatusModel> call, Throwable t) {
                Log.e("getStatus","failed get json");
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

}
