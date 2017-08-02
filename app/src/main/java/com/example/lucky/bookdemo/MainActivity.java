package com.example.lucky.bookdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements BookAdapter.ClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String BOOK_DETAIL_KEY = "book";
    public static final String OSCAR_WILDE_AUTHOR = "oscar Wilde";
    private List<Book> mBookList;
    private RecyclerView mRecyclerView;
    private BookAdapter mAdapter;
    private ProgressBar mProgressBar;
    private BookClient mClient;

    /**
     * This is an example how to use ButterKnife ! ! !
     *@BindView(R.id.example_textView)
     * TextView exampleText;
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        //TODO ButterKnife Example ! ! !
//		ButterKnife.bind(this);
//		exampleText.setText("Hello from Butterknife");

//		@OnClick(R.id.exampleButton)
//		public void submit() {
        //ButterKnife OnClick annotation
//		}

        setTitle("");
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
        mClient.getBooks(OSCAR_WILDE_AUTHOR, new JsonHttpResponseHandler() {
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
                        mAdapter = new BookAdapter(MainActivity.this, mBookList);
                        mAdapter.setOnItemClickListener(MainActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mProgressBar.setVisibility(ProgressBar.GONE);
                        mAdapter.notifyDataSetChanged();
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

    @Override
    public void onClickedItem(View v, int position) {
        Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
        intent.putExtra(BOOK_DETAIL_KEY, mBookList.get(position));
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fetch the data remotely
                fetchSearchedBook(query);
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                MainActivity.this.setTitle(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    private void fetchSearchedBook(String query) {
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        mClient = new BookClient();
        mClient.getBooks(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray docs;
                    if (response != null) {
                        docs = response.getJSONArray("docs");
                        final ArrayList<Book> books = Book.fromJson(docs);
                        mBookList.clear();
                        for (Book b : books) {
                            mBookList.add(b);
                        }
                        mAdapter = new BookAdapter(MainActivity.this, mBookList);
                        mAdapter.setOnItemClickListener(MainActivity.this);
                        mRecyclerView.setAdapter(mAdapter);
                        mProgressBar.setVisibility(ProgressBar.GONE);
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
