package com.example.appkahootadi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class User extends Fragment implements View.OnClickListener {
    EditText nickname;
    Button startBtn;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public User() {
    }

    public static User newInstance(String param1, String param2) {
        User fragment = new User();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        startBtn = (Button) v.findViewById(R.id.start);
        startBtn.setOnClickListener(this);
        nickname = (EditText) v.findViewById(R.id.txtNick);
        SharedPreferences preferences = getContext().getSharedPreferences("credentials",Context.MODE_PRIVATE);
        String userNick = preferences.getString("user","");

        nickname.setText(userNick);

        return v;
    }

    @Override
    public void onClick(View view) {
        String user = nickname.getText().toString();
        SharedPreferences preferences = getContext().getSharedPreferences("credentials",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user",user);
        editor.apply();
    }
}