package com.example.chessapp;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public class ChatGPTRequest {

    private static final String TAG = "ChatGPTRequest";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void sendMessage(Context context, String message, String apiKey, Response.Listener<String> listener) {
        if(GameSession.getInstance().getNeedHelp())
        {
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            try {
                JSONObject requestBody = new JSONObject();
                requestBody.put("model", "gpt-4.1-2025-04-14");


                // gpt-4.1-nano-2025-04-14 - cheapest model
                // gpt-4.1-2025-04-14 - expensive model
                // messages is a JSON array of objects

                JSONObject messageObj = new JSONObject();
                messageObj.put("role", "user");
                messageObj.put("content", message);

                requestBody.put("messages", new org.json.JSONArray().put(messageObj));

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        API_URL,
                        response -> {
                            try {
                                // Parse the response to extract the assistant's message
                                JSONObject jsonResponse = new JSONObject(response);
                                String content = jsonResponse
                                        .getJSONArray("choices")
                                        .getJSONObject(0)
                                        .getJSONObject("message")
                                        .getString("content");

                                listener.onResponse(content);
                            } catch (Exception e) {
                                listener.onResponse("Failed to parse response: " + e.getMessage());
                            }
                        },
                        error -> {
                            Log.e(TAG, "Volley error: ", error);
                            listener.onResponse("Request failed: " + error.toString());
                        }
                ) {
                    @Override
                    public byte[] getBody() {
                        return requestBody.toString().getBytes();
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public java.util.Map<String, String> getHeaders() {
                        java.util.Map<String, String> headers = new java.util.HashMap<>();
                        headers.put("Authorization", "Bearer " + apiKey);
                        return headers;
                    }
                };

                requestQueue.add(stringRequest);

            } catch (Exception e) {
                listener.onResponse("Error preparing request: " + e.getMessage());
            }
        }

    }

}
