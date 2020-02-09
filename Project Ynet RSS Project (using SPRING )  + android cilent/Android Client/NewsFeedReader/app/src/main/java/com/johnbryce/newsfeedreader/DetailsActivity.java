package com.johnbryce.newsfeedreader;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    private TextView idTextView;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView pubDateTextView;
    private Button linkBtn;
    private TextView descriptionTextView;
    private String url;
    private MediaPlayer btnSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        News oneNews = (News) intent.getSerializableExtra("OneNewsKey");

        idTextView = findViewById(R.id.idTextView);
        idTextView.setText(String.valueOf(oneNews.getId()));

        titleTextView = findViewById(R.id.titleTextView);
        titleTextView.setText(oneNews.getTitle());

        authorTextView = findViewById(R.id.authorTextView);
        authorTextView.setText(oneNews.getAuthor());

        pubDateTextView = findViewById(R.id.pubDateTextView);
        pubDateTextView.setText(oneNews.getPubDate());

        linkBtn = findViewById(R.id.linkBtn);
        linkBtn.setText(oneNews.getLink());
        url = oneNews.getLink();
        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSound = MediaPlayer.create(DetailsActivity.this, R.raw.select);
                btnSound.start();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        descriptionTextView = findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(oneNews.getDescription());
    }
}
