package com.example.ndg.alocasiafarming.Admin;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ndg.alocasiafarming.Model.LoginModel;
import com.example.ndg.alocasiafarming.Model.StatusModel;
import com.example.ndg.alocasiafarming.R;
import com.example.ndg.alocasiafarming.Rest.ApiClient;
import com.example.ndg.alocasiafarming.Rest.JsonApi;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminLogin extends AppCompatActivity {
    private EditText userNameText;
    private EditText passText;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        userNameText=(EditText) findViewById(R.id.username);
        passText=(EditText) findViewById(R.id.password);
        btnLogin=(Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = userNameText.getText().toString();
                if(TextUtils.isEmpty(username)){
                    userNameText.setError("Tidak Boleh Kosong");
                    return;
                }
                String password = passText.getText().toString();
                if (TextUtils.isEmpty(password)){
                    passText.setError("Tidak Boleh Kosong");
                    return;
                }
                JsonApi mJsonApi = ApiClient.getClient().create(JsonApi.class);
                Call<LoginModel> call = mJsonApi.createLogin(username,password);
                call.enqueue(new Callback<LoginModel>() {
                    @Override
                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                        if(!response.isSuccessful()){
                            Log.e("getStatus","error");
                            return;
                        }
                        LoginModel loginModel = response.body();
                        if(loginModel.getLogin()==true) {
                            Toast.makeText(AdminLogin.this,"Berhasil Login",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AdminLogin.this, Admin.class);
                            startActivity(intent);

                            Log.i("coba",""+loginModel.getLogin());
                        } else {
                            Toast.makeText(AdminLogin.this,"Gagal Login",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginModel> call, Throwable t) {
                        Toast.makeText(AdminLogin.this,"Gagal",Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }
}
