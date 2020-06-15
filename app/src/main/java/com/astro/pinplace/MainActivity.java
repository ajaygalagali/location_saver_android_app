package com.astro.pinplace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    static ArrayList<String> arrayList = new ArrayList<String>();
    static ArrayList<String> latitudeList = new ArrayList<String>();
    static ArrayList<String> longitudeList = new ArrayList<String>();
    static  ArrayAdapter<String> arrayAdapter;

 //Pin Location App
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.astro.pinplace", Context.MODE_PRIVATE);
        arrayList.clear();
        latitudeList.clear();
        longitudeList.clear();
        try {

            arrayList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitudeList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats",ObjectSerializer.serialize(new ArrayList<String>())));
            longitudeList = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lons",ObjectSerializer.serialize(new ArrayList<String>())));


        } catch (Exception e) {
            e.printStackTrace();
        }




//        Intent back = getIntent();
//        Toast.makeText(this,back.getStringExtra("state"), Toast.LENGTH_SHORT).show();
        /*if(back.getStringExtra("state") != null){
            arrayList.add(String.valueOf(back.getStringExtra("state")));
            latitudeList.add(back.getStringExtra("lat"));
            longitudeList.add(back.getStringExtra("lon"));

        }*/



        arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent map2Intent = new Intent(getApplicationContext(),MapsActivity2.class);
                map2Intent.putExtra("lat", String.valueOf(latitudeList.get(position)));
                map2Intent.putExtra("lon", String.valueOf(longitudeList.get(position)));
                startActivity(map2Intent);


            }
        });

    }

    public void addClicked(View view){
        Intent mapIntent = new Intent(getApplicationContext(),MapsActivity.class);
        startActivity(mapIntent);

    }
}
