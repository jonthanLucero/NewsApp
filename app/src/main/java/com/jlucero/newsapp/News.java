package com.jlucero.newsapp;

/**
 * Created by Andrés on 5/25/19.
 */

public class News
{
    //News Information to display
    private String mSectionName;
    private String mPublicationDate;
    private String mPublicationTitle;
    private String mPublicationURL;
    private String mAuthor;

    public News(String sectionName, String publicationDate, String publicationTitle, String publicationURL, String author)
    {
        mSectionName            = sectionName;
        mPublicationDate        = publicationDate;
        mPublicationTitle       = publicationTitle;
        mPublicationURL         = publicationURL;
        mAuthor                 = author;
    }


    public String getSectionName(){return mSectionName;}

    public String getPublicationDate(){return mPublicationDate;}

    public String getPublicationTitle(){return mPublicationTitle;}

    public String getPublicationURL(){return mPublicationURL;}

    public String getAuthor(){return mAuthor;}
}
