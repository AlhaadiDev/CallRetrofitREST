package com.example.user.testretrofit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import com.example.user.testretrofit.Retrofit.ApiServiceInterface;
import com.example.user.testretrofit.Retrofit.RetrofitRequest;
import com.example.user.testretrofit.Utils.LocalData;
import com.example.user.testretrofit.Utils.MyProperties;
import com.example.user.testretrofit.Utils.Utils;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnView = findViewById(R.id.button_view);
        btnView.setOnClickListener(this);

        getAccessToken();
    }

    private void getAccessToken() {
        ApiServiceInterface apiService = RetrofitRequest.getRetrofitToken().create(ApiServiceInterface.class);

        HashMap<String, String> inputParams = new HashMap();
        inputParams.put("grant_type", "password");
        inputParams.put("client_id", "" + Utils.client_id);
        inputParams.put("client_secret", "" + Utils.client_secret);
        inputParams.put("username", "" + Utils.pre_email);
        inputParams.put("password", "" + Utils.pre_password);

        retrofit2.Call<JsonObject> call = apiService.postAccessToken(inputParams);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, Response<JsonObject> response) {
                MyProperties.ShowLogD("token",response.body().toString());
                try {
                    String jsonData = response.body().toString();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String token = jsonObject.getString("access_token");

                    LocalData.getInstance().saveString(LocalData.SP_KEY_ACCESS_TOKEN,token);
                    MyProperties.ShowLogD("accessToken",LocalData.getInstance().loadString(LocalData.SP_KEY_ACCESS_TOKEN));
                } catch (Throwable w) {
                    w.printStackTrace();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_view:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }
}
