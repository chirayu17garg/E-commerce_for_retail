package com.example.e_commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class UserCategoryActivity extends AppCompatActivity {

    private ListView category_listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_category);

        //Categories

        arrayList.add("Air Conditioner");arrayList.add("Adapter");arrayList.add("All in one");arrayList.add("Antivirus");arrayList.add("Battery");arrayList.add("Bed sheet");
        arrayList.add("Blanket");arrayList.add("Cable");arrayList.add("Cctv");arrayList.add("Chimney");arrayList.add("Cooler");arrayList.add("Fan");arrayList.add("Trimmer");
        arrayList.add("Hard Disk");arrayList.add("Hand Blender");arrayList.add("Hair Dryer");arrayList.add("Graphic Card");arrayList.add("Geyser");arrayList.add("Gas");
        arrayList.add("Head Phone");arrayList.add("Iron");arrayList.add("Led TV");arrayList.add("Keyboard");arrayList.add("Mouse");arrayList.add("Speaker");
        arrayList.add("Pen Drive");arrayList.add("Laptop");arrayList.add("Mother Board");arrayList.add("Monitor");arrayList.add("Mobile Phone");arrayList.add("Mixer");
        arrayList.add("Tube Light");arrayList.add("Bulb");arrayList.add("Aata Chakki");arrayList.add("UPS");arrayList.add("Room heater");arrayList.add("Refrigerator");
        arrayList.add("Printer");arrayList.add("Washing Machine");arrayList.add("Water Purifier");arrayList.add("Wifi-Adopter");arrayList.add("Microwave");

        Collections.sort(arrayList);

        category_listView = (ListView) findViewById(R.id.categories_list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        category_listView.setAdapter(adapter);

        category_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(UserCategoryActivity.this,HomeActivity.class);
                intent.putExtra("category",arrayList.get(position));
                startActivity(intent);
            }
        });
    }
}