package com.example.chessapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.sqlite.SQLiteBindOrColumnIndexOutOfRangeException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Handler;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChessGameActivity extends Activity {

    private TextView textView;
    private Handler handler;
    private RequestQueue requestQueue;
    private final int POLL_INTERVAL = 1000; // 1 second

    private ChessBoard board;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chessgame_activity);

        board = findViewById(R.id.chessBoard);
        TextView BlackTextBox = findViewById(R.id.BlackUser);
        TextView WhiteTextBox = findViewById(R.id.WhiteUser);
        Button playButton = findViewById(R.id.PlayButton);
        TextView getCheckMate = findViewById(R.id.Checkmate);
        TextView mySentence = findViewById(R.id.turn);
        TextView AI = findViewById(R.id.ChatGPT);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch mySwitch = findViewById(R.id.needHelp);


        playButton.setOnClickListener(v -> {
            DatabaseMethods dbMethods = new DatabaseMethods();
            dbMethods.Play(getBaseContext());
            playButton.setVisibility(TextView.INVISIBLE);
            mySwitch.setVisibility(View.INVISIBLE);
            handler = new Handler();
            board.setAI(AI);
            board.resetBoard();
            getCheckMate.setVisibility(View.INVISIBLE);
            startPolling(BlackTextBox,WhiteTextBox, mySentence, AI, mySwitch, getCheckMate, playButton);
        });
    }
    private void startPolling( TextView BlackTextBox, TextView WhiteTextBox, TextView myTurnSentence, TextView AI, @SuppressLint("UseSwitchCompatOrMaterialCode") Switch mySwitch, TextView getCheckMate, Button Play) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (GameSession.getInstance().getID()==0 || "Player".equals(GameSession.getInstance().getRUsername()))
                {
                    requestGameData( mySwitch);
                }
                else
                {
                    WhiteTextBox.setText(UserSession.getInstance().getUsername());
                    BlackTextBox.setText(GameSession.getInstance().getRUsername());
                    if (board.isCheckMate(!"White".equals(GameSession.getInstance().getColour()))){
                        getCheckMate.setTextColor(Color.GREEN);
                        getCheckMate.setVisibility(View.VISIBLE);
                        setWinner(UserSession.getInstance().getUsername());
                        GameSession.getInstance().clearGameSession();
                        mySwitch.setVisibility(View.VISIBLE);
                        Play.setVisibility(View.VISIBLE);
                        AI.setVisibility(View.INVISIBLE);
                    }

                    else if (board.isCheckMate("White".equals(GameSession.getInstance().getColour()))){
                        getCheckMate.setTextColor(Color.RED);
                        getCheckMate.setVisibility(View.VISIBLE);
                        GameSession.getInstance().clearGameSession();
                        mySwitch.setVisibility(View.VISIBLE);
                        Play.setVisibility(View.VISIBLE);
                        AI.setVisibility(View.INVISIBLE);
                    }

                    if (GameSession.getInstance().getMyMove())
                    {
                        myTurnSentence.setVisibility(View.VISIBLE);
                    }
                    else {
                        myTurnSentence.setVisibility(View.INVISIBLE);
                        board.requestMove();
                    }

                }

                handler.postDelayed(this, POLL_INTERVAL); // Schedule next call
            }
        }, 0);
    }
    public void requestGameData( @SuppressLint("UseSwitchCompatOrMaterialCode") Switch helpNeeded) {
        requestQueue = Volley.newRequestQueue(this);
        ArrayList<String> responseString = new ArrayList<>();
        String requestURL = "https://studev.groept.be/api/a24pt104/RetrieveGameData/"+ UserSession.getInstance().getUsername()+"/" + UserSession.getInstance().getUsername();
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject curObject = response.getJSONObject(i);

                            if(curObject.getString("WhitePlayer").equals(UserSession.getInstance().getUsername()))
                            {
                                responseString.add(curObject.getString("BlackPlayer"));
                                responseString.add(curObject.getString("idGames"));
                                responseString.add("White");


                            }
                            else {
                                responseString.add(curObject.getString("WhitePlayer"));
                                responseString.add(curObject.getString("idGames"));
                                responseString.add("Black");
                            }
                            GameSession.getInstance().setGame(getApplicationContext(),responseString.get(0),Integer.parseInt(responseString.get(1)), responseString.get(2),responseString.get(2).equals("White"), helpNeeded.isChecked());
                            board.setFlipped(!"White".equals(GameSession.getInstance().getColour()));
                            board.callInvalidate();
                        }

                    } catch (JSONException e) {
                        Log.e("Database", e.getMessage(), e);
                    }
                    catch (SQLiteBindOrColumnIndexOutOfRangeException e)
                    {
                        Log.i("Waiting....", "Looking for users");
                    }
                },

                error -> {
                }
        );

        requestQueue.add(submitRequest);

    }


    public void setWinner(String winner) {
        requestQueue = Volley.newRequestQueue(this);
        String requestURL = "https://studev.groept.be/api/a24pt104/setWinner/"+winner+"/"+ GameSession.getInstance().getID();
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject curObject = response.getJSONObject(i);
                        }

                    } catch (JSONException e) {
                        Log.e("Database", e.getMessage(), e);
                    }
                },

                error -> {
                }
        );

        requestQueue.add(submitRequest);

    }

}

