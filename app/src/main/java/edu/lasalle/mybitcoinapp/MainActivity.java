package edu.lasalle.mybitcoinapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.lasalle.mybitcoinapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private RequestQueue request;
    private EditText cryptoSearchEditText;
   // private ProgressBar cryptoLoadingProgressBar;
    private EditText stockSearchEditText;
    private RecyclerView stockRecycleView;
    private ProgressBar stockLoadingProgressBar;
    private RecyclerView cryptoRecycleView;
    private RelativeLayout cryptoRelativeLayout;
    private ArrayList<CurrencyModel> currencyModelArrayList;
    private CurrencyAdapter currencyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_crypto);

        //other variables
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        //crypto variables 
        cryptoSearchEditText = findViewById(R.id.cryptoSearchEditText);
        cryptoRecycleView = findViewById(R.id.cryptoRecycleView);
        cryptoRelativeLayout = findViewById(R.id.cryptoRelativeLayout);
     //   cryptoLoadingProgressBar = findViewById(R.id.cryptoLoadingProgressBar);

        //stock variables
      //  stockSearchEditText = findViewById(R.id.stockSearchEditText);
      //  stockRecycleView = findViewById(R.id.stockRecycleView);
       // stockLoadingProgressBar = findViewById(R.id.stockLoadingProgressBar);


        //Set adapter class to RecyclerView
        currencyModelArrayList = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        cryptoRecycleView.setLayoutManager(manager);
        cryptoRecycleView.setHasFixedSize(true);
        currencyAdapter = new CurrencyAdapter(currencyModelArrayList, this);
        cryptoRecycleView.setAdapter(currencyAdapter);

       /* setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());            //when app is loaded set to home tab

        //when clicking on tab goto the correct fragment for the tab clicked
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
        }); */

        //Use Volley library to get info from CoinMarketAPI
        request = Volley.newRequestQueue(this);
        getCurrentInfo();

        cryptoSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
    }

    //function to goto fragment
    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void filter (String currency){
        ArrayList<CurrencyModel> list = new ArrayList<>();
        for (CurrencyModel item: currencyModelArrayList){
            if (item.getName().toLowerCase().contains(currency.toLowerCase())){
                list.add(item);
            }
        }
        if (list.isEmpty()){
            Toast.makeText(this, "No Currency found", Toast.LENGTH_SHORT).show();
        }
        else {
            currencyAdapter.filterList(list);
        }
    }


    //method to get API information
    private void getCurrentInfo(){
       // cryptoLoadingProgressBar.setVisibility(View.VISIBLE);
        //url of COinMarketCap API
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // cryptoLoadingProgressBar.setVisibility(View.GONE);
                        try {
                            JSONArray dataArray = response.getJSONArray("data");
                            for(int i = 0; i< dataArray.length(); i++){
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                //get name from api
                                String name = dataObject.getString("name");
                                //get symbol from api
                                String symbol = dataObject.getString("symbol");
                                JSONObject quote = dataObject.getJSONObject("quote");
                                JSONObject USD = quote.getJSONObject("USD");
                                //get price from api
                                double price = USD.getDouble("price");

                                currencyModelArrayList.add(new CurrencyModel(name, symbol, price));
                            }
                            currencyAdapter.notifyDataSetChanged();

                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Failed to Extract JSON Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  cryptoLoadingProgressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "Failed to Get Data", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers= new HashMap<>();
                headers.put("X-CMC_PRO_API_KEY","6704264f-0c9d-47b5-8e97-98a4606aec0c");
                return headers;
            }
        };
        request.add(objectRequest);
    }


}