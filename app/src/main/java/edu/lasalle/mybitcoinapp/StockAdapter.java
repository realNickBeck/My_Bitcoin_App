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

import java.util.ArrayList;

public class StockAdapter extends RecyclerView.Adapter<StockAdapter.ViewHolder>{
    private ArrayList<CurrencyModel> stockModelArrayList;
    private final Context context;

    public StockAdapter(ArrayList<CurrencyModel> stockModelArrayList, Context context) {
        this.stockModelArrayList = stockModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public StockAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stock_item,parent,false);
        return new StockAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockAdapter.ViewHolder holder, int position) {
        CurrencyModel currencyModel = stockModelArrayList.get(position);
        holder.name.setText(currencyModel.getName());
        holder.symbol.setText(currencyModel.getSymbol());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StockInfoActivity.class);
            intent.putExtra("stock_name", currencyModel.getName());
            intent.putExtra("stock_symbol", currencyModel.getSymbol());
            //Toast.makeText(context, currencyModel.getName(), Toast.LENGTH_SHORT).show();
            context.startActivity(intent);
        });
    }

    public void filterList(ArrayList<CurrencyModel> filteredList){
        stockModelArrayList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return stockModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView symbol, name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            symbol= itemView.findViewById(R.id.stockSymbol);
            name= itemView.findViewById(R.id.stockName);
        }
    }
}
