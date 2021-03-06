package com.example.gardenwiz_v3;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import API.RetrofitBuilder;
import API.plantApi;
import API.runsData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class History extends AppCompatActivity {

    RecyclerView recyclerView;
    private  String JWT = null;
    private  String gUserID = null;
    String s1[], s2[];
    Context context = History.this;
    //int images[] = {R.drawable.dandelion, R.drawable.sunflower};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        Bundle bundle = getIntent().getExtras();
        JWT = bundle.getString("JWT");
        gUserID = bundle.getString("userID");

        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Retrofit retrofit = RetrofitBuilder.getInstance();
        plantApi myPlantAPI = retrofit.create(plantApi.class);
        System.out.println("user id "+gUserID);
        Call<List<runsData>> list = myPlantAPI.getrunsData("Bearer " + JWT, Integer.parseInt(gUserID));

        list.enqueue(new Callback<List<runsData>>() {
            @Override
            public void onResponse(Call<List<runsData>> call, Response<List<runsData>> response3) {
                System.out.println("test");
                String[] plantNames = new String[response3.body().size()];
                String[] betyID = new String[response3.body().size()];
                for (int i = 0; i < response3.body().size(); i++) {
                    System.out.println(response3.body().get(i).getRunName());
                    plantNames[i] = String.valueOf(response3.body().get(i).getRunName());
                    betyID[i] = String.valueOf(response3.body().get(i).getRunID());
                    System.out.println(response3.body().get(i).getRunID());
                }
                String[] images = null;
                MyAdapter myAdapter = new MyAdapter(context, plantNames, betyID, images, JWT, gUserID);
                recyclerView.setAdapter(myAdapter);
                //plantList.add();
            }

            @Override
            public void onFailure(Call<List<runsData>> call, Throwable t) {
                System.out.println(t.getMessage());
                call.toString();
                System.out.println("fail "+call.toString());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}