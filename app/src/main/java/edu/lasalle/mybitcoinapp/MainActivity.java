package edu.lasalle.mybitcoinapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Objects;

import edu.lasalle.mybitcoinapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private RequestQueue request;
    private EditText searchEditText;
    private RecyclerView recyclerView;
    private ProgressBar loadingProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        searchEditText = findViewById(R.id.searchEditText);
        recyclerView = findViewById(R.id.recycleView);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);

        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());            //when app is loaded set to home tab

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch(item.getItemId()){
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
        request = Volley.newRequestQueue(this);

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

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }


    //method to get API information
    private void getCurrentInfo(String cryptoName){
        //url of COinMarketCap API
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
        String apiKey = "6704264f-0c9d-47b5-8e97-98a4606aec0c";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //get name from api and set it on app
                            String name = response.getJSONObject("data").getString("name");


                            //get symbol from api and set it on app
                            String symbol = response.getJSONObject("data").getString("symbol");;


                            //get current price of the requested crypto currency
                            String price = response.getJSONObject("quote").getJSONObject("USD").getString("price");


                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Failed to Extract JSON Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(MainActivity.this, "Failed to Get Data", Toast.LENGTH_SHORT).show();
                    }
                }

        );
        request.add(objectRequest);
    }


}