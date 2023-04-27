package edu.lasalle.mybitcoinapp;

public class NewsModel {
    private String publisher;
    private String articleTitle;
    private String image;
    private String url;

    public NewsModel(String publisher, String articleTitle, String image, String url) {
        this.publisher = publisher;
        this.articleTitle = articleTitle;
        this.image = image;
        this.url = url;

    }
    public NewsModel(String publisher, String articleTitle, String image) {
        this.publisher = publisher;
        this.articleTitle = articleTitle;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
