package com.uas.fahmiko.vpnuser;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.uas.fahmiko.vpnuser.adapter.AccAdapter;
import com.uas.fahmiko.vpnuser.helper.Preference;
import com.uas.fahmiko.vpnuser.model.Acc;
import com.uas.fahmiko.vpnuser.model.GetAcc;
import com.uas.fahmiko.vpnuser.rest.ApiClient;
import com.uas.fahmiko.vpnuser.rest.ApiInterface;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    Context mContext;
    private List<Acc> listAcc;
    ApiInterface mApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler2);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getSupportActionBar().setTitle("Data Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().show();
        RefreshData();

    }

    public void RefreshData(){
        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        Preference p = new Preference(getApplicationContext());
        Call<GetAcc> mAccCall = mApiInterface.getAccount(p.getUserPreferece().getId_user());
        mAccCall.enqueue(new Callback<GetAcc>() {
            @Override
            public void onResponse(Call<GetAcc> call, Response<GetAcc> response) {
                listAcc = response.body().getResult();
                mAdapter = new AccAdapter(listAcc,getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(Call<GetAcc> call, Throwable t) {
                Log.e("Get Acc",t.getMessage());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
