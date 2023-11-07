package edu.upa.pe.iloveltravelbackend.controllers;

import edu.upa.pe.iloveltravelbackend.dtos.ArticleDTO;
import edu.upa.pe.iloveltravelbackend.models.Article;
import edu.upa.pe.iloveltravelbackend.services.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    private final ArticleService articleService;
    public ArticleController(ArticleService articleService){
        this.articleService = articleService;
    }
    @PostMapping("/search")
    public ResponseEntity<?> searchArticle(@RequestBody Article article) {
        try {
            List<ArticleDTO> articles = articleService.searchArticle(article);
            return ResponseEntity.ok(articles);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/create")
    public ResponseEntity<?> addArticle(@RequestBody Article article){
        try{
            String newArticle = articleService.addArticle(article);
            return new ResponseEntity<>(newArticle, HttpStatus.CREATED);
        } catch (IllegalStateException sms) {
            return new ResponseEntity<>(sms.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}