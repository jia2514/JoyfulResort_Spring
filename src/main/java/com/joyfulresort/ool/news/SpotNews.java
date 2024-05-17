package com.joyfulresort.ool.news;

import javax.persistence.*;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Blob;
import java.util.Date;

@Entity
@Data
@Table(name = "spot_news_list")
public class SpotNews {
    @Id
    @Column(name = "spot_news_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer spotNewsId;
    @Column(name = "spot_news_title")
    private String spotNewsTitle;
    @Column(name = "spot_news_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date spotNewsDate;
    @Column(name = "spot_news_abstract")
    private String spotNewsAbstract;
    @Column(name = "spot_news_content")
    private String spotNewsContent;
    @Column(name = "news_photo_path")
    private String photoPath;
    @Lob
    @Column(name = "spot_news_photo")
    private Blob spotNewsPhoto;
    @Column(name = "spot_news_state")
    private Boolean spotNewsState;


}
