package edu.lasalle.mybitcoinapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;



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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_info);


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
        }
    }

    private void callAlphaVantage(String ticker){
        String link = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + ticker + "&apikey=5CCA399QMPAGBKE1";

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, link, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // cryptoLoadingProgressBar.setVisibility(View.GONE);

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
                //cryptoLoadingProgressBar.setVisibility(View.GONE);
            }
        });
        request.add(objectRequest);
    }
}
