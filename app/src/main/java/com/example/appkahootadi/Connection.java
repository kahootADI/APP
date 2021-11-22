package com.example.appkahootadi;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.IOException;

import lipermi.handler.CallHandler;
import lipermi.net.Client;

interface TestService {
    public String getNickname(String nickname);
    public boolean kahootStarted();
}
public class Connection extends Fragment implements TestService {
    private Button btn;
    private EditText serverIP;
    Handler handler;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Connection() {
        // Required empty public constructor
    }

    public static Connection newInstance(String param1, String param2) {
        Connection fragment = new Connection();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        handler= new Handler(Looper.getMainLooper());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_connection, container, false);
        btn = (Button) v.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                new Conn().execute();
            }
        });
        return v;

    }


    @Override
    public String getNickname(String nickname) {
        return nickname;
    }

    @Override
    public boolean kahootStarted() {
        return false;
    }

    class Conn extends AsyncTask<ImageView, Void, ImageView> {
        ImageView iv;
        boolean connected = false;
        @Override
        protected ImageView doInBackground(ImageView... params) {
            Looper.prepare();
            try {
                CallHandler callHandler = new CallHandler();
                serverIP = (EditText) getView().findViewById(R.id.textIp);
                String ip = serverIP.getText().toString();
                Client client = new Client(ip, 7777, callHandler);
                TestService testService = (TestService) client.getGlobal(TestService.class);
                SharedPreferences preferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
                if(!preferences.getString("user","").equals("")) {
                    connected = true;
                    testService.getNickname(preferences.getString("user",""));
                    Toast.makeText(getContext(), "Servidor Disponible", Toast.LENGTH_SHORT).show();
                    if(testService.kahootStarted()) {
                        handler.post(new Runnable() {
                            public void run() {
                                iv = getView().findViewById(R.id.image);
                                iv.setImageResource(R.drawable.green);
                            }
                        });
                    }else{
                        handler.post(new Runnable() {
                            public void run() {
                                iv = getView().findViewById(R.id.image);
                                iv.setImageResource(R.drawable.orange);
                            }
                        });
                    }
                }else{
                    Toast.makeText(getContext(), "Error de Conexió,Nickname incorrecte", Toast.LENGTH_SHORT).show();
                }
                if(!connected){
                    Toast.makeText(getContext(), "Servidor no Disponible", Toast.LENGTH_SHORT).show();

                }
            } catch (IOException e) {
                Toast.makeText(getContext(), "Servidor no Disponible", Toast.LENGTH_SHORT).show();
            }
            Looper.loop();
            return iv;
        }

    }
}
