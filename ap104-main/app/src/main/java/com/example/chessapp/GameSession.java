package com.example.chessapp;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;

public class GameSession {
    private static GameSession instance;
    private String Colour;
    private String RivalUser;
    private int IDGame;
    private boolean myMove;
    private boolean needHelp;

    private String movesString = "";

    private GameSession() { }

    public static synchronized GameSession getInstance() {
        if (instance == null) {
            instance = new GameSession();
        }
        return instance;
    }

    public void setGame(Context context, String RUser, int IdGame, String PColour, boolean pMyMove, boolean pNeedHelp) {
        this.RivalUser = RUser;
        this.IDGame = IdGame;
        this.Colour = PColour;
        this.myMove = pMyMove;
        this.needHelp = pNeedHelp;
    }

    public String getRUsername() {
        return RivalUser;
    }

    public int getID() {
        return IDGame;
    }

    public String getColour(){
        return Colour;
    }
    public boolean getMyMove(){
        return myMove;
    }
    public void setMyMove(boolean pMyMove){myMove = pMyMove;}
    public boolean getNeedHelp(){return needHelp;}
    public String getMovesString(){return movesString;}
    public void setMovesString(String moves){movesString = moves; }

    public void clearGameSession() {
        RivalUser = null;
        IDGame = 0;
        instance = null;
        myMove = false;
        needHelp = false;
    }
}