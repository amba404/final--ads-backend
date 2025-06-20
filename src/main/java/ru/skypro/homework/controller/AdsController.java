package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdsService;
import ru.skypro.homework.service.CommentService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdsController {

    private final AdsService adsService;
    private final CommentService commentService;

    @Tag(name = "Объявления")
    @GetMapping()
    public Ads getAllAds() {
        return adsService.getAllAds();
    }

    @Tag(name = "Объявления")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Ad addAd(@RequestBody CreateOrUpdateAd properties, @RequestBody MultipartFile image) {
        return adsService.addAd(properties, image);
    }

    @Tag(name = "Объявления")
    @GetMapping("/{id}")
    public ExtendedAd getAds(@PathVariable int id) {
        return adsService.getAds(id);
    }

    @Tag(name = "Объявления")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAd(@PathVariable int id) {
        if (adsService.removeAd(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Tag(name = "Объявления")
    @PatchMapping("/{id}")
    public Ad updateAds(@PathVariable int id, @RequestBody CreateOrUpdateAd updateAd) {
        return adsService.updateAds(id, updateAd);
    }

    @Tag(name = "Объявления")
    @GetMapping("/me")
    public Ads getAdsMe() {
        return adsService.getAdsMe();
    }

    @Tag(name = "Объявления")
    @PatchMapping("/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestBody MultipartFile image) {
        byte[] bytes = adsService.updateImage(id, image);
        if (bytes.length > 0) {
            return ResponseEntity.ok()
                    .body(bytes);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Tag(name = "Комментарии")
    @GetMapping("/{id}/comments")
    public Comments getComments(@PathVariable int id) {
        return commentService.getComments(id);
    }

    @Tag(name = "Комментарии")
    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable int id, @RequestBody CreateOrUpdateComment comment) {
        return commentService.addComment(id, comment);
    }

    @Tag(name = "Комментарии")
    @DeleteMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int id, @PathVariable int commentId) {
        if (commentService.deleteComment(id, commentId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Tag(name = "Комментарии")
    @PatchMapping("/{id}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable int id, @PathVariable int commentId, @RequestBody CreateOrUpdateComment comment) {
        Comment result = commentService.updateComment(id, commentId, comment);
        if (result != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
