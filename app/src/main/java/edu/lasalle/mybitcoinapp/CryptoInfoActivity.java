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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class CryptoInfoActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_crypto_info);


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
        //String stock_symbol = "";

        if(getIntent().hasExtra("crypto_name") && getIntent().hasExtra("crypto_symbol") && getIntent().hasExtra("crypto_price")){
            String crypto_name = getIntent().getStringExtra("crypto_name");
            String crypto_symbol = getIntent().getStringExtra("crypto_symbol").toUpperCase();
            String crypto_price = getIntent().getStringExtra("crypto_price");

            symbol.setText(crypto_symbol);
            name.setText(crypto_name);
            price.setText(crypto_price);
        }
    }
}
