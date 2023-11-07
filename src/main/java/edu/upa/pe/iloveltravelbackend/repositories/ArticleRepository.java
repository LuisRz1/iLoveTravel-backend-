package edu.upa.pe.iloveltravelbackend.repositories;

import edu.upa.pe.iloveltravelbackend.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByCity(String city);
}