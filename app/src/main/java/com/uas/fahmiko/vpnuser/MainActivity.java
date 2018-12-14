package com.uas.fahmiko.vpnuser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import com.uas.fahmiko.vpnuser.adapter.ServerAdapter;
import com.uas.fahmiko.vpnuser.model.GetServer;
import com.uas.fahmiko.vpnuser.model.Server;
import com.uas.fahmiko.vpnuser.rest.ApiClient;
import com.uas.fahmiko.vpnuser.rest.ApiInterface;

import java.util.List;

public class MainActivity extends Menu {
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

        Context mContext = getApplicationContext();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler1);
        mLayoutManager = new GridLayoutManager(mContext,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        RefreshData();
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
