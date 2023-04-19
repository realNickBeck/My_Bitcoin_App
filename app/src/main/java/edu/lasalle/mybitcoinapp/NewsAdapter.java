package edu.lasalle.mybitcoinapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private ArrayList<NewsModel> newsModelArrayList;
    private final Context context;

    public NewsAdapter(ArrayList<NewsModel> newsModelArrayList, Context context) {
            this.newsModelArrayList = newsModelArrayList;
            this.context = context;
            }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.news_item,parent,false);
            return new NewsAdapter.ViewHolder(view);
            }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
            NewsModel newsModel = newsModelArrayList.get(position);
            holder.publisher.setText(newsModel.getPublisher());
            holder.articleTitle.setText(newsModel.getArticleTitle());
            holder.url.setText(newsModel.getUrl());

            }

    public void filterList(ArrayList<NewsModel> filteredList){
            newsModelArrayList = filteredList;
            notifyDataSetChanged();
            }

    @Override
    public int getItemCount() {
            return newsModelArrayList.size();
            }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView publisher, articleTitle, url;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            publisher= itemView.findViewById(R.id.publisherName);
            articleTitle= itemView.findViewById(R.id.articleTitle);
            url= itemView.findViewById(R.id.articleImage);
        }
}
}
