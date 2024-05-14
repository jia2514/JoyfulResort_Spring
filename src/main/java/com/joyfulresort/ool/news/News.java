package com.joyfulresort.ool.news;

import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Blob;
import java.util.Date;

@Entity
@Table(name = "spot_news_list")
public class News {
    @Id
    @Column(name = "spot_news_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer newsId;
    @Column(name = "spot_news_title")
    private String newsTitle;
    @Column(name = "spot_news_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date newsDate;
    @Column(name = "spot_news_abstract")
    private String newsAbstract;
    @Column(name = "spot_news_content")
    private String newsContent;
    @Column(name = "news_photo_path")
    private String photoPath;
    @Lob
    @Column(name = "spot_news_photo")
    private Blob newsPhoto;
    @Column(name = "spot_news_state")
    private Boolean newsState;

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public Date getNewsDate() {
        return newsDate;
    }

    public void setNewsDate(Date newsDate) {
        this.newsDate = newsDate;
    }

    public String getNewsAbstract() {
        return newsAbstract;
    }

    public void setNewsAbstract(String newsAbstract) {
        this.newsAbstract = newsAbstract;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Blob getNewsPhoto() {
        return newsPhoto;
    }

    public void setNewsPhoto(Blob newsPhoto) {
        this.newsPhoto = newsPhoto;
    }

    public Boolean getNewsState() {
        return newsState;
    }

    public void setNewsState(Boolean newsState) {
        this.newsState = newsState;
    }
}
