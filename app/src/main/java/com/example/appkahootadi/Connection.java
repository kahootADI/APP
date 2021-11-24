package com.example.appkahootadi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    }
public class Connection extends Fragment implements TestService {
    private Button btn;
    private EditText serverIP;
    Handler handler;
    private Conn con;
    private SharedPreferences preferences;
    private Client client;
    private boolean conected = false;

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

    class Conn extends AsyncTask<Void, Void, MainActivity> {
        ImageView iv;
        boolean nickExists = false;
        @Override
        protected MainActivity doInBackground(Void... params) {
            try {
                Log.d("1",con.getStatus().toString());
                CallHandler callHandler = new CallHandler();
                serverIP = (EditText) getView().findViewById(R.id.textIp);
                String ip = serverIP.getText().toString();
                Client client = new Client(ip, 7777, callHandler);
                TestService testService = (TestService) client.getGlobal(TestService.class);
                preferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
                if(!preferences.getString("user","").equals("")) {
                    nickExists = testService.checkNicknames(preferences.getString("user",""));
                    if(!nickExists){
                        testService.getNickname(preferences.getString("user",""));
                    }else {
                        errorHandler("Nickname already exists,choose another",2);
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
                    errorHandler("Nickname cant be empty",1);
                }
            } catch (IOException e) {
            }
            return null;
        }
        protected void OnPostExecute(){

        }

    }
    public void errorHandler(String message,int idError){
        Looper.prepare();
        if(idError == 1){
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Error!");
            alert.setMessage(message);
            alert.setCancelable(true);

            alert.setNeutralButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            con.cancel(true);
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = alert.create();
            alert11.show();
        }else if(idError == 2) {

            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("Error!");
            alert.setMessage(message);
            alert.setCancelable(true);
            final EditText input = new EditText(getContext());
            alert.setView(input);

            alert.setPositiveButton(
                    "Save Nickname",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String userNick = input.getText().toString();
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("user", userNick);
                            editor.apply();
                            dialog.cancel();
                        }
                    });
            AlertDialog alert11 = alert.create();
            alert11.show();
        }
        Looper.loop();
    }
}
