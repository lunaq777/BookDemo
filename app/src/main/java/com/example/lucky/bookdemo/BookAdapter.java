package com.example.lucky.bookdemo;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
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
    private ClickListener clickListener;

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
    public void onBindViewHolder(final BookViewHolder holder, final int position) {
        Book book = mBooks.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        Picasso.with(mContext).load(Uri.parse(book.getCoverUrl())).error(R.drawable.ic_nocover).into(holder.ivCover);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onClickedItem(holder.itemView, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null != mBooks ? mBooks.size() : 0;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    interface ClickListener {
        void onClickedItem(View v, int position);
    }
}
