package edu.lasalle.mybitcoinapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


public class StockInfoActivity extends AppCompatActivity {

    private RequestQueue request;

    private TextView symbol ;
    private TextView name;
    private TextView price;
    private TextView about_company;
    private TextView company_info;
    private TextView sector_info;
    private TextView stock_open;
    private TextView stock_close ;
    private TextView stock_high;
    private TextView stock_low;
    private TextView market_cap ;
    private TextView button;
    private ProgressBar stockInfoProgressBar;
    private StockNewsAdapter stockNewsAdapter;
    private RecyclerView stockRecycleView;
    private ArrayList<NewsModel> stockNewsModelArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);

        stockNewsModelArrayList = new ArrayList<>();
        stockRecycleView = findViewById(R.id.stockRecyclerView);
        stockRecycleView.setLayoutManager(new LinearLayoutManager(this));
        stockRecycleView.setHasFixedSize(true);
        stockNewsAdapter = new StockNewsAdapter(stockNewsModelArrayList, this);
        stockRecycleView.setAdapter(stockNewsAdapter);

        stockNewsAdapter.notifyDataSetChanged();


        symbol = findViewById(R.id.symbol);
        name = findViewById(R.id.companyName);
        price = findViewById(R.id.price);
        about_company = findViewById(R.id.aboutCompany);
        company_info = findViewById(R.id.companyInfo);
        sector_info = findViewById(R.id.sectorInfo);
        stock_open = findViewById(R.id.openInfo);
        stock_close = findViewById(R.id.closeInfo);
        stock_high = findViewById(R.id.weekHighInfo);
        stock_low = findViewById(R.id.weekLowInfo);
        market_cap = findViewById(R.id.marketCapInfo);
        button = findViewById(R.id.button);
        stockInfoProgressBar = findViewById(R.id.stockInfoProgressBar);

        request = Volley.newRequestQueue(this);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getIncomingIntent();

    }


    private void getIncomingIntent(){
        String stock_symbol = "";
        if(getIntent().hasExtra("stock_name") && getIntent().hasExtra("stock_symbol")){
            String stock_name = getIntent().getStringExtra("stock_name");
            stock_symbol = getIntent().getStringExtra("stock_symbol").toUpperCase();

            callAlphaVantage(stock_symbol);
            getPricing(stock_symbol);
            getNews(stock_symbol);
        }
    }

    private void callAlphaVantage(String ticker){
        String link = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + ticker + "&apikey=5CCA399QMPAGBKE1";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, link, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        stockInfoProgressBar.setVisibility(View.GONE);
                        try {
                            String symbolInfo = response.getString("Symbol");
                            String nameInfo = response.getString("Name");  //get name from api
                            String description = response.getString("Description");      //get description from api
                            String marketCap = response.getString("MarketCapitalization");
                            String sector = response.getString("Sector");      //get sector from api
                            String high = response.getString("52WeekHigh");  //get 52 week high from api
                            String low = response.getString("52WeekLow");     //get 52 week low from api
                            String sharesOutstanding = response.getString("SharesOutstanding");     //get 52 week low from api

                            //float priceInfo = Integer.parseInt(marketCap) / Integer.parseInt(sharesOutstanding);
                            symbol.setText(symbolInfo);
                            name.setText(nameInfo);
                           // price.setText( Float.toString(priceInfo) );
                            about_company.setText("About " + nameInfo);
                            company_info.setText(description);
                            sector_info.setText(sector);
                           // stock_open.setText(symbolInfo;
                           // stock_close.setText(symbolInfo;
                            stock_high.setText(high);
                            stock_low.setText(low);
                            market_cap.setText(marketCap);

                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stockInfoProgressBar.setVisibility(View.GONE);
            }
        });
        request.add(objectRequest);
    }
    private void getPricing(String ticker){
        String link = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol="+ ticker + "&interval=5min&apikey=5CCA399QMPAGBKE1";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, link, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // cryptoLoadingProgressBar.setVisibility(View.GONE);
                        try {
                            JSONArray dataArray = response.getJSONArray("Time Series (5min)");
                            for(int i = 0; i< dataArray.length(); i++){
                                JSONObject dataObject = dataArray.getJSONObject(i);

                               // JSONObject quote = dataObject.getJSONObject("quote");
                               // JSONObject USD = quote.getJSONObject("USD");
                                //get price from api
                                double price = dataObject.getDouble("1. open");
                                Log.v("price", "" + price);

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //cryptoLoadingProgressBar.setVisibility(View.GONE);
            }
        });
        request.add(objectRequest);
    }

    private void getNews(String ticker){
        String link = "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&tickers="+ ticker+"&apikey=5CCA399QMPAGBKE1";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, link, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // cryptoLoadingProgressBar.setVisibility(View.GONE);
                        try {
                            JSONArray dataArray = response.getJSONArray("feed");
                            for(int i = 0; i< dataArray.length(); i++){
                                JSONObject dataObject = dataArray.getJSONObject(i);
                                //get publisher from api
                                String publisher = dataObject.getString("source");
                                //get title from api
                                String title = dataObject.getString("title");
                                String image = dataObject.getString("banner_image");

                                stockNewsModelArrayList.add(new NewsModel(publisher, title, image));
                            }
                            stockNewsAdapter.notifyDataSetChanged();
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.add(objectRequest);
    }
}
