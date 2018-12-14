package com.uas.fahmiko.vpnuser;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uas.fahmiko.vpnuser.helper.Preference;
import com.uas.fahmiko.vpnuser.model.GetUser;
import com.uas.fahmiko.vpnuser.model.User;
import com.uas.fahmiko.vpnuser.rest.ApiClient;
import com.uas.fahmiko.vpnuser.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    private List<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.txt_username);
        password = findViewById(R.id.txt_password);
        login = findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View view = v;
                ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<GetUser> mUserCall = mApiInterface.loginUser(username.getText().toString(),password.getText().toString());

                mUserCall.enqueue(new Callback<GetUser>() {
                    @Override
                    public void onResponse(Call<GetUser> call, Response<GetUser> response) {
                        if(response.body().getStatus().equals("failed")){
                            Toast.makeText(getApplicationContext(),"Gagal Login, cek username password",Toast.LENGTH_SHORT).show();
                        }else{
                            users = response.body().getResult();
                            Preference pr = new Preference(getApplicationContext());
                            pr.saveCredentials(users.get(0).getId_user(),users.get(0).getName(),users.get(0).getUsername(),users.get(0).getPassword(),users.get(0).getPhoto());
                            Intent i = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), "Login Berhasil",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GetUser> call, Throwable t) {
                        Snackbar.make(view,"Cek kembali koneksi",Snackbar.LENGTH_SHORT).show();
                    }
                });


            }
        });



    }
}
