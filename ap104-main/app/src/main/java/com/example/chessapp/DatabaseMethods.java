package com.example.chessapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DatabaseMethods {
    private RequestQueue requestQueue;
    public void checkLogin(Context context, String User, String Password, TextView txtResponseView) {
        requestQueue = Volley.newRequestQueue(context);
        String requestURL = "https://studev.groept.be/api/a24pt104/LoginUserData/" + User + "/" + User + "/" + Password;
        ArrayList<String> responseString = new ArrayList<>();
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    try {

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject curObject = response.getJSONObject(i);
                            responseString.add(curObject.getString("Username"));
                            responseString.add(curObject.getString("Email"));
                        }
                        if (responseString.size() == 2) {
                            UserSession.getInstance().setUser(responseString.get(0), responseString.get(1));

                            Intent intent = new Intent(context, ChessGameActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        if (responseString.isEmpty()) {
                            txtResponseView.setVisibility(View.VISIBLE);
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






    public void Register(Context context, String User, String Password, String Email, TextView txtResponseView) {
        requestQueue = Volley.newRequestQueue(context);
        String requestURL = "https://studev.groept.be/api/a24pt104/RegisterUserData/" + User  + "/" + Password + "/" + Email;
        ArrayList<String> responseString = new ArrayList<>();
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    try {
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);


                    } catch (NullPointerException e) {
                        Log.e("Database", e.getMessage(), e);
                    }
                },

                error -> txtResponseView.setVisibility(View.VISIBLE)
        );

        requestQueue.add(submitRequest);

    }
    public void Play(Context context) {
        requestQueue = Volley.newRequestQueue(context);
        String requestURL = "https://studev.groept.be/api/a24pt104/CheckGameHasStarted";
        ArrayList<String> responseString = new ArrayList<>();
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject curObject = response.getJSONObject(i);
                            responseString.add(curObject.getString("idGames"));
                        }
                        if (responseString.isEmpty()) {
                            NewGame(context, UserSession.getInstance().getUsername());
                        } else {
                            JoinGame(context, UserSession.getInstance().getUsername(), Integer.parseInt(responseString.get(0)));
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

    public void NewGame(Context context,  String User) {
        requestQueue = Volley.newRequestQueue(context);
        String requestURL = "https://studev.groept.be/api/a24pt104/StartGame/" + User;
        ArrayList<String> responseString = new ArrayList<>();
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                },

                error -> {
                }
        );

        requestQueue.add(submitRequest);

    }

    public void JoinGame(Context context,  String User, int idGame) {
        requestQueue = Volley.newRequestQueue(context);
        String requestURL = "https://studev.groept.be/api/a24pt104/JoinGame/" + User + "/" + idGame ;
        JsonArrayRequest submitRequest = new JsonArrayRequest(Request.Method.GET, requestURL, null,

                response -> {
                },

                error -> {
                }
        );

        requestQueue.add(submitRequest);

    }






}
