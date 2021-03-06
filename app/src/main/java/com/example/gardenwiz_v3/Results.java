package com.example.gardenwiz_v3;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import API.PlantImages;
import API.RetrofitBuilder;
import API.RetrofitImages;
import API.plantApi;
import API.resultsData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Results extends Activity {
    Context context = Results.this;
    RecyclerView recyclerView;
    ImageView mainImage;
    TextView RunName, Desc;

    String runName, desc;
    static String runID;
    private  String JWT = null;
    private  String gUserID = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        Bundle bundle = getIntent().getExtras();
        JWT = bundle.getString("JWT");
        gUserID = bundle.getString("userID");

        recyclerView = findViewById(R.id.recyclerView);
        Retrofit retrofit = RetrofitBuilder.getInstance();
        plantApi myPlantAPI = retrofit.create(plantApi.class);
        int runid = Integer.parseInt(runID);
        Call<List<resultsData>> list = myPlantAPI.getresultsData("Bearer " + JWT, runid);
        list.enqueue(new Callback<List<resultsData>>() {
            @Override
            public void onResponse(Call<List<resultsData>> call, Response<List<resultsData>> response3) {
                String[] plantNames = new String[response3.body().size()];
                String[] betyID = new String[response3.body().size()];
                String[] sciName = new String[response3.body().size()];
                String[] state = new String[response3.body().size()];
                String[] type = new String[response3.body().size()];
                String[] shadeT = new String[response3.body().size()];
                String[] edible = new String[response3.body().size()];
                String[] bloomP = new String[response3.body().size()];
                String[] phMin = new String[response3.body().size()];
                String[] phMax = new String[response3.body().size()];
                String[] flowerColor = new String[response3.body().size()];
                String[] symbol = new String[response3.body().size()];
                String[] images = new String[response3.body().size()];
                boolean done = false;
                List<resultsData> resultsdata = response3.body();
                for (int i = 0; i < response3.body().size(); i++) {

                    plantNames[i] = String.valueOf(response3.body().get(i).getCommonName());
                    betyID[i] = String.valueOf(response3.body().get(i).getBetyID());
                    System.out.println(response3.body().get(i).getScientificName());
                    sciName[i] = String.valueOf(response3.body().get(i).getScientificName());
//                    state[i] = String.valueOf(response3.body().get(i).getState());
//                    type[i] = String.valueOf(response3.body().get(i).getType());
//                    shadeT[i] = String.valueOf(response3.body().get(i).getShadeTol());
//                    edible[i] = String.valueOf(response3.body().get(i).getEdible());
//                    bloomP[i] = String.valueOf(response3.body().get(i).getBloomPeriod());
////                    phMin[i] = String.valueOf(response3.body().get(i).getPhMin());
////                    phMax[i] = String.valueOf(response3.body().get(i).getPhMax());
//                    flowerColor[i] = String.valueOf(response3.body().get(i).getFlowerColor());
//                    symbol[i] = String.valueOf(response3.body().get(i).getSymbol());

                    //System.out.println(response3.body().get(i).getBetyID());

                    Retrofit retrofit = RetrofitImages.getInstance();
                    plantApi myPlantAPI = retrofit.create(plantApi.class);
                    Call<PlantImages> list = myPlantAPI.getplantImages("query","json","pageimages",sciName[i], "2","100");
                    int finalI = i;
                    list.enqueue(new Callback<PlantImages>() {
                        @Override
                        public void onResponse(Call<PlantImages> call, Response<PlantImages> response) {
                            //System.out.println("------");
                            PlantImages plants;
                            if(response.body().getQuery().getPages().get(0).getMissing() == null) {
                                //System.out.println(response.body().getQuery().getPages().get(0).getThumbnail().getSource());
                                if (response.body().getQuery().getPages().get(0).getThumbnail() != null) {
                                    images[finalI] = response.body().getQuery().getPages().get(0).getThumbnail().getSource();
                                }
                            }
                            if(finalI == response3.body().size()-1){
                                //
                                //MyResultsAdapter myAdapter = new MyResultsAdapter(context, plantNames, betyID, images, resultsdata );
                                //
                                MyResultsAdapter myAdapter = new MyResultsAdapter(context, plantNames, betyID, images, resultsdata, type, bloomP, state,  edible, shadeT, flowerColor, symbol,JWT,gUserID);

                                recyclerView.setAdapter(myAdapter);
                                //plantList.add();

                            }
                        }

                        @Override
                        public void onFailure(Call<PlantImages> call, Throwable t) {

                        }

                    });

                }





            }

            @Override
            public void onFailure(Call<List<resultsData>> call, Throwable t) {
                call.toString();
                System.out.println(call.toString());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}