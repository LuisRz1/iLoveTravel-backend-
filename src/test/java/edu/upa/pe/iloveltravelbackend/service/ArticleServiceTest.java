package edu.upa.pe.iloveltravelbackend.service;
import edu.upa.pe.iloveltravelbackend.dtos.ArticleDTO;
import edu.upa.pe.iloveltravelbackend.models.Article;
import edu.upa.pe.iloveltravelbackend.repositories.ArticleRepository;
import edu.upa.pe.iloveltravelbackend.services.ArticleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ArticleServiceTest {
    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSearchArticle_Success() {
        String city = "SampleCity";
        String country = "SampleCountry";
        Article article = new Article();
        article.setCity(city);
        article.setCountry(country);
        when(articleRepository.findByCity(city)).thenReturn(Collections.singletonList(article));

        List<ArticleDTO> result = articleService.searchArticle(article);
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    @Test
    public void testSearchArticle_NoCountry() {
        Article article = new Article();
        assertThrows(IllegalStateException.class, () -> articleService.searchArticle(article));
    }

    @Test
    public void testSearchArticle_NoArticleFound() {
        String city = "NonExistentCity";
        Article article = new Article();
        article.setCity(city);
        when(articleRepository.findByCity(city)).thenReturn(Collections.emptyList());
        assertThrows(IllegalStateException.class, () -> articleService.searchArticle(article));
    }

    @Test
    public void testAddArticle_Success() {
        Article article = new Article();
        article.setCity("NewCity");
        when(articleRepository.findByCity("NewCity")).thenReturn(Collections.emptyList());
        String result = articleService.addArticle(article);
        assertEquals("ARTÍCULO CREADO CORRECTAMENTE", result);
        verify(articleRepository, times(1)).save(article);
    }

    @Test
    public void testAddArticle_DuplicateCity() {
        Article article = new Article();
        article.setCity("ExistingCity");
        when(articleRepository.findByCity("ExistingCity")).thenReturn(Collections.singletonList(article));
        assertThrows(IllegalStateException.class, () -> articleService.addArticle(article));
    }

    @Test
    public void testAddArticle_EmptyCity() {
        Article article = new Article();
        assertThrows(IllegalStateException.class, () -> articleService.addArticle(article));
    }
}
