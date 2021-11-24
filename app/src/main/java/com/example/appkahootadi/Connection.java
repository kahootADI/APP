package com.example.appkahootadi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
import android.util.Log;
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
    public boolean checkNicknames(String actualNickname);
    public int getSizeList();
    }
public class Connection extends Fragment implements TestService {
    private Button btn;
    private EditText serverIP;
    Handler handler;
    private Conn con;
    private SharedPreferences preferences;
    private Client client;
    private boolean conected = false;
    private int numClients = 0;

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
                if(conected){
                    con.cancel(true);
                }
                con = new Conn();
                conected = true;
                con.execute();
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

    @Override
    public boolean checkNicknames(String actualNickname) {
        return false;
    }

    @Override
    public int getSizeList() {
        return 0;
    }

    class Conn extends AsyncTask<Void, Void, MainActivity> {
        ImageView iv;
        boolean nickExists = false;
        @Override
        protected MainActivity doInBackground(Void... params) {
            try {
                CallHandler callHandler = new CallHandler();
                serverIP = (EditText) getView().findViewById(R.id.textIp);
                String ip = serverIP.getText().toString();
                Client client = new Client(ip, 7777, callHandler);
                TestService testService = (TestService) client.getGlobal(TestService.class);
                preferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
                numClients = testService.getSizeList();
                Log.d("1", String.valueOf(numClients));
                if(numClients >= 2){
                    callAnswers();
                }
                if(!preferences.getString("user","").equals("")) {
                    nickExists = testService.checkNicknames(preferences.getString("user",""));
                    if(!nickExists){
                        testService.getNickname(preferences.getString("user",""));
                    }else {
                        errorHandler();
                    }
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
                    errorHandler();
                }
            } catch (IOException e) {
            }

            return null;
        }

    }
    public void errorHandler(){
        Intent myIntent = new Intent(getContext(), Dialog.class);
        startActivity(myIntent);
    }

    public void callAnswers(){
        Intent myIntent = new Intent(getContext(), Answers.class);
        startActivity(myIntent);
    }
}
