package edu.upa.pe.iloveltravelbackend.dtos;

import edu.upa.pe.iloveltravelbackend.models.Article;


public class ArticleDTO {
    private String country;
    private String city;
    private String description;

    public ArticleDTO(Article article) {
        this.country = article.getCountry();
        this.city = article.getCity();
        this.description = article.getDescription();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
