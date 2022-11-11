package com.example.apiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    Spinner spinner;
    EditText findByProduct;
    ListView listView;
    List<Mask> lvProducts;
    AdapterMask pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AddData.id_toothpaste = -1;
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener((view -> startActivity(new Intent(
                MainActivity.this, AddData.class))));

        findByProduct = findViewById(R.id.FindProduct);
        findByProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                sort(spinner.getSelectedItemPosition());
            }
        });

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sort(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }
        });

        listView = findViewById(R.id.lvData);
        lvProducts = new ArrayList<>();
        pAdapter = new AdapterMask(MainActivity.this, lvProducts);
        listView.setAdapter(pAdapter);
        listView.setOnItemClickListener((arg0, arg1, position, arg3) -> {
            AddData.id_toothpaste = Integer.parseInt(String.valueOf(arg3));
            Intent intent = new Intent(MainActivity.this, AddData.class);
            intent.putExtra("id_toothpaste", Integer.parseInt(String.valueOf(arg3)));
            intent.putExtra("name_toothpaste", lvProducts.get(position).getName_toothpaste());
            intent.putExtra("abrasiveness_index", lvProducts.get(position).getAbrasiveness_index());
            intent.putExtra("country_of_origin", lvProducts.get(position).getCountry_of_origin());
            intent.putExtra("volume", lvProducts.get(position).getVolume());
            intent.putExtra("price", lvProducts.get(position).getPrice());
            intent.putExtra("Image", lvProducts.get(position).getImage());
            startActivity(intent);
        });
//отображение информации идет через новый поток и метод завершается до того каак получит иноформацию, не придумал как поправить ставлю просто ждать
        new GetProducts().execute();
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sort(int position) {

        List<Mask> list = new ArrayList<>();
        if (findByProduct.getText().equals(null)) {
            list.addAll(lvProducts);
        } else {
            for (Mask item : lvProducts) {
                if (item.getName_toothpaste().contains(findByProduct.getText())) {
                    list.add(item);
                }
            }
        }

        switch (position) {

            case 1:
                Sort.SortByName_toothpaste sp = new Sort.SortByName_toothpaste();
                Collections.sort(list, sp);
                break;

            case 2:
                Sort.SortByVolume sq = new Sort.SortByVolume();
                Collections.sort(list, sq);
                break;

            case 3:
                Sort.SortByCountry_of_origin sc = new Sort.SortByCountry_of_origin();
                Collections.sort(list, sc);
                break;
        }

        pAdapter = new AdapterMask(MainActivity.this, list);
        listView.setAdapter(pAdapter);
    }


    private class GetProducts extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://ngknn.ru:5001/NGKNN/ДрейИА/Api/toothpastes");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();

            } catch (Exception exception) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray tempArray = new JSONArray(s);
                for (int i = 0; i < tempArray.length(); i++) {

                    JSONObject productJson = tempArray.getJSONObject(i);
                    Mask tempProduct = new Mask(
                            productJson.getInt("id_toothpaste"),
                            productJson.getString("name_toothpaste"),
                            productJson.getInt("abrasiveness_index"),
                            productJson.getString("country_of_origin"),
                            productJson.getInt("volume"),
                            productJson.getDouble("price"),
                            productJson.getString("Image")
                    );
                    lvProducts.add(tempProduct);
                    pAdapter.notifyDataSetInvalidated();
                }
            } catch (Exception ignored) {

            }
        }
    }
}