package com.example.user.testretrofit;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.user.testretrofit.Retrofit.ApiServiceInterface;
import com.example.user.testretrofit.Retrofit.RetrofitRequest;
import com.example.user.testretrofit.Utils.LocalData;
import com.example.user.testretrofit.Utils.MyProperties;
import com.example.user.testretrofit.Utils.Utils;
import com.google.gson.JsonObject;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    EditText edtUsername, edtPassword;
    Button btnSubmit, btnScan;
    String username, password;
    TextView txName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        btnSubmit = findViewById(R.id.btn_submit);
        txName = findViewById(R.id.txtName);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnScan = findViewById(R.id.btn_scan);

        btnSubmit.setOnClickListener(this);
        btnScan.setOnClickListener(this);

        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();


    }

    private void loginNow(String user, String pass) {
        ApiServiceInterface apiService = RetrofitRequest.getRetrofitRequest().create(ApiServiceInterface.class);
        HashMap<String, String> param = new HashMap<>();
        param.put("email", "" + user);
        param.put("password", "" + pass);
        param.put("device_id", "" + Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
        param.put("brand", "" + Build.BRAND);
        param.put("package_name", "" + getApplicationContext().getPackageName());
        param.put("model", "" + Build.MODEL);
//        param.put("reg_token", "" + FirebaseInstanceId.getInstance().getToken());
        Call<JsonObject> call = apiService.UserLogin(param);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    String jsonString = response.body().toString();
                    JSONObject jsonData = new JSONObject(jsonString);
                    MyProperties.ShowLogD("login", jsonString);
                    String userName = jsonData.getJSONObject("data").getString("name");
                    txName.setText(userName);

                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                MyProperties.ShowLogD("failed", "Failed Server");
            }
        });
    }

    private void getUser() {
        MyProperties.ShowLogD("onclick", "getUser");
        ApiServiceInterface apiSevice = RetrofitRequest.getRetrofitRequest().create(ApiServiceInterface.class);
        Call<JsonObject> call = apiSevice.userDetails("Bearer " + LocalData.getInstance().loadString(LocalData.SP_KEY_ACCESS_TOKEN), "139");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    MyProperties.ShowLogD("token", response.body().toString());
                    JSONObject jsonData = new JSONObject(response.body().toString());
                    String name = jsonData.getString("name");
                    txName.setText(name);
                } catch (Throwable r) {
                    r.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    private void scanQr(){
        startActivity(new Intent(this, QRCodeActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
//                loginNow("tenant1@ionnex.com", "12345678");
                getUser();
                break;
            case R.id.btn_scan:
                scanQr();
                break;
        }
    }
}
