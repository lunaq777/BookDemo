package com.example.lucky.bookdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String BOOK_DETAIL_KEY = "book";
    private List<Book> mBookList;
    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;
    private ProgressBar mProgressBar;
    private BookClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBookList = new ArrayList<>();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager verticalLayoutmanager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(verticalLayoutmanager);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        fetchBooks();
    }

    private void fetchBooks() {
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        mClient = new BookClient();
        mClient.getBooks("oscar Wilde", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray docs;
                    if (response != null) {
                        // Get the docs json array
                        docs = response.getJSONArray("docs");
                        final ArrayList<Book> books = Book.fromJson(docs);
                        mBookList.clear();
                        for (Book b : books) {
                            mBookList.add(b);
                        }
                        mAdapter.notifyDataSetChanged();
                        mAdapter = new BookAdapter(MainActivity.this, mBookList);
                        mRecyclerView.setAdapter(mAdapter);
                        mProgressBar.setVisibility(ProgressBar.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                mProgressBar.setVisibility(ProgressBar.GONE);
            }
        });
    }
}
