package edu.upa.pe.iloveltravelbackend.repositories;

import edu.upa.pe.iloveltravelbackend.models.Article;
import edu.upa.pe.iloveltravelbackend.models.Tip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TipRepository extends JpaRepository<Tip, Long> {
    List<Tip> findByArticle(Article article);
}
