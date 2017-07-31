package com.example.lucky.bookdemo;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created on 7/31/2017.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context mContext;
    private List<Book> mBooks;

    public BookAdapter(Context context, List<Book> booksList) {
        this.mBooks = booksList;
        this.mContext = context;
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvAuthor;

        public BookViewHolder(View view) {
            super(view);
            this.ivCover = view.findViewById(R.id.ivBookCover);
            this.tvTitle = view.findViewById(R.id.tvTitle);
            this.tvAuthor = view.findViewById(R.id.tvAuthor);
        }
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, null);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = mBooks.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        Picasso.with(mContext).load(Uri.parse(book.getCoverUrl())).error(R.drawable.ic_nocover).into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return null != mBooks ? mBooks.size() : 0;
    }
}
