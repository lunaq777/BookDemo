package com.example.lucky.bookdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created on 8/2/17.
 */

public class BookDetailActivity extends AppCompatActivity {
    private ImageView mBookCover;
    private TextView mTitle;
    private TextView mAuthor;
    private TextView mPublisher;
    private TextView mPageCount;
    private BookClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // Fetch views
        mBookCover = (ImageView) findViewById(R.id.ivBookCover);
        mTitle = (TextView) findViewById(R.id.tvTitle);
        mAuthor = (TextView) findViewById(R.id.tvAuthor);
        mPublisher = (TextView) findViewById(R.id.tvPublisher);
        mPageCount = (TextView) findViewById(R.id.tvPageCount);
        // Use the book to populate the data into our views
        Book book = (Book) getIntent().getSerializableExtra(MainActivity.BOOK_DETAIL_KEY);
        loadBook(book);
    }

    // Populate data for the book
    private void loadBook(Book book) {
        //change activity title
        this.setTitle(book.getTitle());
        // Populate data
        Picasso.with(this).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(mBookCover);
        mTitle.setText(book.getTitle());
        mAuthor.setText(book.getAuthor());
        // fetch extra book data from books API
        mClient = new BookClient();
        mClient.getExtraBookDetails(book.getOpenLibraryId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.has("publishers")) {
                        // display comma separated list of publishers
                        final JSONArray publisher = response.getJSONArray("publishers");
                        final int numPublishers = publisher.length();
                        final String[] publishers = new String[numPublishers];
                        for (int i = 0; i < numPublishers; ++i) {
                            publishers[i] = publisher.getString(i);
                        }
                        mPublisher.setText(TextUtils.join(", ", publishers));
                    }
                    if (response.has("number_of_pages")) {
                        mPageCount.setText(Integer.toString(response.getInt("number_of_pages")) + " pages");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



}
