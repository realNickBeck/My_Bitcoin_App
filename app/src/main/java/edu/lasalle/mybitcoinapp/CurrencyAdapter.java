package edu.lasalle.mybitcoinapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyAdapter.ViewHolder> {
    private ArrayList<CurrencyModel> currencyModelArrayList;
    private Context context;
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
    }

    @Override
    public int getItemCount() {
        return currencyModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView symbol, name, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            symbol= itemView.findViewById(R.id.cryptoSymbol);
            name= itemView.findViewById(R.id.cryptoName);
            price= itemView.findViewById(R.id.cryptoPrice);
        }
    }
}
