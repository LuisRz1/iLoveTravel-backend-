package edu.upa.pe.iloveltravelbackend.service;

import edu.upa.pe.iloveltravelbackend.dtos.TipDTO;
import edu.upa.pe.iloveltravelbackend.models.Article;
import edu.upa.pe.iloveltravelbackend.models.Tip;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.repositories.TipRepository;
import edu.upa.pe.iloveltravelbackend.services.TipService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TipServiceTest {
    @Mock
    private TipRepository tipRepository;

    @InjectMocks
    private TipService tipService;

    public TipServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTip() {
        Tip tip = new Tip();
        tip.setTiptitle("Some title");
        tip.setTiplocation("Some location");
        tip.setTipdescripcion("Some description");
        tip.setTipimagen("Some image URL");
        tip.setTiptype(Tip.TipType.COMIDA);
        User user = new User();
        Article article = new Article();

        tip.setUser(user);
        tip.setArticle(article);

        when(tipRepository.save(tip)).thenReturn(tip);

        String result = tipService.addTip(tip);

        assertEquals("Tip registrado correctamente", result);

        verify(tipRepository, times(1)).save(tip);
    }

    @Test
    void testGetAllTips() {
        List<Tip> tipList = new ArrayList<>();
        when(tipRepository.findAll()).thenReturn(tipList);
        List<TipDTO> result = tipService.getAllTips();
        verify(tipRepository, times(1)).findAll();
    }
    @Test
    void testUpdateTipRanking() {
        Long tipId = 1L;
        Double stars = 4.5;
        Tip tip = new Tip();
        tip.setRanking(3.0);
        when(tipRepository.findById(tipId)).thenReturn(java.util.Optional.of(tip));
        when(tipRepository.save(tip)).thenReturn(tip);

        tipService.updateTipRanking(tipId, stars);

        assertEquals(3.8, tip.getRanking());

        verify(tipRepository, times(1)).findById(tipId);
        verify(tipRepository, times(1)).save(tip);
    }

}
