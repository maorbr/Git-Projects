package com.maor.newsfeedreader;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllNewsActivity extends AppCompatActivity implements TextDownloader.Callbacks, AdapterView.OnItemClickListener {

    private ListView listViewNews;
    private ArrayList<News> newsArrayList;
    private String authorName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news);

        listViewNews = findViewById(R.id.listViewNews);

        Intent intent = getIntent();
        authorName = intent.getStringExtra("AuthorKey");

        TextDownloader textDownloader = new TextDownloader(AllNewsActivity.this);
        String link = "http://192.168.1.20:8080/api/news";
        textDownloader.execute(link);
    }
    
    @Override
    public void onAboutToBegin() {
        Toast.makeText(this,"Loading...",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(int httpStatusCode, String errorMessage) {
        Intent intent = new Intent(AllNewsActivity.this, ErrorActivity.class);
        intent.putExtra("ErrorKey", "Error Status: " + httpStatusCode + "\nError Message: " + errorMessage);
        startActivity(intent);
    }

    @Override
    public void onSuccess(String downloadedText) {
        try {
            handleJSON(downloadedText);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (authorName != null) {
            ArrayList<News> tempArrayList = new ArrayList<>();
            for (News oneNew : newsArrayList) {
                if (oneNew.getAuthor().contains(authorName)) {
                    tempArrayList.add(oneNew);
                }
            }
            newsArrayList = tempArrayList;
        }
        NewsAdapter adapter = new NewsAdapter(AllNewsActivity.this, newsArrayList);
        listViewNews.setAdapter(adapter);
        listViewNews.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News oneNews = newsArrayList.get(position);
        Intent intent = new Intent(AllNewsActivity.this, DetailsActivity.class);
        intent.putExtra("OneNewsKey", oneNews);
        startActivity(intent);
    }

    private void handleJSON(String json) throws JSONException {

        newsArrayList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(json);

        for (int i = 0; i < jsonArray.length(); i++) {

            JSONObject jsonObject = jsonArray.getJSONObject(i);

            long id = jsonObject.getLong("id");
            String title = jsonObject.getString("title");
            String description = jsonObject.getString("description");
            String author = jsonObject.getString("author");
            String link = jsonObject.getString("link");
            String pubDate = jsonObject.getString("pubDate");

            News oneNew = new News(id, title, description, author, link, pubDate);

            newsArrayList.add(oneNew);
        }
    }
}
