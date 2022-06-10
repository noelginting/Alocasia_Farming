package com.example.ndg.alocasiafarming;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ndg.alocasiafarming.Adapter.TampilanHistoryAdapter;
import com.example.ndg.alocasiafarming.Model.History;
import com.example.ndg.alocasiafarming.Model.HistoryModel;
import com.example.ndg.alocasiafarming.Model.StatusModel;
import com.example.ndg.alocasiafarming.Rest.ApiClient;
import com.example.ndg.alocasiafarming.Rest.JsonApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment {
    private JsonApi mJsonApi;
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.history__fragment,container,false);

        mJsonApi = ApiClient.getClient().create(JsonApi.class);
        listView= (ListView) view.findViewById(R.id.list_view);
        getStatus();
        return view;
    }
    private void getStatus(){
        Call<StatusModel> call= mJsonApi.getStatus(1);
        call.enqueue(new Callback<StatusModel>() {
            @Override
            public void onResponse(Call<StatusModel> call, Response<StatusModel> response) {
                if(!response.isSuccessful()){
                    Log.e("getStatus","error");
                    return;
                }
                StatusModel statusModel =response.body();
                if (!statusModel.getError()){
                    getDataHistori(statusModel.getId_tanam().toString());
                }
            }

            @Override
            public void onFailure(Call<StatusModel> call, Throwable t) {
                Log.e("getStatus","failed get json");
            }
        });
    }
    private void getDataHistori(String idtanam){
        Call<HistoryModel> call = mJsonApi.getHistory(1,idtanam);
        call.enqueue(new Callback<HistoryModel>() {
            @Override
            public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                if(!response.isSuccessful()){
                    Log.e("getDataHistori","cek koneksi kembali");
                }
                HistoryModel historyModel = response.body();
                if(!historyModel.getError()){
                    ArrayList<History> data = new ArrayList<>(historyModel.getHistories());
                    TampilanHistoryAdapter adapter = new TampilanHistoryAdapter(getActivity(),data);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<HistoryModel> call, Throwable t) {
                Log.e("getDataHistori","failed get json");
            }
        });
    }
}
