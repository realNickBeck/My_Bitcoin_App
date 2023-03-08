package edu.lasalle.mybitcoinapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StocksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StocksFragment extends Fragment {

    private ArrayList<CurrencyModel> stockModelArrayList;
    private EditText stockSearchEditText;
    private ProgressBar stockLoadingProgressBar;
    private StockAdapter stockAdapter;
    private RequestQueue request;
    private RecyclerView stockRecycleView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StocksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StocksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StocksFragment newInstance(String param1, String param2) {
        StocksFragment fragment = new StocksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_stocks, container, false);

        stockSearchEditText = v.findViewById(R.id.stockSearchEditText);
        stockRecycleView = v.findViewById(R.id.stockRecycleView);
        stockLoadingProgressBar = v.findViewById(R.id.stockLoadingProgressBar);

        stockAdapter = new StockAdapter(stockModelArrayList, getContext());
        stockModelArrayList = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        stockRecycleView.setLayoutManager(manager);
        stockRecycleView.setHasFixedSize(true);
        stockRecycleView.setAdapter(stockAdapter);

        //Use Volley library to get info from CoinMarketAPI
        request = Volley.newRequestQueue(getContext());

        getStocks();        //gets the stock ticker and stock name onto the recyclerView

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stockAdapter = new StockAdapter(stockModelArrayList, getContext());
        stockModelArrayList = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        stockRecycleView.setLayoutManager(manager);
        stockRecycleView.setHasFixedSize(true);
        stockRecycleView.setAdapter(stockAdapter);

        stockAdapter.notifyDataSetChanged();

    }

    private void getStockInfo(String userInput){
         stockLoadingProgressBar.setVisibility(View.VISIBLE);
       // String stockTicker =  String.valueOf(stockSearchEditText.getText()).toUpperCase();
        Toast.makeText(getContext(), "stock info was called", Toast.LENGTH_SHORT).show();
        //url of COinMarketCap API
        String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + userInput + "&interval=5min&apikey=5CCA399QMPAGBKE1";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        stockLoadingProgressBar.setVisibility(View.GONE);
                        try {
                            JSONArray dataArray = response.getJSONArray("Time Series (5min)");
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

                                stockModelArrayList.add(new CurrencyModel(name, symbol, price));
                            }
                            stockAdapter.notifyDataSetChanged();

                        }catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Failed to Extract JSON Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stockLoadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to Get Data", Toast.LENGTH_SHORT).show();
            }
        });
        request.add(objectRequest);
    }

    private void getStocks()  {
        stockLoadingProgressBar.setVisibility(View.VISIBLE);
        //url of COinMarketCap API
        String url = "https://www.alphavantage.co/query?function=LISTING_STATUS&apikey=5CCA399QMPAGBKE1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                stockLoadingProgressBar.setVisibility(View.GONE);
                try {
                    String[] stockTickers ;
                    String[] stockCompanies;

                    String[] stockLine = response.split("\n");
                    String[] stockTokens;

                    stockTickers = new String[stockLine.length];
                    stockCompanies = new String[stockLine.length];

                    for(int i=1; i< 10; i++ ){
                        stockTokens = stockLine[i].split(",");
                        stockTickers[i] = stockTokens[0];
                        String ticker = "" + stockTickers[i];

                        Log.v("Ticker: ", stockTickers[i]);

                        stockCompanies[i] = stockTokens[1];
                        String name = "" + stockCompanies[i];

                        Log.v("Company: ", stockCompanies[i]);
                        Log.v("number: ", "" + i);

                        stockModelArrayList.add(new CurrencyModel(name, ticker ));
                    }
                    stockAdapter.notifyDataSetChanged();


                } catch(Exception E){
                   // Toast.makeText(getContext(), "Failed to Extract CSV Data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stockLoadingProgressBar.setVisibility(View.GONE);
               // Toast.makeText(getContext(), "Failed to Get Data", Toast.LENGTH_SHORT).show();
            }
        });

        request.add(stringRequest);
    }
}
