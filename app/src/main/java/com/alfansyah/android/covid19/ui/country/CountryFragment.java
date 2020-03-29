package com.alfansyah.android.covid19.ui.country;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alfansyah.android.covid19.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountryFragment extends Fragment {

    RecyclerView rvCovidCountry;
    ProgressBar progressBar;

    private static final String TAG = CountryFragment.class.getSimpleName();
    ArrayList<CovidCountry> covidCountries;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_country, container, false);

        //call view
        rvCovidCountry = root.findViewById(R.id.rvCovidCountry);
        progressBar = root.findViewById(R.id.progress_circular_country);
        rvCovidCountry.setLayoutManager(new LinearLayoutManager(getActivity()));

        //panggil method volley
        getDataFromServer();
        return root;
    }

    private void showRecyclerView(){
        CovidCountryAdapter covidCountryAdapter = new CovidCountryAdapter(covidCountries,getActivity());
        rvCovidCountry.setAdapter(covidCountryAdapter);
    }

    private void getDataFromServer() {

        String url = "https://corona.lmao.ninja/countries";

        covidCountries = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    Log.e(TAG, "onResponse: " + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);

                            //extract JSON Object  inside JSON Object
                            JSONObject countryInfo = data.getJSONObject("countryInfo");

                        covidCountries.add(new CovidCountry(
                                    data.getString("country"),
                                    data.getString("cases"),
                                    data.getString("todayCases"),
                                    data.getString("deaths"),
                                    data.getString("todayDeaths"),
                                    data.getString("recovered"),
                                    data.getString("active"),
                                    countryInfo.getString("flag")
                                    ));
                        }
                        showRecyclerView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "onErrorResponse: "+error);
                    }
                });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}