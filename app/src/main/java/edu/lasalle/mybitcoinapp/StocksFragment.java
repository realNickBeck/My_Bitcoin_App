package edu.lasalle.mybitcoinapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StocksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StocksFragment extends Fragment {

    private ArrayList<CurrencyModel> currencyModelArrayList;
    private EditText stockSearchEditText;
    private ProgressBar stockLoadingProgressBar;
    private CurrencyAdapter currencyAdapter;
    private RequestQueue request;

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

        request = Volley.newRequestQueue(getContext());
        return inflater.inflate(R.layout.fragment_stocks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        stockSearchEditText = view.findViewById(R.id.stockSearchEditText);
        stockLoadingProgressBar = view.findViewById(R.id.stockLoadingProgressBar);

        stockSearchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get city user puts into the edit text box
                String userInput = Objects.requireNonNull(stockSearchEditText.getText()).toString().toUpperCase();

                //check if they actually puy input into the edit text box
                if(userInput.isEmpty()){
                    Toast.makeText(getContext(), "Please Enter Stock Ticker", Toast.LENGTH_SHORT).show();
                }else{
                    //call function that gets information from the api
                    getStockInfo(userInput);
                }
            }
        });
    }


    private void getStockInfo(String userInput){
         stockLoadingProgressBar.setVisibility(View.VISIBLE);
       // String stockTicker =  String.valueOf(stockSearchEditText.getText()).toUpperCase();

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

                                currencyModelArrayList.add(new CurrencyModel(name, symbol, price));
                            }
                            currencyAdapter.notifyDataSetChanged();

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

}