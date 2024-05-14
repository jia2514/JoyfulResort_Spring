package com.joyfulresort.ool.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Controller
public class NewsController {
    @Autowired
    private NewsRepository newsRepository;

    @GetMapping("/news")
    public String newsPage(Model model){
        List<News> news = newsRepository.findAll();
        model.addAttribute("news", news);
        return "frontend/news";
    }

    @GetMapping("/news/backend")
    public String newsList(Model model){
        List<News> news = newsRepository.findAll();
        model.addAttribute("news", news);
        return "backend/news";
    }

    @GetMapping("/news/backend/add")
    public String addPage(Model model){
        model.addAttribute("news", new News());
        return "backend/add_news";
    }

    @PostMapping("/news/backend/add")
    public String addNews(@ModelAttribute News news, @RequestParam("file") MultipartFile file) throws IOException, SQLException {

        news.setPhotoPath(file.getOriginalFilename());
        news.setNewsPhoto(new SerialBlob(file.getBytes()));
        newsRepository.save(news);
        return "redirect:/news/backend";
    }

    @GetMapping("/news/images/{newsId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer newsId) throws SQLException {
        News image = newsRepository.findById(newsId).orElse(null);
        if(image != null){
            Blob blob = image.getNewsPhoto();
            int blobLength = (int) blob.length();
            byte[] blobBytes = blob.getBytes(1, blobLength);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(blobBytes);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/news/backend/update/{newsId}")
    public String updatePage(@PathVariable Integer newsId, Model model){
        News news = newsRepository.findById(newsId).orElse(null);
        model.addAttribute("news", news);
        return "/backend/update_news";
    }

    @PostMapping("/news/backend/update/{newsId}")
    public String updateNews(@PathVariable Integer newsId, MultipartFile file) throws IOException, SQLException {
        News news = newsRepository.findById(newsId).orElse(null);
        if(news != null){
            news.setPhotoPath(file.getOriginalFilename());
            news.setNewsPhoto(new SerialBlob(file.getBytes()));
            newsRepository.save(news);
        }


        return "redirect:/news/backend";
    }

    @GetMapping("/news/backend/delete/{newsId}")
    public String deleteNews(@PathVariable Integer newsId){
        newsRepository.deleteById(newsId);
        return "redirect:/news/backend";
    }

}
