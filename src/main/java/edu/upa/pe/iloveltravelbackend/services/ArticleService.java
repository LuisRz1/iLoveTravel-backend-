package edu.upa.pe.iloveltravelbackend.services;

import edu.upa.pe.iloveltravelbackend.dtos.ArticleDTO;
import edu.upa.pe.iloveltravelbackend.dtos.TipDTO;
import edu.upa.pe.iloveltravelbackend.models.Article;
import edu.upa.pe.iloveltravelbackend.models.Tip;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.ArticleRepository;
import edu.upa.pe.iloveltravelbackend.repositories.TipRepository;
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
    private final TipRepository tipRepository;

    public ArticleService(ArticleRepository articleRepository, TipRepository tipRepository) {
        this.articleRepository = articleRepository;
        this.tipRepository = tipRepository;
    }
    public List<TipDTO> searchArticle(Article article) {
        String country = article.getCountry();
        String city = article.getCity();

        if (country == null || country.isEmpty()) {
            throw new IllegalStateException("Por favor, ingrese el nombre del país.");
        }

        List<Article> articles = articleRepository.findByCity(city);

        if (articles.isEmpty()) {
            throw new IllegalStateException("Artículo No Encontrado");
        }

        List<Tip> tips = tipRepository.findByArticle(articles.get(0)); // Obtén tips del primer artículo encontrado

        List<TipDTO> tipDTOs = tips.stream()
                .map(TipDTO::new)
                .collect(Collectors.toList());

        return tipDTOs;
    }
    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        List<ArticleDTO> articlesAll = new ArrayList<>();
        for (Article article : articles) {
            articlesAll.add(new ArticleDTO(article));
        }
        return articlesAll;
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
