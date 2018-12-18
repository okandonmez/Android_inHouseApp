package com.inhouseapp.inhouse;

import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends AppCompatActivity {

    TextView txtGoRegister;
    EditText edtname,edtsurname,edtusername,edtpassword;
    Button btnRegister;

    String tokenURL = "http://inhouse-dev.us-west-2.elasticbeanstalk.com/account/register";
    String strusername;
    String strpassword;
    String strname;
    String strsurname;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtname = findViewById(R.id.edtName);
        edtpassword = findViewById(R.id.edtPassw);
        edtsurname = findViewById(R.id.edtSurname);
        edtusername = findViewById(R.id.edtUsername);
        btnRegister = findViewById(R.id.buttons);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strname = edtname.getText().toString();
                strpassword = edtpassword.getText().toString();
                strsurname = edtsurname.getText().toString();
                strusername = edtusername.getText().toString();

                if (strname.length() == 0){
                    edtname.setError("Bu alan boş bırakılamaz !");
                    return;
                }
                if (strsurname.length() == 0){
                    edtsurname.setError("Bu alan boş bırakılamaz !");
                    return;
                }
                if (strusername.length() == 0){
                    edtusername.setError("Bu alan boş bırakılamaz !");
                    return;
                }
                if (strpassword.length() == 0){
                    edtpassword.setError("Bu alan boş bırakılamaz !");
                    return;
                }
                setRegisterReq();

            }

        });


        }

private void setRegisterReq(){
    RequestQueue queue = Volley.newRequestQueue(this);

    JSONObject map = new JSONObject();
    try {
        map.put("FirstName", strname);
        map.put("LastName", strsurname);
        map.put("Email", strusername);
        map.put("Password", strpassword);

    } catch (JSONException e) {
        e.printStackTrace();
    }

    JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, tokenURL, map, new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            Log.e("deneme", response.toString());
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext(),"Bir hata meydana geldi !",Toast.LENGTH_LONG).show();
        }
    });

    queue.add(sr);
}

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

