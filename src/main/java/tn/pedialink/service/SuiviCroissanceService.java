package tn.pedialink.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.pedialink.dto.SuiviCroissanceDto;
import tn.pedialink.exception.BadRequestException;
import tn.pedialink.model.SuiviCroissance;
import tn.pedialink.repository.SuiviCroissanceRepository;

import java.util.List;

@Service
public class SuiviCroissanceService {

    @Autowired
    private SuiviCroissanceRepository suiviCroissanceRepository;

    public SuiviCroissance createSuivi(SuiviCroissanceDto dto) {
        validateDto(dto);
        
        SuiviCroissance suivi = new SuiviCroissance();
        suivi.setPatientId(dto.getPatientId());
        suivi.setDateMesure(dto.getDateMesure());
        suivi.setPoids(dto.getPoids());
        suivi.setTaille(dto.getTaille());
        suivi.setAgeEnMois(dto.getAgeEnMois());
        suivi.setObservation(dto.getObservation());
        
        // Calcul automatique de l'IMC
        suivi.setImc(calculerIMC(dto.getPoids(), dto.getTaille()));
        
        // Détermination du statut
        suivi.setStatut(determinerStatut(suivi.getImc(), dto.getAgeEnMois()));
        
        return suiviCroissanceRepository.save(suivi);
    }

    public List<SuiviCroissance> getAllSuivis() {
        return suiviCroissanceRepository.findAll();
    }

    public SuiviCroissance getSuiviById(String id) {
        return suiviCroissanceRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Suivi croissance non trouvé"));
    }

    public List<SuiviCroissance> getSuivisByPatient(String patientId) {
        return suiviCroissanceRepository.findByPatientIdOrderByDateMesureDesc(patientId);
    }

    public SuiviCroissance updateSuivi(String id, SuiviCroissanceDto dto) {
        validateDto(dto);
        
        SuiviCroissance suivi = getSuiviById(id);
        suivi.setPatientId(dto.getPatientId());
        suivi.setDateMesure(dto.getDateMesure());
        suivi.setPoids(dto.getPoids());
        suivi.setTaille(dto.getTaille());
        suivi.setAgeEnMois(dto.getAgeEnMois());
        suivi.setObservation(dto.getObservation());
        
        // Recalcul de l'IMC et du statut
        suivi.setImc(calculerIMC(dto.getPoids(), dto.getTaille()));
        suivi.setStatut(determinerStatut(suivi.getImc(), dto.getAgeEnMois()));
        
        return suiviCroissanceRepository.save(suivi);
    }

    public void deleteSuivi(String id) {
        SuiviCroissance suivi = getSuiviById(id);
        suiviCroissanceRepository.delete(suivi);
    }

    private Double calculerIMC(Double poids, Double taille) {
        if (poids == null || taille == null || taille == 0) {
            return null;
        }
        // IMC = poids (kg) / (taille (m))^2
        double tailleEnMetres = taille / 100.0;
        return Math.round((poids / (tailleEnMetres * tailleEnMetres)) * 100.0) / 100.0;
    }

    private String determinerStatut(Double imc, Integer ageEnMois) {
        if (imc == null || ageEnMois == null) {
            return "NORMAL";
        }
        
        // Logique simplifiée - à adapter selon les normes pédiatriques
        if (imc < 14) {
            return "RETARD_SEVERE";
        } else if (imc < 16) {
            return "RETARD_LEGER";
        } else {
            return "NORMAL";
        }
    }

    private void validateDto(SuiviCroissanceDto dto) {
        if (dto.getPatientId() == null || dto.getPatientId().isEmpty()) {
            throw new BadRequestException("Patient ID est requis");
        }
        if (dto.getDateMesure() == null) {
            throw new BadRequestException("Date de mesure est requise");
        }
        if (dto.getPoids() == null || dto.getPoids() <= 0) {
            throw new BadRequestException("Poids doit être positif");
        }
        if (dto.getTaille() == null || dto.getTaille() <= 0) {
            throw new BadRequestException("Taille doit être positive");
        }
    }
}
