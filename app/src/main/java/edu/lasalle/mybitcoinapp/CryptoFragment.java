package edu.lasalle.mybitcoinapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CryptoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class CryptoFragment extends Fragment {

    private RequestQueue request;
    private EditText cryptoSearchEditText;
    private RecyclerView cryptoRecycleView;
    private ArrayList<CurrencyModel> currencyModelArrayList;
    private CurrencyAdapter currencyAdapter;
    private ProgressBar cryptoLoadingProgressBar;

    private RecyclerView cryptoRecyclerView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CryptoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CryptoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CryptoFragment newInstance(String param1, String param2) {
        CryptoFragment fragment = new CryptoFragment();
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
        View v = inflater.inflate(R.layout.fragment_crypto, container, false);
        cryptoSearchEditText = v.findViewById(R.id.cryptoSearchEditText);
        cryptoRecycleView = v.findViewById(R.id.cryptoRecycleView);
        cryptoLoadingProgressBar = v.findViewById(R.id.cryptoLoadingProgressBar);

        currencyAdapter = new CurrencyAdapter(currencyModelArrayList, getContext());
        currencyModelArrayList = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        cryptoRecycleView.setLayoutManager(manager);
        cryptoRecycleView.setHasFixedSize(true);
        //currencyAdapter = new CurrencyAdapter(currencyModelArrayList, getContext());
        cryptoRecycleView.setAdapter(currencyAdapter);

        //Use Volley library to get info from CoinMarketAPI
        request = Volley.newRequestQueue(getContext());
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
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currencyModelArrayList = new ArrayList<>();
        cryptoRecyclerView = view.findViewById(R.id.cryptoRecycleView);
        cryptoRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cryptoRecyclerView.setHasFixedSize(true);
        currencyAdapter = new CurrencyAdapter(currencyModelArrayList, getContext());
        cryptoRecyclerView.setAdapter(currencyAdapter);

        currencyAdapter.notifyDataSetChanged();
    }

    private void filter (String currency){
        ArrayList<CurrencyModel> list = new ArrayList<>();
        for (CurrencyModel item: currencyModelArrayList){
            if (item.getName().toLowerCase().contains(currency.toLowerCase())){
                list.add(item);
            }
        }
        if (list.isEmpty()){
            Toast.makeText(getContext(), "No Currency Found", Toast.LENGTH_SHORT).show();
        }
        else {
            currencyAdapter.filterList(list);
        }
    }

    //method to get API information
    private void getCurrentInfo(){
         cryptoLoadingProgressBar.setVisibility(View.VISIBLE);
        //url of COinMarketCap API
        String url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cryptoLoadingProgressBar.setVisibility(View.GONE);
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
                            Toast.makeText(getContext(), "Failed to Extract JSON Data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                cryptoLoadingProgressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to Get Data", Toast.LENGTH_SHORT).show();
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