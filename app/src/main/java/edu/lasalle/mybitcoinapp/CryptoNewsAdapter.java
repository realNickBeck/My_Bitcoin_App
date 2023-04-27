package edu.lasalle.mybitcoinapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CryptoNewsAdapter  extends RecyclerView.Adapter<CryptoNewsAdapter.ViewHolder>{
    private ArrayList<NewsModel> cryptoNewsModelArrayList;
    private final Context context;

    public CryptoNewsAdapter(ArrayList<NewsModel> cryptoNewsModelArrayList, Context context) {
        this.cryptoNewsModelArrayList = cryptoNewsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CryptoNewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item,parent,false);
        return new CryptoNewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CryptoNewsAdapter.ViewHolder holder, int position) {
        NewsModel newsModel = cryptoNewsModelArrayList.get(position);
        holder.publisher.setText(newsModel.getPublisher());
        holder.articleTitle.setText(newsModel.getArticleTitle());
        String image = cryptoNewsModelArrayList.get(position).getImage();

        //Toast.makeText(context, "" + image, Toast.LENGTH_SHORT).show();
        //Picasso.get().load(image).into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            Uri uri = Uri.parse(newsModel.getUrl()); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        });

    }


    @Override
    public int getItemCount() {
        return cryptoNewsModelArrayList.size();
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
