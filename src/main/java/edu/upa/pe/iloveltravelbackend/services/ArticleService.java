package edu.upa.pe.iloveltravelbackend.services;

import edu.upa.pe.iloveltravelbackend.dtos.ArticleDTO;
import edu.upa.pe.iloveltravelbackend.models.Article;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ArticleService {
    private final ArticleRepository articleRepository;
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<ArticleDTO> searchArticle(Article article) {
        String country = article.getCountry();
        String city = article.getCity();

        if (country == null || country.isEmpty()) {
            throw new IllegalStateException("Por favor, ingrese el nombre del país.");
        }

        List<Article> articles = articleRepository.findByCity(city);

        if (articles.isEmpty()) {
            throw new IllegalStateException("Artículo No Encontrado");
        }

        List<ArticleDTO> articleDTOs = articles.stream()
                .map(ArticleDTO::new)
                .collect(Collectors.toList());
        return articleDTOs;
    }
    private boolean isEmptyOrWhitespace(String value) {
        return value == null || value.trim().isEmpty();
    }

    public String addArticle(Article article) {
        if (isEmptyOrWhitespace(article.getCity())) {
            throw new IllegalStateException("Se necesita colocar la ciudad");
        }

        List<Article> existingArticleByCity = articleRepository.findByCity(article.getCity());

        if (!existingArticleByCity.isEmpty()) {
            throw new IllegalStateException("Ya se ha creado un artículo sobre esta ciudad");
        }

        articleRepository.save(article);
        return "ARTÍCULO CREADO CORRECTAMENTE";
    }

}
