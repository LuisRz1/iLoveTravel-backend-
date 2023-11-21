package edu.upa.pe.iloveltravelbackend.dtos;

public class TipUpdateDTO {
    private Long tipid;
    private Double stars;

    public Long getTipid() {
        return tipid;
    }

    public void setTipid(Long tipid) {
        this.tipid = tipid;
    }

    public Double getStars() {
        return stars;
    }

    public void setStars(Double stars) {
        this.stars = stars;
    }
}
