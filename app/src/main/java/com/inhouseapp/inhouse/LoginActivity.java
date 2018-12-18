package com.inhouseapp.inhouse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    ProgressBar pbLogin;

    TextView txtGoRegister;
    EditText edtName, edtPassword;
    Button btnLogin;

    String tokenURL = "http://inhouse-dev.us-west-2.elasticbeanstalk.com/account/gettoken";
    String strUsername;
    String strPassword;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStatubasBar();

        edtName = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassw);
        btnLogin = findViewById(R.id.button);
        txtGoRegister = findViewById(R.id.txtGoRegister);
        pbLogin = findViewById(R.id.pbLogin);
        pbLogin.setVisibility(View.INVISIBLE);

        txtGoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUsername = edtName.getText().toString();
                strPassword = edtPassword.getText().toString();

                if (strUsername.length() == 0){
                    edtName.setError("Bu alan boş bırakılamaz !");
                    return;
                }
                if (strPassword.length() == 0){
                    edtPassword.setError("Bu alan boş bırakılamaz !");
                    return;
                }

                setTokenRequest();


            }
        });

    }

    private void setTokenRequest() {
        pbLogin.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject map = new JSONObject();
        try {
            map.put("Email", strUsername);
            map.put("Password", strPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.POST, tokenURL, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String token = response.getString("token");
                    storeToToken(token);
                    if (token.length() > 1){
                        pbLogin.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(getApplicationContext(),HomePage.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbLogin.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Kullanıcı adı veya şifre hatalı !",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(sr);
    }

    private void parseVolleyError(VolleyError error) throws UnsupportedEncodingException, JSONException {
        try{
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject jsResponseBody = new JSONObject(responseBody);
            String errorDescription = jsResponseBody.getString("error_description");
            if (errorDescription.equals("The user name or password is incorrect.")) {
                Toast.makeText(getApplicationContext(), "Kullanıcı adı veya şifre hatalı", Toast.LENGTH_LONG).show();
            } else if (errorDescription.equals("You need to confirm email")) {
                Toast.makeText(getApplicationContext(), "Eposta adresinizden hesabınızı onaylamalısınız", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Server ayağa kaldırılıyor",Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setStatubasBar(){
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.appBlueColor));
    }

    private void storeToToken(String token){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("tokenizer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("access_token", token);
        editor.commit();
    }
}
