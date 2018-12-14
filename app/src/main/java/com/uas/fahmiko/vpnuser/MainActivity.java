package com.uas.fahmiko.vpnuser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.uas.fahmiko.vpnuser.adapter.ServerAdapter;
import com.uas.fahmiko.vpnuser.helper.ClickListenner;
import com.uas.fahmiko.vpnuser.helper.RecyclerTouchListener;
import com.uas.fahmiko.vpnuser.model.Acc;
import com.uas.fahmiko.vpnuser.model.GetAcc;
import com.uas.fahmiko.vpnuser.model.GetServer;
import com.uas.fahmiko.vpnuser.model.Server;
import com.uas.fahmiko.vpnuser.rest.ApiClient;
import com.uas.fahmiko.vpnuser.rest.ApiInterface;

public class MainActivity extends Menu{
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private List<Server> listServer;
    ApiInterface mApiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getMenu();
        if(preference.checkSavedCredetential()==false){
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
        }

        final Context mContext = getApplicationContext();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler1);
        mLayoutManager = new GridLayoutManager(mContext,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RefreshData();

        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListenner() {
            @Override
            public void onClick(View v, int position) {
                Server s = listServer.get(position);
                Date tgl = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String hariIni = df.format(tgl);
                ApiInterface mApiInterface = ApiClient.getClient().create(ApiInterface.class);

                MultipartBody.Part body = null;
                final RequestBody reqUser = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (preference.getUserPreferece().getId_user().isEmpty())?"":preference.getUserPreferece().getId_user());
                final RequestBody reqServer = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (s.getIdServer().isEmpty())?"":s.getIdServer());
                final RequestBody reqActive = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        (hariIni.isEmpty())?"":hariIni);

                Call<GetAcc> mAccCall;
                final RequestBody reqAction = MultipartBody.create(MediaType.parse("multipart/form-data"),
                        "insert");
                mAccCall = mApiInterface.postAcc(body, reqServer,
                        reqUser, reqActive, reqAction);

                mAccCall.enqueue(new Callback<GetAcc>() {
                    @Override
                    public void onResponse(Call<GetAcc> call, Response<GetAcc> response) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(reqServer);
                        sb.append(reqActive);
                        sb.append(reqUser);
                        Toast.makeText(getApplicationContext(), sb,Toast.LENGTH_LONG).show();
                        RefreshData();
                    }

                    @Override
                    public void onFailure(Call<GetAcc> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "KOneksi gagal",Toast.LENGTH_LONG).show();
                    }
                });

            }

            @Override
            public void onLongClick(View v, int position) {

            }
        }));
    }
    public void RefreshData(){
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<GetServer> mServerCall = mApiInterface.getServer();
        mServerCall.enqueue(new Callback<GetServer>() {
            @Override
            public void onResponse(Call<GetServer> call, Response<GetServer> response) {
                Log.d("Get Server",response.body().getStatus());
                listServer = response.body().getResult();
                mAdapter = new ServerAdapter(listServer,getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetServer> call, Throwable t) {
                Log.e("Get Server",t.getMessage());
            }
        });
    }
}
