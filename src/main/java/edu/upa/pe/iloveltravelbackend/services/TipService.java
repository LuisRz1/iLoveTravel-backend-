package edu.upa.pe.iloveltravelbackend.services;

import edu.upa.pe.iloveltravelbackend.dtos.TipDTO;
import edu.upa.pe.iloveltravelbackend.models.Tip;
import edu.upa.pe.iloveltravelbackend.repositories.TipRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class TipService {
    private final TipRepository tipRepository;

    @Autowired
    public TipService(TipRepository tipRepository) {
        this.tipRepository = tipRepository;
    }

    private boolean isEmptyOrWhitespace(String value) {
        return value == null || value.trim().isEmpty();
    }
    public String addTip(Tip tip) {
        validateTip(tip);
        tip.setRanking(0.0);
        Tip savedTip = tipRepository.save(tip);
        return "Tip registrado correctamente";
    }

    public List<TipDTO> getAllTips() {
        List<Tip> tips = tipRepository.findAll();
        List<TipDTO> tipsAll = new ArrayList<>();
        for (Tip tip : tips) {
            tipsAll.add(new TipDTO(tip));
        }
        return tipsAll;
    }

    public void updateTipRanking(Long tipid, Double stars) {
        if (stars == null || !Arrays.asList(1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0).contains(stars)) {
            throw new IllegalArgumentException("Las estrellas solo pueden ser enteras o tener decimal 5");
        }
        Tip tip = tipRepository.findById(tipid)
                .orElseThrow(() -> new IllegalArgumentException("Consejo no encontrado con ID: " + tipid));

        Double existingRanking = tip.getRanking();

        if (existingRanking != 0.0) {
            Double newRanking = (existingRanking + stars) / 2;
            newRanking = BigDecimal.valueOf(newRanking)
                    .setScale(1, RoundingMode.HALF_UP)
                    .doubleValue();
            tip.setRanking(newRanking);
        } else {
            tip.setRanking(stars);
        }

        tipRepository.save(tip);
    }
    private void validateTip(Tip tip) {
        if (isEmptyOrWhitespace(tip.getTipdescripcion()) || isEmptyOrWhitespace(tip.getTiplocation())
                || isEmptyOrWhitespace(tip.getTipimagen()) || tip.getArticle() == null
                || isEmptyOrWhitespace(String.valueOf(tip.getRanking())) || tip.getUser() == null) {
            throw new IllegalStateException("Todos los campos son requeridos");
        }
    }
}
