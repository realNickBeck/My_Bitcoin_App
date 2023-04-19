package edu.lasalle.mybitcoinapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StockNewsAdapter extends RecyclerView.Adapter<StockNewsAdapter.ViewHolder>{
    private ArrayList<NewsModel> stockNewsModelArrayList;
    private final Context context;

    public StockNewsAdapter(ArrayList<NewsModel> stockNewsModelArrayList, Context context) {
        this.stockNewsModelArrayList = stockNewsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public StockNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item,parent,false);
        return new StockNewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockNewsAdapter.ViewHolder holder, int position) {
        NewsModel newsModel = stockNewsModelArrayList.get(position);
        holder.publisher.setText(newsModel.getPublisher());
        holder.articleTitle.setText(newsModel.getArticleTitle());
        String image = stockNewsModelArrayList.get(position).getImage();
        Picasso.get().load(image).into(holder.image);

    }


    @Override
    public int getItemCount() {
        return stockNewsModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView publisher, articleTitle;
        private ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            publisher= itemView.findViewById(R.id.publisherName);
            articleTitle= itemView.findViewById(R.id.articleTitle);
            image= itemView.findViewById(R.id.articleImage);
        }
    }
}
