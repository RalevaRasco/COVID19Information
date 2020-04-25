package com.example.covid_19information;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText country;
    TextView confirmed;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        country = findViewById(R.id.country);
        confirmed = findViewById(R.id.confirmed);
        search = findViewById(R.id.search);

    }

    public void fetchData(View view) {
        final TextView textView = (TextView) findViewById(R.id.text);
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.covid19api.com/summary";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject responseObject = new JSONObject(response);
                            JSONArray countriesArray = responseObject.getJSONArray("Countries");
                            for(int i = 0; i < countriesArray.length(); i++) {
                                JSONObject currentCountry = countriesArray.getJSONObject(i);

                                String currentCountryString = currentCountry.getString("Country").toLowerCase();
                                String countrySearched = search.getText().toString().toLowerCase();
                                if(currentCountryString.equals(countrySearched)) {
                                    confirmed.setText("Total Confirmed: " + currentCountry.getString("TotalConfirmed"));
                                    confirmed.setText(currentCountry.getString("TotalRecovered"));
                                }

                            }

                        }catch (Exception e){
                            confirmed.setText("Country not found");
                        }


                        // Display the first 500 characters of the response string.
                        confirmed.setText("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                confirmed.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
