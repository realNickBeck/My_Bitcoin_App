package edu.lasalle.mybitcoinapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private TextView title ;
    private TextView publisher;
    private ImageView image;
    private EditText newsSearchEditText;
    private StockAdapter stockAdapter;
    private RequestQueue request;
    private RecyclerView newsRecycleView;
    private ArrayList<CurrencyModel> newsModelArrayList;
    private ProgressBar newsLoadingProgressBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        newsSearchEditText = v.findViewById(R.id.newsSearchEditText);
        newsLoadingProgressBar = v.findViewById(R.id.newsLoadingProgressBar);
        newsRecycleView = v.findViewById(R.id.newsRecycleView);

        stockAdapter = new StockAdapter(newsModelArrayList, getContext());
        newsModelArrayList = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        newsRecycleView.setLayoutManager(manager);
        newsRecycleView.setHasFixedSize(true);
        newsRecycleView.setAdapter(stockAdapter);

        //Use Volley library to get info from CoinMarketAPI
        request = Volley.newRequestQueue(getContext());

        getArticles();        //gets the stock ticker and stock name onto the recyclerView

        newsSearchEditText.addTextChangedListener(new TextWatcher() {
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

    private void getArticles(){
        String link = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" +  "&apikey=5CCA399QMPAGBKE1";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, link, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        newsLoadingProgressBar.setVisibility(View.GONE);
                        try {


                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                newsLoadingProgressBar.setVisibility(View.GONE);
            }
        });
        request.add(objectRequest);
    }
}