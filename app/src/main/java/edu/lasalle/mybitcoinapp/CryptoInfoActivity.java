package edu.lasalle.mybitcoinapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class CryptoInfoActivity extends AppCompatActivity {
    private RequestQueue request;

    private TextView symbol ;
    private TextView name;
    private TextView price;
    private TextView volume ;
    private TextView stock_high;
    private TextView stock_low;
    private TextView market_cap ;
    private TextView button;
    private ProgressBar cryptoInfoProgressBar;
    private CryptoNewsAdapter cryptoNewsAdapter;
    private RecyclerView cryptoNewsRecycleView;
    private ArrayList<NewsModel> cryptoNewsModelArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_info);

        cryptoNewsModelArrayList = new ArrayList<>();
        cryptoNewsRecycleView = findViewById(R.id.cryptoNewsRecyclerView);
        cryptoNewsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        cryptoNewsRecycleView.setHasFixedSize(true);
        cryptoNewsAdapter = new CryptoNewsAdapter(cryptoNewsModelArrayList, this);
        cryptoNewsRecycleView.setAdapter(cryptoNewsAdapter);

        cryptoNewsAdapter.notifyDataSetChanged();

        symbol = findViewById(R.id.symbol);
        name = findViewById(R.id.companyName);
        price = findViewById(R.id.price);
        volume = findViewById(R.id.volumeInfo);

        stock_high = findViewById(R.id.weekHighInfo);
        stock_low = findViewById(R.id.weekLowInfo);
        market_cap = findViewById(R.id.marketCapInfo);
        button = findViewById(R.id.button);
        cryptoInfoProgressBar = findViewById(R.id.cryptoInfoProgressBar);

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
        //String stock_symbol = "";

        if(getIntent().hasExtra("crypto_name") && getIntent().hasExtra("crypto_symbol") && getIntent().hasExtra("crypto_price")){
            String crypto_name = getIntent().getStringExtra("crypto_name");
            String crypto_symbol = getIntent().getStringExtra("crypto_symbol").toUpperCase();
            String crypto_price = getIntent().getStringExtra("crypto_price");
            String crypto_volume = getIntent().getStringExtra("crypto_volume");
            String crypto_marketCap = getIntent().getStringExtra("crypto_marketCap");

            symbol.setText(crypto_symbol);
            name.setText(crypto_name);
            price.setText(crypto_price);
            volume.setText(crypto_volume);
            market_cap.setText(crypto_marketCap);

            getNews(crypto_symbol);
        }
    }

    private void getNews(String ticker){
        String link = "https://www.alphavantage.co/query?function=NEWS_SENTIMENT&crypto="+ ticker+"&apikey=5CCA399QMPAGBKE1";

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
                                String url = dataObject.getString("url");

                                cryptoNewsModelArrayList.add(new NewsModel(publisher, title, image, url));
                            }
                            cryptoNewsAdapter.notifyDataSetChanged();
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
