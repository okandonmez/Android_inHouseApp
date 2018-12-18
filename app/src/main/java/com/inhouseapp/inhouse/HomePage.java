package com.inhouseapp.inhouse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage extends AppCompatActivity {
    ActionBar myToolbar;
    private DrawerLayout mydrawerLayout;
    private ActionBarDrawerToggle myToggle;
    private NavigationView navigation;
    private Button btn;
    String strToken;
    String announcementsURL = "http://inhouse-dev.us-west-2.elasticbeanstalk.com/api/getannouncements";

    ListView listemiz;
    AnnouncementsAdapter adaptorumuz;
    Activity activity = this;

    final List<Announcement> announcements =new ArrayList<Announcement>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        listemiz = (ListView) findViewById(R.id.liste);


        getToken();
        getAnnouncements();



        myToolbar = getSupportActionBar();
        myToolbar.setTitle("inHouse Main Page");
        mydrawerLayout=(DrawerLayout) findViewById(R.id.drawerLayout);
        myToggle= new ActionBarDrawerToggle(this,mydrawerLayout,R.string.open,R.string.close);
        mydrawerLayout.addDrawerListener(myToggle);
        myToggle.syncState();
        navigation= findViewById(R.id.nav_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int selected_id= menuItem.getItemId();

                switch (selected_id)
                {
                    case R.id.home:
                        Intent intent1 = new Intent(getApplicationContext(),HomePage.class);
                        startActivity(intent1);
                        break;
                    case R.id.sendMessage:
                        Intent intent2 = new Intent(getApplicationContext(),SendMessageActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.workHours:
                        Intent intent3 = new Intent(getApplicationContext(),WorkHourActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.aboutMe:
                        Intent intent4 = new Intent(getApplicationContext(),AboutActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.logout:
                        Intent intent5 = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent5);
                        HomePage.this.finish();
                        break;

                }
                mydrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    // Navigation View
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(myToggle.onOptionsItemSelected(item))
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawermenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void getAnnouncements(){
        RequestQueue queue = Volley.newRequestQueue(this);

       /* JSONObject map = new JSONObject();

        JsonObjectRequest sr = new JsonObjectRequest(Request.Method.GET, announcementsURL, map, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("response",response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorResponse", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer "+strToken);
                return headers;
            }
        }; */

        StringRequest sr = new StringRequest(Request.Method.GET, announcementsURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++){
                        announcements.add(new Announcement(jsonArray.getJSONObject(i).getString("title"), jsonArray.getJSONObject(i).getString("description"), jsonArray.getJSONObject(i).getString("time")));
                    }

                    adaptorumuz = new AnnouncementsAdapter(activity, announcements);
                    listemiz.setAdapter(adaptorumuz);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"A error occured !",Toast.LENGTH_LONG).show();
            }
        }){
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
}
