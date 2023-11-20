package edu.upa.pe.iloveltravelbackend.dtos;

import edu.upa.pe.iloveltravelbackend.models.Tip;

public class TipDTO {
    private String tipTitle;
    private String tipLocation;
    private String tipDescription;
    private String tipImage;
    private Tip.TipType tipType;
    private Double ranking;
    private UserDTO userDTO;
    private ArticleDTO articleDTO;

    public TipDTO(Tip tip) {
        this.tipTitle = tip.getTiptitle();
        this.tipLocation = tip.getTiplocation();
        this.tipDescription = tip.getTipdescripcion();
        this.tipImage = tip.getTipdescripcion();
        this.tipType = tip.getTiptype();
        this.ranking = tip.getRanking();
        this.userDTO = new UserDTO(tip.getUser());
        this.articleDTO = new ArticleDTO(tip.getArticle());
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public ArticleDTO getArticleDTO() {
        return articleDTO;
    }

    public void setArticleDTO(ArticleDTO articleDTO) {
        this.articleDTO = articleDTO;
    }

    public String getTipTitle() {
        return tipTitle;
    }

    public String getTipLocation() {
        return tipLocation;
    }

    public String getTipDescription() {
        return tipDescription;
    }

    public String getTipImage() {
        return tipImage;
    }

    public Tip.TipType getTipType() {
        return tipType;
    }

    public Double getRanking(){
        return ranking;
    }
}
