package com.jlucero.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News>
{
    List<News> mNewsList;


    public NewsAdapter(@NonNull Context context, List<News> list) {
        super(context,0, list);
        mNewsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        News currentNews = mNewsList.get(position);

        if(view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item,parent,false);

        //Read the parameters from the News Item
        String sectionName          = currentNews.getSectionName().trim();
        String sectionTitle         = currentNews.getPublicationTitle().trim();
        String publicationDateTime  = currentNews.getPublicationDate().trim();
        String author               = currentNews.getAuthor().trim();

        //TextViewMapping
        TextView txtSectionName = view.findViewById(R.id.sectionName_text_view);
        txtSectionName.setText(sectionName);
        TextView txtPublicationTitle = view.findViewById(R.id.sectionTitle_text_view);
        txtPublicationTitle.setText(sectionTitle);
        TextView txtPublicationDate = view.findViewById(R.id.publicationDate_text_view);
        txtPublicationDate.setText(NewsUtils.getNewsDateTime(publicationDateTime));
        TextView txtAuthor  = view.findViewById(R.id.authorPublication_text_view);
        txtAuthor.setText(author);
        return view;
    }
}
