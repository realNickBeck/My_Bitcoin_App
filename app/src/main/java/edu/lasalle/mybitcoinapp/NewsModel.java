package edu.lasalle.mybitcoinapp;

public class NewsModel {
    private String publisher;
    private String articleTitle;
    private String url;

    public NewsModel(String publisher, String articleTitle, String url) {
        this.publisher = publisher;
        this.articleTitle = articleTitle;
        this.url = url;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
