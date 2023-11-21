package edu.upa.pe.iloveltravelbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tips")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tip {
    public enum TipType {
        COMIDA,
        HOSPEDAJE,
        ATRACCIONES,
        AHORROS,
        TRANSPORTE
    }
    @Getter
    @Id
    @Column(name = "tip_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tipid;
    @Getter
    @Column(name = "tip_title")
    private String tiptitle;
    @Getter
    @Column(name = "tip_location")
    private String tiplocation;
    @Getter
    @Column(name = "tip_descripcion")
    private String tipdescripcion;
    @Getter
    @Column(name = "tip_imagen")
    private String tipimagen;
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "tip_type")
    private TipType tiptype;
    @Getter
    @Column(name = "ranking")
    private Double ranking;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;


    public Long getTipid() {
        return tipid;
    }

    public void setTipid(Long tipid) {
        this.tipid = tipid;
    }

    public String getTiptitle() {
        return tiptitle;
    }

    public void setTiptitle(String tiptitle) {
        this.tiptitle = tiptitle;
    }

    public String getTiplocation() {
        return tiplocation;
    }

    public void setTiplocation(String tiplocation) {
        this.tiplocation = tiplocation;
    }

    public String getTipdescripcion() {
        return tipdescripcion;
    }

    public void setTipdescripcion(String tipdescripcion) {
        this.tipdescripcion = tipdescripcion;
    }

    public String getTipimagen() {
        return tipimagen;
    }

    public void setTipimagen(String tipimagen) {
        this.tipimagen = tipimagen;
    }

    public TipType getTiptype() {
        return tiptype;
    }

    public void setTiptype(TipType tiptype) {
        this.tiptype = tiptype;
    }

    public Double getRanking() {
        return ranking;
    }

    public void setRanking(Double ranking) {
        this.ranking = ranking;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
