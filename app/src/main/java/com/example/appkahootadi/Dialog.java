package com.example.appkahootadi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileWriter;

public class Dialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .4));

        final Button button = this.findViewById(R.id.button);
        button.setOnClickListener(v -> {
            EditText nick = this.findViewById(R.id.nickname);
            String text = nick.getText().toString();
            SharedPreferences preferences = getSharedPreferences("credentials",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user",text);
            editor.apply();
            finish();
        });
    }
}
