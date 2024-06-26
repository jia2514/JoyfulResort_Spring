package com.joyfulresort.ool.news;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Blob;
import java.util.Date;

@Entity
@Table(name = "spot_news_list")
public class SpotNews {
    @Id
    @Column(name = "spot_news_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer spotNewsId;
    @Column(name = "spot_news_title")
    @NotBlank(message = "*請輸入標題")
    private String spotNewsTitle;
    @Column(name = "spot_news_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "*請選擇日期")
    private Date spotNewsDate;
    @Column(name = "spot_news_abstract")
    @NotBlank(message = "*請輸入摘要")
    private String spotNewsAbstract;
    @Column(name = "spot_news_content")
    @NotBlank(message = "*請輸入內容")
    private String spotNewsContent;
    @Column(name = "news_photo_path")
    private String photoPath;
    @Lob
    @Column(name = "spot_news_photo")
    private Blob spotNewsPhoto;
    @Column(name = "spot_news_state")
    @NotNull(message = "*請輸入摘要")
    private Boolean spotNewsState;

    public Integer getSpotNewsId() {
        return spotNewsId;
    }

    public void setSpotNewsId(Integer spotNewsId) {
        this.spotNewsId = spotNewsId;
    }

    public String getSpotNewsTitle() {
        return spotNewsTitle;
    }

    public void setSpotNewsTitle(String spotNewsTitle) {
        this.spotNewsTitle = spotNewsTitle;
    }

    public Date getSpotNewsDate() {
        return spotNewsDate;
    }

    public void setSpotNewsDate(Date spotNewsDate) {
        this.spotNewsDate = spotNewsDate;
    }

    public String getSpotNewsAbstract() {
        return spotNewsAbstract;
    }

    public void setSpotNewsAbstract(String spotNewsAbstract) {
        this.spotNewsAbstract = spotNewsAbstract;
    }

    public String getSpotNewsContent() {
        return spotNewsContent;
    }

    public void setSpotNewsContent(String spotNewsContent) {
        this.spotNewsContent = spotNewsContent;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public Blob getSpotNewsPhoto() {
        return spotNewsPhoto;
    }

    public void setSpotNewsPhoto(Blob spotNewsPhoto) {
        this.spotNewsPhoto = spotNewsPhoto;
    }

    public Boolean getSpotNewsState() {
        return spotNewsState;
    }

    public void setSpotNewsState(Boolean spotNewsState) {
        this.spotNewsState = spotNewsState;
    }
}
