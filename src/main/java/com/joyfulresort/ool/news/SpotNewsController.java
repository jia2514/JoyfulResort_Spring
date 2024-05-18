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
public class SpotNewsController {
    @Autowired
    private SpotNewsRepository newsRepository;

    @GetMapping("/spotnews")
    public String newsPage(Model model){
        List<SpotNews> news = newsRepository.findAll();
        model.addAttribute("news", news);
        return "front-end/conference/spot_news";
    }

    @GetMapping("/spotnews/backend")
    public String newsList(Model model){
        List<SpotNews> news = newsRepository.findAll();
        model.addAttribute("news", news);
        return "back-end/conference/spot_news";
    }

    @GetMapping("/spotnews/backend/add")
    public String addPage(Model model){
        model.addAttribute("news", new SpotNews());
        return "back-end/conference/add_spot_news";
    }

    @PostMapping("/spotnews/backend/add")
    public String addNews(@ModelAttribute SpotNews news, @RequestParam("file") MultipartFile file) throws IOException, SQLException {

        news.setPhotoPath(file.getOriginalFilename());
        news.setSpotNewsPhoto(new SerialBlob(file.getBytes()));
        newsRepository.save(news);
        return "redirect:/spotnews/backend";
    }

    @GetMapping("/spotnews/images/{spotNewsId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer spotNewsId) throws SQLException {
        SpotNews image = newsRepository.findById(spotNewsId).orElse(null);
        if(image != null){
            Blob blob = image.getSpotNewsPhoto();
            int blobLength = (int) blob.length();
            byte[] blobBytes = blob.getBytes(1, blobLength);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(blobBytes);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/spotnews/backend/update/{spotNewsId}")
    public String updatePage(@PathVariable Integer spotNewsId, Model model){
        SpotNews news = newsRepository.findById(spotNewsId).orElse(null);
        model.addAttribute("news", news);
        return "/back-end/conference/update_spot_news";
    }

    @PostMapping("/spotnews/backend/update/{spotNewsId}")
    public String updateNews(@PathVariable Integer spotNewsId, MultipartFile file) throws IOException, SQLException {
        SpotNews news = newsRepository.findById(spotNewsId).orElse(null);
        if(news != null){
            news.setPhotoPath(file.getOriginalFilename());
            news.setSpotNewsPhoto(new SerialBlob(file.getBytes()));
            newsRepository.save(news);
        }


        return "redirect:/spotnews/backend";
    }

    @GetMapping("/spotnews/backend/delete/{spotNewsId}")
    public String deleteNews(@PathVariable Integer spotNewsId){
        newsRepository.deleteById(spotNewsId);
        return "redirect:/spotnews/backend";
    }

}
