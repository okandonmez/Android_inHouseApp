package com.inhouseapp.inhouse;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
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
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class WorkHourActivity extends AppCompatActivity {

    String strToken;
    String workhHourURL = "http://inhouse-dev.us-west-2.elasticbeanstalk.com/api/getworkinghours";

    String strTotalHour, strMonSrt, strMonEnd, strTueSrt, strTueEnd, strWedSrt, strWedEnd, strThuSrt, strThuEnd, strFriSrt, strFriEnd;
    TextView txtTotalHour, txtMonSrt, txtMonEnd, txtTueSrt, txtTueEnd, txtWedSrt, txtWedEnd, txtThuSrt, txtThuEnd, txtFirSrt, txtFriEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_hour);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        connectUI();
        getToken();
        getInformations();
    }
    private void connectUI(){
        txtTotalHour = findViewById(R.id.txtTotalWorkingHours);
        txtMonSrt = findViewById(R.id.txtMonSrt);
        txtMonEnd = findViewById(R.id.txtMonEnd);
        txtTueSrt = findViewById(R.id.txtTueSrt);
        txtTueEnd = findViewById(R.id.txtTueEnd);
        txtWedSrt = findViewById(R.id.txtWedSrt);
        txtWedEnd = findViewById(R.id.txtWedEnd);
        txtThuSrt = findViewById(R.id.txtThuSrt);
        txtThuEnd = findViewById(R.id.txtThuEnd);
        txtFirSrt = findViewById(R.id.txtFriSrt);
        txtFriEnd = findViewById(R.id.txtFriEnd);
    }

    public void getInformations(){
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject map = new JSONObject();

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, workhHourURL, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response",response.toString());

                try {
                    strTotalHour = response.getString("totalWorkingHours");
                    strMonSrt = response.getString("mondayStartTime");
                    strMonEnd = response.getString("mondayEndTime");
                    strTueSrt = response.getString("tuesdayStartTime");
                    strTueEnd = response.getString("tuesdayEndTime");
                    strWedSrt = response.getString("wednesdayStartTime");
                    strWedEnd = response.getString("wednesdayEndTime");
                    strThuSrt = response.getString("thursdayStartTime");
                    strThuEnd = response.getString("thursdayEndTime");
                    strFriSrt = response.getString("fridayStartTime");
                    strFriEnd = response.getString("fridayEndTime");

                    txtTotalHour.setText("Total Working Hours : " +strTotalHour);
                    txtMonSrt.setText(strMonSrt);
                    txtMonEnd.setText(strMonEnd);
                    txtTueSrt.setText(strTueSrt);
                    txtTueEnd.setText(strTueEnd);
                    txtWedSrt.setText(strWedSrt);
                    txtWedEnd.setText(strWedEnd);
                    txtThuSrt.setText(strThuSrt);
                    txtThuEnd.setText(strThuEnd);
                    txtFirSrt.setText(strFriSrt);
                    txtFriEnd.setText(strFriEnd);

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
