package com.example.appkahootadi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.io.IOException;
import java.util.ArrayList;

import lipermi.handler.CallHandler;
import lipermi.net.Client;

public class Connection extends Fragment implements TestService {

    private Button btn;
    private EditText serverIP;
    Handler handler;
    private Conn con;
    private SharedPreferences preferences;
    private Client client;
    private boolean conected = false;
    private int numClients = 0;
    boolean siguientePregunta = false;
    private long countdownTime = 0;
    private int timeout = 0;
    boolean firstQuestion = true;
    boolean respuesta = false;

    public boolean isRespuesta() {
        return respuesta;
    }

    public void setRespuesta(boolean respuesta) {
        this.respuesta = respuesta;
    }

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
        handler = new Handler(Looper.getMainLooper());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_connection, container, false);
        btn = (Button) v.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conected) {
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
    public String getNickname(String nickname, String playerIP) {
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

    @Override
    public ArrayList<String> getQuestionAnswers() {
        return null;
    }

    @Override
    public String answerPlayer(String playerIP, String answer) {
        return playerIP;
    }

    @Override
    public boolean inCountdown() {
        return false;
    }

    @Override
    public long getActualTimer() {
        return 0;
    }

    @Override
    public int getTimeout() {
        return 0;
    }

    @Override
    public boolean respondido() {
        return respuesta;
    }

    @Override
    public boolean siguientePregunta() {
        return false;
    }


    @Override
    public boolean panelQR() { return false; }

    class Conn extends AsyncTask<Void, Void, MainActivity> {
        ImageView iv;
        boolean nickExists = false;
        boolean started = false;
        boolean end = false;
        public boolean panelQR;

        @Override
        protected MainActivity doInBackground(Void... params) {
            try {
                CallHandler callHandler = new CallHandler();
                serverIP = (EditText) getView().findViewById(R.id.textIp);
                String ip = serverIP.getText().toString();
                client = new Client(ip, 7777, callHandler);
                TestService testService = (TestService) client.getGlobal(TestService.class);
                preferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
                Log.d("Cliente", String.valueOf(client));
                if (!preferences.getString("user", "").equals("")) {
                    nickExists = testService.checkNicknames(preferences.getString("user", ""));
                    if (!nickExists) {
                        testService.getNickname(preferences.getString("user", ""), client.toString());
                    } else {
                        errorHandler();
                    }
                    if (testService.kahootStarted()) {
                        handler.post(new Runnable() {
                            public void run() {
                                iv = getView().findViewById(R.id.image);
                                iv.setImageResource(R.drawable.green);
                            }
                        });
                        while (!end) {
                            started = testService.inCountdown();
                            if (!started) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Log.i("Started", String.valueOf(started));
                                    }
                                }, 2000);
                            } else {
                                countdownTime = testService.getActualTimer();
                                end = true;
                            }
                        }
                        while (end) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (countdownTime == 0) {
                                        end = false;
                                    }
                                }
                            }, 1000);
                        }
                        if (!firstQuestion) {
                            while (!siguientePregunta) {
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        siguientePregunta = testService.siguientePregunta();
                                    }
                                }, 1000);
                            }
                        } else {
                            Log.d("PANEL QR", String.valueOf(testService.panelQR()));
                            panelQR = testService.panelQR();
                            while (!panelQR) {
                                Log.d("Panel QR", "NULO");
                                panelQR = testService.panelQR();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.d("Panel QR", String.valueOf(panelQR));
                                    }
                                }, 1000);
                            }
                        }
                        for (int i = 0; i < testService.getQuestionAnswers().size(); i++) {
                            Log.d("Respuestas de esta pregunta: ", testService.getQuestionAnswers().get(i));
                        }
                        Intent myIntent = new Intent(getContext(), Answers.class);
                        myIntent.putStringArrayListExtra("Respuestas", testService.getQuestionAnswers());
                        startActivityForResult(myIntent, 2);
                    } else {
                        handler.post(new Runnable() {
                            public void run() {
                                iv = getView().findViewById(R.id.image);
                                iv.setImageResource(R.drawable.orange);
                            }
                        });
                    }
                } else {
                    errorHandler();
                }
            } catch (IOException e) {
                Log.d("Error", e.toString());
            }
            return null;
        }
    }

    public void errorHandler() {
        Intent myIntent = new Intent(getContext(), Dialog.class);
        startActivity(myIntent);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            String answer = data.getStringExtra("Answer");
            Log.d("CLIENTE", client.toString());
            answerPlayer(client.toString(), answer);
            boolean respuesta = true;
        }
    }
}


