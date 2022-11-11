package com.example.apiproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddData extends AppCompatActivity implements View.OnClickListener {

    TextView txtNamePaste;
    TextView txtVolume;
    TextView txtPrice;
    TextView txtCountryOrigin;
    TextView txtAbrasivenessIndex;
    ImageView imageView;
    String Image;
    public static int id_toothpaste = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener((view -> {
            Intent intent = new Intent(AddData.this, MainActivity.class);
            startActivity(intent);
        }));

        Button btnSafe = findViewById(R.id.btnSafe);
        btnSafe.setOnClickListener(this);

        Button btnDel = findViewById(R.id.btnDel);
        btnDel.setOnClickListener(this);


        txtNamePaste = findViewById(R.id.NamePaste);
        txtNamePaste.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtNamePaste.setHint(null);
            else
                txtNamePaste.setHint(R.string.namePaste);
        });

        txtVolume = findViewById(R.id.Volume);
        txtVolume.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtVolume.setHint(null);
            else
                txtVolume.setHint(R.string.volume);
        });

        txtPrice = findViewById(R.id.Price);
        txtPrice.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtPrice.setHint(null);
            else
                txtPrice.setHint(R.string.price);
        });

        txtCountryOrigin = findViewById(R.id.CountryOrigin);
        txtCountryOrigin.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtCountryOrigin.setHint(null);
            else
                txtCountryOrigin.setHint(R.string.country_of_origin);
        });

        txtAbrasivenessIndex = findViewById(R.id.AbrasivenessIndex);
        txtAbrasivenessIndex.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                txtAbrasivenessIndex.setHint(null);
            else
                txtAbrasivenessIndex.setHint(R.string.abrasiveness);
        });


        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImg.launch(intent);
        });
        if (id_toothpaste ==-1) {
            btnDel.setVisibility(View.INVISIBLE);
        }
        else {
            setData();
        }
    }
    private void setData() {
        Bundle arg = getIntent().getExtras();
        id_toothpaste = arg.getInt("id_toothpaste");
        txtNamePaste.setText(arg.getString("name_toothpaste"));
        txtVolume.setText(String.valueOf(arg.getInt("volume")));
        txtPrice.setText(String.valueOf(arg.getDouble("price")));
        txtAbrasivenessIndex.setText(String.valueOf(arg.getInt("abrasiveness_index")));
        txtCountryOrigin.setText(arg.getString("country_of_origin"));
        Image = arg.getString("Image");
        imageView.setImageBitmap(getImgBitmap(Image));
    }
    //картинки
    private Bitmap getImgBitmap(String encodedImg) {
        if (!encodedImg.equals("null")) {
            byte[] bytes = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                bytes = Base64.getDecoder().decode(encodedImg);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        return BitmapFactory.decodeResource(getResources(),
                R.drawable.stock_photo);
    }

    private final ActivityResultLauncher<Intent> pickImg = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            if (result.getData() != null) {
                Uri uri = result.getData().getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imageView.setImageBitmap(bitmap);
                    Image = encodeImage(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    });

    public static String encodeImage(Bitmap bitmap) {
        int prevW = 500;
        int prevH = bitmap.getHeight() * prevW / bitmap.getWidth();

        Bitmap b = Bitmap.createScaledBitmap(bitmap, prevW, prevH, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(bytes);
        }
        return "";
    }


    //работа с информацией


    private void postData( String name_toothpaste, int abrasiveness_index, String country_of_origin,int volume ,double price,String Image) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ДрейИА/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Mask mask = new Mask( name_toothpaste, abrasiveness_index, country_of_origin,  volume, price,  Image);

        Call<Mask> call = retrofitAPI.createPost(mask);

        call.enqueue(new Callback<Mask>() {
            @Override
            public void onResponse(Call<Mask> call, Response<Mask> response) {
                Toast.makeText(AddData.this, "Паста добавлена", Toast.LENGTH_LONG).show();

               txtNamePaste.setText("");
               txtAbrasivenessIndex.setText("");
                txtCountryOrigin.setText("");
                 txtVolume.setText("");
                txtPrice.setText("");

                txtNamePaste.clearFocus();
                txtAbrasivenessIndex.clearFocus();
                txtCountryOrigin.clearFocus();
                txtVolume.clearFocus();
                txtPrice.clearFocus();
            }

            @Override
            public void onFailure(Call<Mask> call, Throwable t) {
                Toast.makeText(AddData.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void putData(int id, String name_toothpaste, int abrasiveness_index, String country_of_origin,int volume ,double prise,String image) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ДрейИА/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Mask mask = new Mask(id, name_toothpaste, abrasiveness_index, country_of_origin,  volume, prise,  image);

        Call<Mask> call = retrofitAPI.updateData(id, mask);

        call.enqueue(new Callback<Mask>() {
            @Override
            public void onResponse(Call<Mask> call, Response<Mask> response) {
                Toast.makeText(AddData.this, "Паста изменёна", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Mask> call, Throwable t) {
                Toast.makeText(AddData.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteData(int id) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ngknn.ru:5001/NGKNN/ДрейИА/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);

        Call<Mask> call = retrofitAPI.deleteData(id);

        call.enqueue(new Callback<Mask>() {
            @Override
            public void onResponse(Call<Mask> call, Response<Mask> response) {
                Toast.makeText(AddData.this, "Товар успешно удалён", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Mask> call, Throwable t) {
                Toast.makeText(AddData.this, "Ошибка: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSafe:
//Сохранение информации(изменение и добавление)
                if (txtNamePaste!=null && txtNamePaste.getText().toString()!=" "&&
                        txtAbrasivenessIndex!=null && txtAbrasivenessIndex.getText().toString()!=" "&&
                        txtCountryOrigin!=null && txtCountryOrigin.getText().toString()!=" "&&
                        txtVolume!=null && txtVolume.getText().toString()!=" "&&
                        txtPrice!=null && txtPrice.getText().toString()!=" "){
                String name_toothpaste = txtNamePaste.getText().toString();
                int abrasiveness_index = Integer.parseInt(txtAbrasivenessIndex.getText().toString());
                String country_of_origin = txtCountryOrigin.getText().toString();
                int volume = Integer.parseInt(txtVolume.getText().toString());
                double price = Double.parseDouble(txtPrice.getText().toString());
                if (id_toothpaste==-1){
                    postData(name_toothpaste, abrasiveness_index, country_of_origin,  volume, price,  Image);
                }
                else {
                    putData(id_toothpaste, name_toothpaste, abrasiveness_index, country_of_origin, volume, price, Image);
                }}else {
                    Toast.makeText(AddData.this, "Заполнены не все поля", Toast.LENGTH_SHORT).show();
                }
                break;
//удаление
            case R.id.btnDel:

                deleteData(id_toothpaste);
                new Handler().postDelayed(() -> startActivity(
                        new Intent(AddData.this, MainActivity.class)), 200);
                break;

        }
    }
}