package edu.lasalle.mybitcoinapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    private ArrayList<CurrencyModel> currencyModelArrayList;
    private final Context context;
    private static DecimalFormat df2 = new DecimalFormat("#.###");

    public CurrencyAdapter(ArrayList<CurrencyModel> currencyModelArrayList, Context context) {
        this.currencyModelArrayList = currencyModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CurrencyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_item,parent,false);
        return new CurrencyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.ViewHolder holder, int position) {
        CurrencyModel currencyModel = currencyModelArrayList.get(position);
        holder.name.setText(currencyModel.getName());
        holder.symbol.setText(currencyModel.getSymbol());
        holder.price.setText("$ "+ df2.format(currencyModel.getPrice()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CryptoInfoActivity.class);
            intent.putExtra("crypto_name", currencyModel.getName());
            intent.putExtra("crypto_symbol", currencyModel.getSymbol());
            intent.putExtra("crypto_price", "$ "+ df2.format(currencyModel.getPrice()));
            intent.putExtra("crypto_volume", "" + df2.format(currencyModel.getVolume()));
            intent.putExtra("crypto_marketCap", "" + df2.format(currencyModel.getMarketCap()));

            //Toast.makeText(context, "" + currencyModel.getVolume(), Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
        });
    }

    public void filterList(ArrayList<CurrencyModel> filteredList){
        currencyModelArrayList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return currencyModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView symbol, name, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            symbol= itemView.findViewById(R.id.cryptoSymbol);
            name= itemView.findViewById(R.id.cryptoName);
            price= itemView.findViewById(R.id.cryptoPrice);
        }
    }
}
