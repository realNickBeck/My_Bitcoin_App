package edu.lasalle.mybitcoinapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import edu.lasalle.mybitcoinapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private RequestQueue request;
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private ProgressBar loadingProgressBar;
    private ArrayList<CurrencyModel> currencyModelArrayList;
    private CurrencyAdapter currencyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_crypto);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.recycleView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        currencyModelArrayList = new ArrayList<>();
        currencyAdapter = new CurrencyAdapter(currencyModelArrayList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(currencyAdapter);
        getCurrentInfo();


        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());            //when app is loaded set to home tab

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.stocks:
                    replaceFragment(new StocksFragment());
                    break;
                case R.id.crypto:
                    replaceFragment(new CryptoFragment());
                    break;
            }
            return true;
        });

        // cryptoInputEditText = findViewById(R.id.cryptoInputEditText);


        //Use Volley library to get info from CoinMarketAPI
        //request = Volley.newRequestQueue(this);

       /* buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = Objects.requireNonNull(cryptoInputEditText.getText()).toString();

                //check if they actually puy input into the edit text box
                if(input.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter Crypto Currency", Toast.LENGTH_SHORT).show();
                }else{
                    //call function that gets information from the api
                    getCurrentInfo(input);
                }
            }
        }); */

    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void filter(String currency) {
        ArrayList<CurrencyModel> list = new ArrayList<>();
        for (CurrencyModel item : currencyModelArrayList) {
            if (item.getName().toLowerCase().contains(currency.toLowerCase())) {
                list.add(item);
            }
        }
        if (list.isEmpty()) {
            Toast.makeText(this, "No Currency found", Toast.LENGTH_SHORT).show();
        } else {
            currencyAdapter.filterList(list);
        }
    }


    //method to get API information
    private void getCurrentInfo() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        //url of COinMarketCap API
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        //Use Volley library to get info from CoinMarketAPI
        request = Volley.newRequestQueue(this);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loadingProgressBar.setVisibility(View.GONE);
                try {
                    JSONArray data = response.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject dataObject = data.getJSONObject(i);
                        String name = dataObject.getString("name");
                        String symbol = dataObject.getString("symbol");
                        JSONObject quote = dataObject.getJSONObject("quote");
                        JSONObject USD = quote.getJSONObject("USD");
                        double price = USD.getDouble("price");
                        currencyModelArrayList.add(new CurrencyModel(name, symbol, price));
                    }

                    currencyAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Failed to Extract JSON Data", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //loadingProgressBar.setVisibility(View.GONE);
                        //error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Failed to Get Data", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            public Map<String, String> getHeader() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("X-CMC_PRO_API_KEY", "5057e6f7-2474-4f0a-812c-f996c8cf2799");
                return header;
            }
        };
        request.add(objectRequest);
    }

}
