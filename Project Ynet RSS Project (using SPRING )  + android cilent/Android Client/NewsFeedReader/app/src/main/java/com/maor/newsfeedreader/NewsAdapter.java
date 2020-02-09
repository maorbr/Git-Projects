package com.maor.newsfeedreader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {
    private LayoutInflater layoutInflater;

    public NewsAdapter(@NonNull Context context, ArrayList<News> news) {
        super(context, 0, news);

        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.item_new, null);

        TextView textViewNewTitle = linearLayout.findViewById(R.id.textViewNewTitle);
        TextView textViewNewDate = linearLayout.findViewById(R.id.textViewNewDate);

        News oneNews = getItem(position);
        textViewNewTitle.setText(oneNews.getTitle());
        textViewNewDate.setText(oneNews.getPubDate());

        return linearLayout;
    }
}
