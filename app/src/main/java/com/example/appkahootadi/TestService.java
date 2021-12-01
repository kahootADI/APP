package com.example.appkahootadi;

import java.util.ArrayList;

public interface TestService {

    public String getNickname(String nickname, String playerIP);

    public boolean kahootStarted();

    public boolean checkNicknames(String actualNickname);

    public int getSizeList();

    public String answerPlayer(String playerIP, String answer);

    public ArrayList<String> getQuestionAnswers();

    public boolean siguientePregunta();

    public boolean panelQR();

    public boolean inCountdown();

    public long getActualTimer();

    public int getTimeout();

    public boolean respondido();
}


