package com.appteamnith.hillffair.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appteamnith.hillffair.R;
import com.appteamnith.hillffair.adapters.ClubEventAdapter;
import com.appteamnith.hillffair.modals.ClubEvent;
import com.appteamnith.hillffair.utilities.Utils;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ClubEventAdapter adapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Toolbar toolbar= (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar= (ProgressBar) findViewById(R.id.progress);
        adapter=new ClubEventAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        showData();
    }


    public  class ClubResponse{
        @SerializedName("clubs")
        private ArrayList<ClubEvent> list;

        @SerializedName("success")
        private boolean success;

        @SerializedName("error")
        private String error;

        public ClubResponse(ArrayList<ClubEvent> list, boolean success, String error) {
            this.list = list;
            this.success = success;
            this.error = error;
        }

        public ArrayList<ClubEvent> getList() {
            return list;
        }

        public void setList(ArrayList<ClubEvent> list) {
            this.list = list;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }

    private  void showData(){
        Call<ClubResponse> getClubResponseCall= Utils.getRetrofitService().getAllClub();
        getClubResponseCall.enqueue(new Callback<ClubResponse>() {
            @Override
            public void onResponse(Call<ClubResponse> call, Response<ClubResponse> response) {
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                ClubResponse clubResponse=response.body();
                if(clubResponse!=null&&response.isSuccess()){
                    if(clubResponse.isSuccess()){
                        adapter.refresh(clubResponse.getList());
                    }
                }
                else {
                    Toast.makeText(EventActivity.this,"Error While Fetching Data",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClubResponse> call, Throwable t) {
                recyclerView.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
               t.printStackTrace();
                Toast.makeText(EventActivity.this,"Error While Fetching Data",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
