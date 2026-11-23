package tn.pedialink.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.pedialink.dto.ApiMessage;
import tn.pedialink.dto.SuiviCroissanceDto;
import tn.pedialink.model.SuiviCroissance;
import tn.pedialink.service.SuiviCroissanceService;

import java.util.List;

@RestController
@RequestMapping("/api/suivi-croissance")
@CrossOrigin(origins = "*")
public class SuiviCroissanceController {

    @Autowired
    private SuiviCroissanceService suiviCroissanceService;

    @PostMapping
    public ResponseEntity<SuiviCroissance> createSuivi(@RequestBody SuiviCroissanceDto dto) {
        SuiviCroissance suivi = suiviCroissanceService.createSuivi(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(suivi);
    }

    @GetMapping
    public ResponseEntity<List<SuiviCroissance>> getAllSuivis() {
        List<SuiviCroissance> suivis = suiviCroissanceService.getAllSuivis();
        return ResponseEntity.ok(suivis);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuiviCroissance> getSuiviById(@PathVariable String id) {
        SuiviCroissance suivi = suiviCroissanceService.getSuiviById(id);
        return ResponseEntity.ok(suivi);
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<SuiviCroissance>> getSuivisByPatient(@PathVariable String patientId) {
        List<SuiviCroissance> suivis = suiviCroissanceService.getSuivisByPatient(patientId);
        return ResponseEntity.ok(suivis);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuiviCroissance> updateSuivi(
            @PathVariable String id,
            @RequestBody SuiviCroissanceDto dto) {
        SuiviCroissance suivi = suiviCroissanceService.updateSuivi(id, dto);
        return ResponseEntity.ok(suivi);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiMessage> deleteSuivi(@PathVariable String id) {
        suiviCroissanceService.deleteSuivi(id);
        return ResponseEntity.ok(new ApiMessage("Suivi croissance supprimé avec succès"));
    }
}
