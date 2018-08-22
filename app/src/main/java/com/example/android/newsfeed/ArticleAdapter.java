package com.example.android.newsfeed;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ArticleAdapter extends ArrayAdapter<NewsArticle> {

    public ArticleAdapter(Context context, List<NewsArticle> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        NewsArticle currentArticle = getItem(position);

        String heading = currentArticle.getHeading();

        String details = currentArticle.getDetails();

        String date = currentArticle.getmDate();

        String author = currentArticle.getmAuthor();

        TextView dateView = listItemView.findViewById(R.id.date);
        dateView.setText(date.substring(0, 10));

        TextView headingView = listItemView.findViewById(R.id.header);
        headingView.setText(heading);

        TextView detailsView = listItemView.findViewById(R.id.details);
        detailsView.setText(details);

        TextView authorView = listItemView.findViewById(R.id.author);
        authorView.setPaintFlags(authorView.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        authorView.setText(author);

        return listItemView;
    }

}