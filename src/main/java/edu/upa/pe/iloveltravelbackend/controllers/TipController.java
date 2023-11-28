package edu.upa.pe.iloveltravelbackend.controllers;

import edu.upa.pe.iloveltravelbackend.dtos.TipDTO;
import edu.upa.pe.iloveltravelbackend.dtos.TipUpdateDTO;
import edu.upa.pe.iloveltravelbackend.dtos.UserDTO;
import edu.upa.pe.iloveltravelbackend.models.Tip;
import edu.upa.pe.iloveltravelbackend.models.User;
import edu.upa.pe.iloveltravelbackend.services.TipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tips")
public class TipController {

    private final TipService tipService;

    @Autowired
    public TipController(TipService tipService) {
        this.tipService = tipService;
    }

    @PostMapping("/añadir")
    public ResponseEntity<?> addTip(@RequestBody Tip tip){
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String newTip = tipService.addTip(tip);
            return new ResponseEntity<>(newTip, HttpStatus.CREATED);
        } catch (IllegalStateException sms){
            return new ResponseEntity<>(sms.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/viewTips")
    public List<TipDTO> getAllTips() {
        return tipService.getAllTips();
    }
    /*
    @PutMapping("/{tipId}/calificar")
    public ResponseEntity<String> updateTipRanking(@PathVariable Long tipId, @RequestParam int newRanking) {
        tipService.updateTipRanking(tipId, newRanking);
        return ResponseEntity.ok("Ranking del Tip actualizado exitosamente.");
    }


    @DeleteMapping("/delete/{tipId}")
    public ResponseEntity<String> deleteTip(@PathVariable Long tipId) {
        try {
            tipService.deleteTip(tipId);
            return new ResponseEntity<>("Tip eliminado correctamente", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("No se pudo eliminar el Tip" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    */

    @PutMapping("/update-ranking")
    public ResponseEntity<String> updateTipRanking(@RequestBody TipUpdateDTO tipUpdateDTO) {
        try {
            tipService.updateTipRanking(tipUpdateDTO.getTipid(), tipUpdateDTO.getStars());
            return ResponseEntity.ok("Ranking actualizado correctamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}