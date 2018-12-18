package com.inhouseapp.inhouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

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

public class AboutActivity extends AppCompatActivity {

    String strToken;
    String infoURL = "http://inhouse-dev.us-west-2.elasticbeanstalk.com/api/getuserinformation";

    String strFirstName, strLastName, strBirthDate, strDepartment, strDateOfEmployment, strEmail;
    TextView txtFirstName, txtLastName, txtBirthDate, txtDepartment, txtDateOfEmployment, txtEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        connectUI();
        getToken();
        getInformations();
    }

    private void connectUI(){
        txtFirstName = findViewById(R.id.txtFirstName);
        txtLastName = findViewById(R.id.txtLastName);
        txtBirthDate = findViewById(R.id.txtBirthDate);
        txtDepartment = findViewById(R.id.txtDepartment);
        txtDateOfEmployment = findViewById(R.id.txtDateOfEmployment);
        txtEmail = findViewById(R.id.txtEmail);
    }

    public void getInformations(){
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject map = new JSONObject();

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, infoURL, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    strFirstName = response.getString("firstName");
                    strLastName = response.getString("lastName");
                    strBirthDate = response.getString("birthDate");
                    strDepartment = response.getString("department");
                    strDateOfEmployment = response.getString("dateOfEmployment");
                    strEmail = response.getString("email");

                    txtFirstName.setText(strFirstName);
                    txtLastName.setText(strLastName);
                    txtBirthDate.setText(strBirthDate);
                    txtDepartment.setText(strDepartment);
                    txtDateOfEmployment.setText(strDateOfEmployment);
                    txtEmail.setText(strEmail);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                headers.put("Authorization", "Bearer "+strToken);
                return headers;
            }
        };

        queue.add(sr);
    }

    private void getToken(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("tokenizer", Context.MODE_PRIVATE);
        strToken = settings.getString("access_token", "no_token");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
