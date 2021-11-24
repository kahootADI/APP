package com.example.appkahootadi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Answers extends AppCompatActivity {

    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private TextView txtTimer;
    private String answer;
    boolean clicked = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        timer();

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        txtTimer = findViewById(R.id.txtTimer);

        String ans1= "El sol";
        String ans2= "La luna";
        String ans3= "Pluton";
        String ans4= "Marte";
        List<String> answers = new ArrayList<>();
        answers.add(ans1);
        answers.add(ans2);
        answers.add(ans3);
        answers.add(ans4);

        ArrayList <Button>buttonsAns=new ArrayList<Button>();
        buttonsAns.add(button1);
        buttonsAns.add(button2);
        buttonsAns.add(button3);
        buttonsAns.add(button4);

        if (answers.size() >= 2){
            button1.setText(answers.get(0).toString());
            button2.setText(answers.get(1).toString());
        }
        if (answers.size() >= 3){
            button3.setText(answers.get(2).toString());
        }
        if (answers.size() >= 2){
            button4.setText(answers.get(3).toString());
        }

        button1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                clicked = false;
                button1.setEnabled(false);
                answer = button1.getText().toString();

                button2.setEnabled(false);
                button2.setBackgroundColor(Color.GRAY);
                button3.setEnabled(false);
                button3.setBackgroundColor(Color.GRAY);
                button4.setEnabled(false);
                button4.setBackgroundColor(Color.GRAY);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clicked = false;
                button1.setEnabled(false);
                button1.setBackgroundColor(Color.GRAY);
                button2.setEnabled(false);

                answer = button2.getText().toString();

                button3.setEnabled(false);
                button3.setBackgroundColor(Color.GRAY);
                button4.setEnabled(false);
                button4.setBackgroundColor(Color.GRAY);

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clicked = false;
                button1.setEnabled(false);
                button1.setBackgroundColor(Color.GRAY);
                button2.setEnabled(false);
                button2.setBackgroundColor(Color.GRAY);
                button3.setEnabled(false);

                answer = button3.getText().toString();

                button4.setEnabled(false);
                button4.setBackgroundColor(Color.GRAY);

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                clicked = false;
                button1.setEnabled(false);
                button1.setBackgroundColor(Color.GRAY);
                button2.setEnabled(false);
                button2.setBackgroundColor(Color.GRAY);
                button3.setEnabled(false);
                button3.setBackgroundColor(Color.GRAY);
                button4.setEnabled(false);

                answer = button4.getText().toString();

            }
        });

    }
    CountDownTimer countTimer;


    public void timer(){

        countTimer = new CountDownTimer(6000, 1000) {

            public void onTick(long millisUntilFinished) {
                txtTimer.setText(String.valueOf(millisUntilFinished / 1000));

                if (button1.isPressed()) {
                    button1.setEnabled(false);
                    button2.setBackgroundColor(Color.GRAY);
                    button2.setEnabled(false);
                    button3.setBackgroundColor(Color.GRAY);
                    button3.setEnabled(false);
                    button4.setBackgroundColor(Color.GRAY);
                    button4.setEnabled(false);
                }
                if (button2.isPressed()) {

                    button1.setEnabled(false);
                    button1.setBackgroundColor(Color.GRAY);
                    button2.setEnabled(false);
                    button3.setBackgroundColor(Color.GRAY);
                    button3.setEnabled(false);
                    button4.setBackgroundColor(Color.GRAY);
                    button4.setEnabled(false);
                }
                if (button3.isPressed()) {

                    button1.setEnabled(false);
                    button1.setBackgroundColor(Color.GRAY);
                    button2.setEnabled(false);
                    button2.setBackgroundColor(Color.GRAY);
                    button3.setEnabled(false);
                    button4.setBackgroundColor(Color.GRAY);
                    button4.setEnabled(false);
                }
                if (button4.isPressed()) {

                    button1.setEnabled(false);
                    button1.setBackgroundColor(Color.GRAY);
                    button2.setEnabled(false);
                    button2.setBackgroundColor(Color.GRAY);
                    button3.setEnabled(false);
                    button3.setBackgroundColor(Color.GRAY);
                    button4.setEnabled(false);
                }



            }

            public void onFinish() {

                if (!clicked == false){
                    button1.setEnabled(false);
                    button1.setBackgroundColor(Color.GRAY);
                    button2.setEnabled(false);
                    button2.setBackgroundColor(Color.GRAY);
                    button3.setEnabled(false);
                    button3.setBackgroundColor(Color.GRAY);
                    button4.setEnabled(false);
                    button4.setBackgroundColor(Color.GRAY);
                }

                txtTimer.setText("0!");

            }
        }.start();
    }

}