package com.inhouseapp.inhouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SendMessageActivity extends AppCompatActivity {

    EditText edtTitle, edtDescription;
    Button btnSend;

    String sendMessageUrl = "http://inhouse-dev.us-west-2.elasticbeanstalk.com/api/postmessage";

    String strTitle, strDescription;
    String strToken;

    ActionBar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        toolbar = getSupportActionBar();
        toolbar.setTitle("Send Message");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getToken();

        btnSend = findViewById(R.id.btnSend);
        edtTitle = findViewById(R.id.edtTitle);
        edtDescription = findViewById(R.id.edtDescription);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strTitle = edtTitle.getText().toString();
                strDescription = edtDescription.getText().toString();

                if (strTitle.length() < 2){
                    edtTitle.setError("Kısa");
                    return;
                }
                if (strDescription.length() < 2){
                    edtDescription.setError("Kısa");
                    return;
                }
                sendMessages();
            }
        });
    }

    private void getToken(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("tokenizer", Context.MODE_PRIVATE);
        strToken = settings.getString("access_token", "no_token");
    }

    public void sendMessages(){
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject map = new JSONObject();
        try {
            map.put("Title", strTitle);
            map.put("Description", strDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, sendMessageUrl, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(),"Message has been sended !",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
                SendMessageActivity.this.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Bir hata meydana geldi !",Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer "+strToken);
                return headers;
            }
        };

        queue.add(sr);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
